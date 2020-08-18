package com.itheima.activiti.gateway;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

public class ExclusiveComplete {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = defaultProcessEngine.getTaskService();

        String user = "zhaoliu";
        String key = "parallelGateway";

        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(user)
                .singleResult();
        if (task !=null){
            // 处理任务
            taskService.complete(task.getId());

            System.out.println(task.getId() + "任务已完成");
        }
    }
}
