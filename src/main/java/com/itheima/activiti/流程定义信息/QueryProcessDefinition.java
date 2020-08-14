package com.itheima.activiti.流程定义信息;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;

import java.util.List;

/**
 * <b>查询流程定义信息</b>
 */
public class QueryProcessDefinition {
    public static void main(String[] args) {
        // 1、得到ProcessEngfine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 2、创建流程定义repositiory对象
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 3、得到processDefinitionQuery对象，可以认为它就是一个查询器
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        // 4、设置条件，并查询出流程定义的信息 查询条件  流程定义的key= holiday
        //                                          orderByProcessDefinitionVersion()设置流程排序方式，根据流程定义的版本号进行排序
        //                                          desc  降序
        List<ProcessDefinition> list = processDefinitionQuery.processDefinitionKey("holiday")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();

        // 5、基本信息的输出
        for (ProcessDefinition processDefinition : list){
            System.out.println("流程定义的ID： " + processDefinition.getId());
            System.out.println("流程定义的名称： " + processDefinition.getName());
            System.out.println("流程定义的key： " + processDefinition.getKey());
            System.out.println("流程定义的版本号： " + processDefinition.getVersion());
            System.out.println("**************");
        }

    }
}
