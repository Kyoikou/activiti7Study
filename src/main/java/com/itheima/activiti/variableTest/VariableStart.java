package com.itheima.activiti.variableTest;

import com.itheima.activiti.entities.Holiday;
import com.itheima.activiti.entities.UserService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * 启动流程实例，设置流程变量
 */
public class VariableStart {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        Holiday holiday = new Holiday();
        float holidayNum  = 1f;
        holiday.setNum(holidayNum);

        Map<String, Object> variables = new HashMap<>();

        variables.put("holiday", holiday);
        variables.put("authService", new UserService());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_1", "holidayPlus3", variables);

        // 输出实例信息
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getName());
        System.out.println(processInstance.getBusinessKey());
    }
}
