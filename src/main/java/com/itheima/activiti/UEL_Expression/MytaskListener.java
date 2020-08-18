package com.itheima.activiti.UEL_Expression;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class MytaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        // 这里指定任务负责人
        delegateTask.setAssignee("张三");
    }
}
