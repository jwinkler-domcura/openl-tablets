package org.openl.rules.ui.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openl.rules.lang.xls.syntax.TableSyntaxNode;
import org.openl.rules.ui.IProjectTypes;
import org.openl.rules.ui.OpenMethodGroupsDictionary;
import org.openl.rules.ui.TableSyntaxNodeUtils;
import org.openl.types.IOpenMethod;
import org.openl.types.impl.AOpenClass.MethodKey;

/**
 * Builds tree node for group of methods.
 * 
 */
public class OpenMethodInstancesGroupTreeNodeBuilder extends OpenMethodsGroupTreeNodeBuilder {

    private static final String TABLE_INSTANCES_GROUP_NAME = "Table Group Instance";

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getDisplayValue(Object nodeObject, int i) {

        TableSyntaxNode tableSyntaxNode = (TableSyntaxNode) nodeObject;

        return TableSyntaxNodeUtils.getTableDisplayValue(tableSyntaxNode, i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return TABLE_INSTANCES_GROUP_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType(Object sorterObject) {
        return IProjectTypes.PT_TABLE_GROUP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrl(Object sorterObject) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWeight(Object sorterObject) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object makeObject(TableSyntaxNode tableSyntaxNode) {
        return tableSyntaxNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectTreeNode makeNode(TableSyntaxNode tableSyntaxNode, int i) {

        if (tableSyntaxNode.getMember() instanceof IOpenMethod) {

            IOpenMethod method = (IOpenMethod) tableSyntaxNode.getMember();

            OpenMethodGroupsDictionary openMethodGroupsDictionary = getOpenMethodGroupsDictionary();

            if (openMethodGroupsDictionary.contains(method)) {
                List<IOpenMethod> groupMethods = openMethodGroupsDictionary.getGroup(method);

                // If group of methods size is over then 1 create the tree
                // element (folder); otherwise - method is unique and additional
                // element will not be created.
                // author: Alexey Gamanovich
                //
                if (groupMethods != null && groupMethods.size() > 1) {

                    String folderName = getMajorityName(groupMethods);

                    return makeFolderNode(folderName);
                }
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Comparable<?> makeKey(TableSyntaxNode tableSyntaxNode, int i) {

        if (tableSyntaxNode.getMember() instanceof IOpenMethod) {

            IOpenMethod method = (IOpenMethod) tableSyntaxNode.getMember();
            MethodKey methodKey = new MethodKey(method);

            int hash = methodKey.hashCode();
            String hashString = String.valueOf(hash);

            Object nodeObject = makeObject(tableSyntaxNode);

            return new NodeKey(getWeight(nodeObject), new String[] { hashString, hashString, hashString });
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getProblems(Object nodeObject) {
        return null;
    }

    /**
     * Gets the majority name of methods group.
     * 
     * @param methods collection of methods what belong to group.
     * @return majority name
     */
    private String getMajorityName(List<IOpenMethod> methods) {

        Map<String, Integer> map = new HashMap<String, Integer>();

        for (IOpenMethod method : methods) {
            String[] names = getDisplayValue(method.getInfo().getSyntaxNode(), 0);
            String name = names[0];

            Integer value = map.get(name);

            if (value == null) {
                value = 0;
            }

            value += 1;
            map.put(name, value);
        }

        Integer maxNameWeight = 0;
        String majorName = StringUtils.EMPTY;

        Set<Map.Entry<String, Integer>> entries = map.entrySet();

        for (Map.Entry<String, Integer> entry : entries) {

            if (maxNameWeight.compareTo(entry.getValue()) < 0) {
                maxNameWeight = entry.getValue();
                majorName = entry.getKey();
            }
        }

        return majorName;
    }

    /**
     * Makes node that represents folder.
     * 
     * @param folderName name of folder node
     * @return tree node
     */
    private ProjectTreeNode makeFolderNode(String folderName) {
        return new ProjectTreeNode(new String[] { folderName, folderName, folderName }, "folder", null, null, 0, null);
    }
}