package com.itheima.activiti.groupTest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

public class TaskServiceExchange {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();

        Task task = taskService.createTaskQuery()
                .processDefinitionKey("candidateDemo")
                .taskAssignee("lisi")
                .singleResult();
        if (task != null){
            taskService.setAssignee(task.getId(), "wangwu");
            System.out.println(task.getId() + "交接任务");
        }
    }
}
