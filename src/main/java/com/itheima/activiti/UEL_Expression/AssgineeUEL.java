package com.itheima.activiti.UEL_Expression;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;

import java.util.HashMap;
import java.util.Map;

/**
 * 启动流程实例，动态设置assgnee
 */
public class AssgineeUEL {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        Map<String, Object> variables = new HashMap<>();

        variables.put("assignee0","zhangsan");
        variables.put("assignee1","lisi");
        variables.put("assignee2","wangwu");

        runtimeService.startProcessInstanceByKey("holiday", "assginees",variables);

    }
}
