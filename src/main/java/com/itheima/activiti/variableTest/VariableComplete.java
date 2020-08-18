package com.itheima.activiti.variableTest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Scanner;

public class VariableComplete {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = defaultProcessEngine.getTaskService();

        String assignee = "lisi";

        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .processInstanceBusinessKeyLike("holidayPlus2")
                .list();

        System.out.println(assignee + "当前拥有" + list.size() + "代办工作");
        int i = 0;
        for (Task task : list){
            System.out.println("序号:" + (++i));
            System.out.println("任务ID" + task.getId());
            System.out.println("任务名字" + task.getName());
            System.out.println("************");
        }

        System.out.println("选择你要执行的任务：");

        Scanner scanner = new Scanner(System.in);

        int taskIndex = Integer.valueOf(scanner.nextLine());

        Task task = list.get(taskIndex - 1);

        taskService.complete(task.getId());
        // 关闭引擎
        defaultProcessEngine.close();
    }
}
