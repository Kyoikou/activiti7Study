package com.itheima.activiti.threeBusinessKey;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * 启动流程实例，获取BusinessKey
 * 本质是影响act_execution这张表
 */
public class BusinessKeyAdd {
    public static void main(String[] args) {
        // 1 得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 2 得到RunService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 3 启动流程实例 同时还要指定业务标识BusinessKey，它本身就是请假单的ID,
        // 第一个参数：流程定义的key  第二个参数，自定义的businessKey
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday","1001");

        // 4 输出processInstance相关属性
        System.out.println(processInstance.getBusinessKey());
        System.out.println(processInstance.getId());
    }
}
