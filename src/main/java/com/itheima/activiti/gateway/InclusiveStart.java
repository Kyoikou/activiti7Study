package com.itheima.activiti.gateway;

import com.itheima.activiti.entities.Holiday;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.HashMap;
import java.util.Map;

public class InclusiveStart {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        Map<String, Object> variables = new HashMap<>();


        Integer userType = 2;

        variables.put("userType", userType);

        ProcessInstance candidateDemo = runtimeService.startProcessInstanceByKey("inclusive","inclusive11", variables);

        System.out.println("流程部署ID " + candidateDemo.getDeploymentId());
        System.out.println("流程定义ID " + candidateDemo.getProcessDefinitionId());
        System.out.println("流程实例ID " + candidateDemo.getId());

        System.out.println("活动ID具体的哪个节点 " + candidateDemo.getActivityId());
    }
}
