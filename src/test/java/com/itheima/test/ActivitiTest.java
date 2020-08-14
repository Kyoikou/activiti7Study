package com.itheima.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.junit.Test;

/**
 * 测试类
 *  作用：测试activiti所需要的25张表的生成
 */
public class ActivitiTest {
    @Test
    public void testGentable(){
        // 1.创建ProcessEngineConfiguration对象  第一个参数：配置文件名字  第二个参数名字：  bean的ID名字  默认processEngineConfiguration
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
//        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml","processEngineConfiguration01");
        // 2.创建ProcessEngine对象
        ProcessEngine processEngine = configuration.buildProcessEngine();

        // 3.输出processEngine对象
        System.out.println(processEngine);

        RuntimeService runtimeService = processEngine.getRuntimeService();

    }

    /**
     *  创建流程引擎第二种方式
     */
    @Test
    public void testGentable2(){
        // 条件：
        /*
            1、activiti配置文件名称  activiti.cfg.xml
            2、bean的id="processEngineConfiguration"
         */
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);
    }
}
