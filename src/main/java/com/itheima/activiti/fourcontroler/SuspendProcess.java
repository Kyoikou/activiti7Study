package com.itheima.activiti.fourcontroler;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;

public class SuspendProcess {
    public static void main(String[] args) {
        // 1 得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 2 得到RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 3 查询流程对应对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("holiday")
                .list().get(0);

        // 4 得到当前流程对应中是否所有实例全部激活/暂停状态
        boolean suspended = processDefinition.isSuspended();

        // 5 判断
        if (suspended){
            // 说明是暂停，就可以激活操作
            System.out.println("定义：" + processDefinition.getId() + "激活");
            repositoryService.activateProcessDefinitionById(processDefinition.getId(), true, null);
        }else {
            // 否则挂起
            System.out.println("定义：" + processDefinition.getId() + "挂起");
            repositoryService.suspendProcessDefinitionById(processDefinition.getId(), true, null);
        }
    }
}
