package com.itheima.activiti.groupTest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;

/**
 * 候选人测试
 */
public class CandidateTest {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("diagram/candidate.bpmn")
                .addClasspathResource("diagram/candidate.png")
                .name("候选人请假流程")
                .deploy();
        // 4、输出部署的一些信息  ID的取值哪里来的？
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
}
