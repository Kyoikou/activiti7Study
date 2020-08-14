package com.itheima.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Scanner;

/**
 * 查询用户的任务，并处理掉任务
 */
public class ActivitiTaskDeal {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = processEngine.getTaskService();

        List<Task> tasksList = taskService.createTaskQuery()
                .processDefinitionKey("holiday")
                .taskAssignee("zhangsan")
                .list();

        System.out.println("你当前代办的工作有：");
        int i = 0;
        for (Task task : tasksList){
            System.out.println("序号：" + (++i) +"  任务ID：" + task.getId() + "   任务名称：" + task.getName() + "   任务负责人：" + task.getAssignee());
        }

        System.out.println("请选择你需要处理的任务：");

        Scanner scanner = new Scanner(System.in);

        int taskIndex = Integer.valueOf(scanner.nextLine());

        Task task = tasksList.get(taskIndex - 1);

        taskService.complete(task.getId());
        // 关闭引擎
        processEngine.close();
    }
}
