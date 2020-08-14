package com.itheima.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;

/**
 * 完成用户任务
 * @author 180681
 */
public class ActivitiTaskComplete {
    public static void main(String[] args) {
        // 1 得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 2、得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        // 3、处理任务，结合当前用户列表的查询操作的话。任务ID10005
        taskService.complete("10005");
    }
}
