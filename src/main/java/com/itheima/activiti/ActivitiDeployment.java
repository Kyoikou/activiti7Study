package com.itheima.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;

/**
 * 流程定义的部署
 */
public class ActivitiDeployment {
    /**
     *  流程定义的部署
     *  activiti的表有哪些？
     *  act_re_deployment  流程部署表
     *  act_ge_bytearray  .bpnm .png文件资源表
     *  act_ge_property     activity系统配置表  版本号。。
     * @param args
     */
    public static void main(String[] args) {
        // 1、创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 2、得到相关的service - RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 3、进行部署  图片名字前缀必须与bpmn文件名前缀一致
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("diagram/holiday.bpmn")
                .addClasspathResource("diagram/holiday.png")
                .name("请假申请流程")
                .deploy();
        // 4、输出部署的一些信息  ID的取值哪里来的？
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
}
