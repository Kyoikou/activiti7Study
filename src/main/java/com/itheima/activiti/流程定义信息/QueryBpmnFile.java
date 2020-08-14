package com.itheima.activiti.流程定义信息;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;

/**
 * <b>需求：</b>
 * 1、从Activiti的act_ge_bytearray表中读取两个资源文件
 * 2、将两个资源文件保存到用户本地路径：
 *
 * 技术方案：
 *      1、第一种方式：使用activiti的api来实现
 *      2、第二种方法：原理层面出发，可以使用jdbc对blob类型、clob类型数据的读取，并保存
 *      3、IO流转换，最好commons-io.jar包可以轻松解决io操作
 *
 * 真实场景：用户想查看这个请假流程具体由哪些步骤要走？
 */
public class QueryBpmnFile {
    public static void main(String[] args) throws IOException {
        // 1、创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 2、得到相关的service - RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 3、得到查询器：ProceeDefinitionQuery对象

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        // 4、设置查询条件
        List<ProcessDefinition> list = processDefinitionQuery.processDefinitionKey("holiday")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();

        // 5、执行查询操作，查询出想要的流程定义
        ProcessDefinition processDefinition = list.get(0);

        // 6、通过流程定义信息，得到部署ID
        String deploymentId = processDefinition.getDeploymentId();

        // 7、通过repositoryService的方法，实现读取图片信息及bpmn文件信息（输入流）
        // getResourceAsStream()方法的参数说明，第一个参数 部署id，第二个参数代办资源名称
        // processDefinition.getDiagramResourceName() 代表获取png图片资源的名称
        // processDefinition.getResourceName() 代表获取bpmn文件的名称
        InputStream pngIs = repositoryService.getResourceAsStream(deploymentId, processDefinition.getDiagramResourceName());

        InputStream bpmnIs = repositoryService.getResourceAsStream(deploymentId, processDefinition.getResourceName());

        // 8、构建出OutputStream流
        // 8.1 创建文件父级目录
        File file = new File("E:\\ActivitiResouces\\" + processDefinition.getDiagramResourceName());
        File fileParent = file.getParentFile();
        if (!fileParent.exists()){
            fileParent.mkdirs();
        }
//        file.createNewFile();
        OutputStream pngOs = new FileOutputStream("E:\\ActivitiResouces\\" + processDefinition.getDiagramResourceName());

        OutputStream bpmnOs = new FileOutputStream("E:\\ActivitiResouces\\" + processDefinition.getResourceName());

        // 9、输入流，输出流的转换 commons-io-xx.jar中的方法
        IOUtils.copy(pngIs,pngOs);
        IOUtils.copy(bpmnIs,bpmnOs);

        // 关闭流
        bpmnIs.close();
        bpmnOs.close();
        pngIs.close();
        pngOs.close();
    }
}
