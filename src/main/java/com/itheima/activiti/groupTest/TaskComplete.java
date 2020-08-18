package com.itheima.activiti.groupTest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

public class TaskComplete {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = defaultProcessEngine.getTaskService();

        Task task = taskService.createTaskQuery()
                .processDefinitionKey("candidateDemo")
                .taskAssignee("zhangsan")
                .singleResult();
        if (task !=null){
            // 处理任务
            taskService.complete(task.getId());

            System.out.println(task.getId() + "任务已完成");
        }
    }
}


