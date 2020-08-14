package com.itheima.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import java.util.Iterator;
import java.util.List;

/**
 * 查询当前用户的代办工作
 */
public class ActivitiTaskQuery {
    public static void main(String[] args) {
        // 1 得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 2、得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        // 3、根据流程定义的key， 负责人assgine来实现当前用户的任务列表查询
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("holiday")
                .taskAssignee("zhangsan")
                .list();
        System.out.println("一共有" + taskList.size() +"条任务");
        // 4、任务列表的展示
        for (Iterator<Task> iterator = taskList.iterator();iterator.hasNext();){
            Task task = iterator.next();
            System.out.println("流程实例ID：" +  task.getProcessInstanceId());
            System.out.println("任务ID：" +  task.getId());
            System.out.println("任务负责人：" +  task.getAssignee());
            System.out.println("任务名称：" +  task.getName());
        }
    }
}
