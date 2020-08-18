package com.itheima.activiti.variableTest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;

/**
 * 流程变量的测试
 */
public class VariableTest {
    // 新的请假流程测试
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("diagram/gatewaydemo.bpmn")
                .addClasspathResource("diagram/gatewaydemo.png")
                .name("请假流程变量控制")
                .deploy();

        // 输出部署信息
        System.out.println(deploy.getName());
        System.out.println(deploy.getId());
    }
}
