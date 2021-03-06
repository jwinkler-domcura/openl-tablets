package org.openl.rules.ruleservice.storelogdata.elasticsearch;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.openl.binding.MethodUtil;
import org.openl.rules.ruleservice.storelogdata.AbstractStoreLogDataService;
import org.openl.rules.ruleservice.storelogdata.Inject;
import org.openl.rules.ruleservice.storelogdata.StoreLogData;
import org.openl.rules.ruleservice.storelogdata.StoreLogDataMapper;
import org.openl.rules.ruleservice.storelogdata.annotation.AnnotationUtils;
import org.openl.rules.ruleservice.storelogdata.elasticsearch.annotation.StoreLogDataToElasticsearch;
import org.openl.spring.config.ConditionalOnEnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnEnable("ruleservice.store.logs.elasticsearch.enabled")
public class ElasticSearchStoreLogDataService extends AbstractStoreLogDataService {

    private final Logger log = LoggerFactory.getLogger(ElasticSearchStoreLogDataService.class);

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    private final StoreLogDataMapper storeLogDataMapper = new StoreLogDataMapper();

    private volatile Collection<Inject<?>> supportedInjects;

    public ElasticsearchOperations getElasticsearchOperations() {
        return elasticsearchOperations;
    }

    public void setElasticsearchOperations(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    protected boolean isSync(StoreLogData storeLogData) {
        StoreLogDataToElasticsearch storeLogDataToElasticsearch = AnnotationUtils
            .getAnnotationInServiceClassOrServiceMethod(storeLogData, StoreLogDataToElasticsearch.class);
        if (storeLogDataToElasticsearch != null) {
            return storeLogDataToElasticsearch.sync();
        }
        return false;
    }

    @Override
    public Collection<Inject<?>> additionalInjects() {
        if (supportedInjects == null) {
            synchronized (this) {
                if (supportedInjects == null) {
                    Collection<Inject<?>> injects = new ArrayList<>();
                    injects.add(new Inject<>(InjectElasticsearchOperations.class, this::getElasticsearchOperations));
                    supportedInjects = Collections.unmodifiableCollection(injects);
                }
            }
        }
        return supportedInjects;
    }

    @Override
    protected void save(StoreLogData storeLogData, boolean sync) {
        Object[] entities;

        StoreLogDataToElasticsearch storeLogDataToElasticsearchAnnotation = storeLogData.getServiceClass()
            .getAnnotation(StoreLogDataToElasticsearch.class);

        Method serviceMethod = storeLogData.getServiceMethod();
        if (serviceMethod != null && serviceMethod.isAnnotationPresent(StoreLogDataToElasticsearch.class)) {
            storeLogDataToElasticsearchAnnotation = serviceMethod.getAnnotation(StoreLogDataToElasticsearch.class);
        }
        if (storeLogDataToElasticsearchAnnotation == null) {
            return;
        }

        if (storeLogDataToElasticsearchAnnotation.value().length == 0) {
            entities = new DefaultElasticEntity[] { new DefaultElasticEntity() };
        } else {
            entities = new Object[storeLogDataToElasticsearchAnnotation.value().length];
            int i = 0;
            for (Class<?> entityClass : storeLogDataToElasticsearchAnnotation.value()) {
                if (StoreLogDataToElasticsearch.DEFAULT.class == entityClass) {
                    entities[i] = new DefaultElasticEntity();
                } else {
                    try {
                        entities[i] = entityClass.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        if (log.isErrorEnabled()) {
                            log.error(String.format(
                                "Failed to instantiate ElasticSearch index builder%s. Please, check that class '%s' is not abstract and has a default constructor.",
                                serviceMethod != null ? " for method '" + MethodUtil
                                    .printQualifiedMethodName(serviceMethod) + "'" : StringUtils.EMPTY,
                                entityClass.getTypeName()), e);
                        }
                        return;
                    }
                }
                i++;
            }
        }

        IndexQuery[] indexQueries = new IndexQuery[entities.length];
        int i = 0;

        for (Object entity : entities) {
            try {
                storeLogDataMapper.map(storeLogData, entity);
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    if (serviceMethod != null) {
                        log.error("Failed to populate Elasticsearch index related to method '{}' in class '{}'.",
                            MethodUtil.printQualifiedMethodName(serviceMethod),
                            entity.getClass().getTypeName(),
                            e);
                    } else {
                        log.error("Failed to populate Elasticsearch index related to class '{}'.",
                            entity.getClass().getTypeName(),
                            e);
                    }
                }
                return;
            }
        }

        for (Object entity : entities) {
            Class<?> clazz = entity.getClass();
            IndexQuery indexQuery = new IndexQueryBuilder().withIndexName(extractIndexName(clazz))
                .withType(null)
                .withId(extractId(entity))
                .withObject(entity)
                .withVersion(null)
                .withSource(null)
                .withParentId(null)
                .build();
            indexQueries[i++] = indexQuery;
        }
        for (IndexQuery indexQuery : indexQueries) {
            if (indexQuery != null) {
                try {
                    elasticsearchOperations.index(indexQuery);
                    elasticsearchOperations.refresh(indexQuery.getIndexName());
                } catch (Exception e) {
                    // Continue the loop if exception occurs
                    log.error("Failed on ElasticSearch entity save operation.", e);
                }
            }
        }
    }

    private String extractId(Object entity) {
        String existingId = null;

        for (Field f : entity.getClass().getDeclaredFields()) {
            Id[] annotationsByType = f.getAnnotationsByType(Id.class);
            if (annotationsByType.length != 0) {
                try {
                    f.setAccessible(true);
                    existingId = (String) f.get(entity);
                } catch (IllegalAccessException e) {
                    log.error("Failed on ElasticSearch entity '{}' extract ID operation.",
                        entity.getClass().getTypeName(),
                        e);
                }
            }
        }
        if (existingId == null) {
            existingId = UUID.randomUUID().toString();
        }
        return existingId;
    }

    private String extractIndexName(Class<?> clazz) {
        String indexName = clazz.getAnnotation(Document.class).indexName();
        try {
            return URLEncoder.encode(indexName, "UTF-8").toLowerCase();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

}
