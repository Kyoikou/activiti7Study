package com.itheima.activiti.groupTest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;

public class CandidateStart {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        ProcessInstance candidateDemo = runtimeService.startProcessInstanceByKey("candidateDemo");

        System.out.println("流程部署ID " + candidateDemo.getDeploymentId());
        System.out.println("流程定义ID " + candidateDemo.getProcessDefinitionId());
        System.out.println("流程实例ID " + candidateDemo.getId());

        System.out.println("活动ID具体的哪个节点 " + candidateDemo.getActivityId());
    }
}
