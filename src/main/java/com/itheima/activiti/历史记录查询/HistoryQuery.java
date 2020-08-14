package com.itheima.activiti.历史记录查询;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;

import java.util.List;

public class HistoryQuery {
    public static void main(String[] args) {
        // 1、创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 2、得到HistoryService
        HistoryService historyService = processEngine.getHistoryService();

        // 3、得到HistoryActionInstanceQuery对象  历史行为查询器
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
        for (HistoricActivityInstance instance1 : historicActivityInstanceQuery.processInstanceId("10001").list()){
            System.out.println("************");
            System.out.println("实例活动中及活动过的ID：" + instance1.getActivityId());
            System.out.println("实例名字：" + instance1.getActivityName());
            System.out.println("实例部署的流程定义ID：" + instance1.getProcessDefinitionId());
            System.out.println("实例部署的流程ID2：" + instance1.getProcessInstanceId());
        }

        System.out.println("***************");
        // 4、设置查询条件
        List<HistoricActivityInstance> list = historicActivityInstanceQuery.list(); // 前面被设置过.processInstanceId("10001")  已设置过查询条件

        // 5、遍历查询结果
        for (HistoricActivityInstance instance : list){
            System.out.println("************");
            System.out.println("实例活动中及活动过的ID：" + instance.getActivityId());
            System.out.println("实例名字：" + instance.getActivityName());
            System.out.println("实例部署的流程定义ID：" + instance.getProcessDefinitionId());
            System.out.println("实例部署的流程ID2：" + instance.getProcessInstanceId());
        }
    }
}
