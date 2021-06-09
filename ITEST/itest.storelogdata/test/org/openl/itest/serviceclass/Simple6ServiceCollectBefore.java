package org.openl.itest.serviceclass;

import java.util.Map;

import org.openl.rules.ruleservice.storelogdata.advice.StoreLogDataAdvice;

public class Simple6ServiceCollectBefore implements StoreLogDataAdvice {

    @Override
    public void prepare(Map<String, Object> values, Object[] args, Object result, Exception ex) {
        values.put("id", String.valueOf(args[0]));
    }
}
