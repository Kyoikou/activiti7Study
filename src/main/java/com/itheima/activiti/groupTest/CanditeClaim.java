package com.itheima.activiti.groupTest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

/**
 * 拾取任务就是将候选用户转化为真正的任务持有人（让assignee有值）
 */
public class CanditeClaim {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = defaultProcessEngine.getTaskService();

        String key = "candidateDemo";
        String candidate_users = "lisi";

        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskCandidateUser(candidate_users)
                .singleResult();
        if (task !=null){
            // 拾取任务
            taskService.claim(task.getId(), candidate_users);

            System.out.println(task.getId() + "任务已被拾取");
        }
    }
}
