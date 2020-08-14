package com.itheima.activiti.流程定义信息;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;

/**
 * <b>删除已经部署的流程定义</b>
 */
public class DeleteProcess {
    public static void main(String[] args) {
        // 1、创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 2、得到相关的service - RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 3、执行删除流程定义
        repositoryService.deleteDeployment("2501", true);
    }
}
