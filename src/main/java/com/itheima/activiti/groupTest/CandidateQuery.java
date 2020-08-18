package com.itheima.activiti.groupTest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import java.util.List;

/**
 * 查询用户组任务
 */

public class CandidateQuery {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = defaultProcessEngine.getTaskService();

        String key = "candidateDemo";
        String candidate_users = "lisi";

//        List<Task> taskList = taskService.createTaskQuery()
//                .processDefinitionKey(key)
//                .taskCandidateUser(candidate_users)     // 设置候选用户
//                .list();

        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(candidate_users)     // 设置候选用户
                .list();

        for (Task task : taskList){
            System.out.println(task.getId());
            System.out.println(task.getProcessInstanceId());
            System.out.println(task.getName());
            System.out.println(task.getAssignee());
            System.out.println("**********");
        }

    }
}
