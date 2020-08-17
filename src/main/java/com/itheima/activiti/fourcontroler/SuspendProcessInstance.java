package com.itheima.activiti.fourcontroler;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;

public class SuspendProcessInstance {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey("1001")
                .singleResult();

        boolean suspended = processInstance.isSuspended();

        String processInstanceId = processInstance.getId();

        System.out.println("流程实例ID" + processInstanceId);

        if (suspended){
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println("激活");
        }else {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println("挂起");
        }
    }
}
