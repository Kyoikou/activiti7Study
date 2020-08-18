package com.itheima.activiti.variableTest;

import com.itheima.activiti.entities.Holiday;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Scanner;

/**
 * 设置流程变量有两种method
 * 一： .setVariable(task.getId(), variableName, variable)  设置单个变量
 * 二： .setVariable(task.getId(), map)                     设置多个变量
 * 设置流程变量有N种方法
 * 1、启动一个流程实例时设置  runtimeService.startProcessInstanceByKey("myProcess_1", "holidayPlus3", variables);
 * 2、TaskSerivece.complete(task.getId(),variablesMap)  完成用户任务时触发
 * 3、随时用RunSerivece.setVariable(execution.getId(), variableName, variable)  通过流程运行实例设置流程变量
 * 4、TaskService.setVariable(task.getId(), variableName, variable)             通过用户任务ID设置
 *
 *  总的来说就两种，与流程实例相关，与任务相关
 *
 * 这个类主要使用第3种
 */
public class VariableSetting {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        TaskService taskService = defaultProcessEngine.getTaskService();

        List<Task> taskList = taskService.createTaskQuery()
                .processInstanceBusinessKeyLike("holidayPlus")
                .list();

        System.out.println("当前有代办工作：" + taskList.size() + "个");
        int i = 0;
        for (Task task : taskList){
            System.out.println("序号:" + (++i));
            System.out.println("任务ID" + task.getId());
            System.out.println("任务名字" + task.getName());
            System.out.println("任务处理人" + task.getAssignee());
            System.out.println("任务流程ID：" + task.getProcessInstanceId());
            System.out.println("************");
        }

        System.out.println("选择你要执行的任务：");

        Scanner scanner = new Scanner(System.in);

        int taskIndex = Integer.valueOf(scanner.nextLine());

        Task task = taskList.get(taskIndex - 1);

        Execution execution = runtimeService.createExecutionQuery()
                .executionId(task.getExecutionId())
                .singleResult();
        Holiday holiday = new Holiday();
        holiday.setNum(5f);

        runtimeService.setVariable(execution.getId(), "holiday", holiday);
    }
}
