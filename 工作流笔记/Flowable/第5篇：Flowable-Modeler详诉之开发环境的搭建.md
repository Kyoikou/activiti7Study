# 第5篇：Flowable-Modeler详诉之开发环境的搭建

## 环境要求

- JDK8以上
- 数据库mysql5.7以上
- 编译工具IDEA
- 项目构建工具maven

## 开始构建Maven项目

1. 定义坐标

   ![image-20200729135537356](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200729135537356.png)

>groupId一般是域名的反写，也作为项目中类的包名，
>
> artifactId是工程名，也就是根文件夹名.
>
>GroupID 是项目组织唯一的标识符，实际对应JAVA的包的结构，是main目录里java的目录结构。 
>
> ArtifactID是项目的唯一的标识符，实际对应项目的名称，就是项目根目录的名称。
>
>----------------1 基础知识必备---------------------------- 
>
>```xml
><groupId>com.yucong.commonmaven</groupId> 
>
><artifactId>commonmaven</artifactId> 
>
><version>0.0.1-SNAPSHOT</version> 
>
><packaging>jar</packaging> 
>
><name>common_maven</name>
>```
>
>
>
>groupId 定义了项目属于哪个组，举个例子，如果你的公司是mycom，有一个项目为myapp，那么groupId就应该是com.mycom.myapp. 
>
>artifacted 定义了当前maven项目在组中唯一的ID,比如，myapp-util,myapp-domain,myapp-web等。 version 指定了myapp项目的当前版本，SNAPSHOT意为快照，说明该项目还处于开发中，是不稳定的版本。 
>
>name 声明了一个对于用户更为友好的项目名称，不是必须的，推荐为每个pom声明name，以方便信息交流。



2.再创建一个子module工程

![image-20200729140019983](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200729140019983.png)



## 依赖引入与启动配置

### 父工程maven依赖

> pom中配置springboot的pom文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.gblty</groupId>
    <artifactId>flowablesstudy</artifactId>
    <packaging>pom</packaging>
    <version>1.0-SNAPSHOT</version>
    <modules>
        <module>flowablelech5</module>
    </modules>

    <!--springboot 父工程 表面这是一个springboot工程-->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.7.RELEASE</version>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <!--热部署-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>
        <!--springboot整合mvc-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- SpringBoot 拦截器 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>

        <!--单元测试-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!--mybatis整合-->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.2</version>
        </dependency>

        <!--mysql驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.13</version>
        </dependency>

        <dependency>
            <groupId>org.flowable</groupId>
            <artifactId>flowable-spring-boot-starter</artifactId>
            <version>6.4.1</version>
        </dependency>

        <!--常用工具类 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.4</version>
        </dependency>

        <!--集合常用工具类 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-collections4</artifactId>
            <version>4.2</version>
        </dependency>

        <!--io常用工具类 -->
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.5</version>
        </dependency>

        <!--文件上传工具类 -->
        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
            <version>1.3.3</version>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.21</version>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.7.21</version>
        </dependency>

        <!--fastjson解析-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.54</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### 子工程maven依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>flowablesstudy</artifactId>
        <groupId>com.gblty</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>flowablelech5</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.flowable</groupId>
            <artifactId>flowable-engine</artifactId>
            <version>6.5.0</version>
            <scope>compile</scope>
            <!--scope指定了依赖(第三方jar包)的作用范围   compile表示参与当前项目的编译、测试、运行、打包-->
        </dependency>
        <!--最关键的，将flowable整合进springboot  不是整合spring-->
        <dependency>
            <groupId>org.flowable</groupId>
            <artifactId>flowable-spring-boot-starter</artifactId>
            <version>6.5.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```



### 子工程启动类

```java
package com.gblfy;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//扫描Mybtis Mapper
@MapperScan(basePackages = {"com.gblfy.**"})
@SpringBootApplication
public class FlowableApplication {

    private static final Logger logger = LoggerFactory.getLogger(FlowableApplication.class);

    public static void main(String[] args){
        logger.info("开始启动服务 ");
        SpringApplication.run(FlowableApplication.class,args);
        logger.info("完成启动服务 ");
    }
}


//  @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
```



### 配置application.yml文件

```
server:
  port: 8989
  tomcat:
    # tomcat的URI编码
    uri-encoding: UTF-8
    # tomcat最大线程数，默认为200
    max-threads: 200
    # Tomcat启动初始化的线程数，默认值25
    min-spare-threads: 30

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.gblfy   # 注意：对应实体类的路径

# 数据源配置
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/flowable?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true
    username: root
    password: yuzaixia95..
```



> mysql的URL几个参数：
>
> useUnicode、characterEncoding不用说，必填
>
> serverTimezone=Asia/Shanghai      （mysql V6以上必填参数，北京事件=UTC+8）
>
> nullCatalogMeansCurrent=true        本机上建过相同的表，允许其他数据库再建一张名字一样，建议配置上
>
> useSSL=false			mysql在高版本需要指明是否进行SSL(协议)连接，默认false，不填为好，填了易报错

---



## 案例演练

### 新建接口

```java
package com.gblfy.service;

import org.flowable.engine.repository.Deployment;

public interface IFlowService {
    /**
     * 部署工作流
     */
    Deployment createFlow(String filePath);
}

```

> 实现逻辑处理类

```java
package com.gblfy.service.impl;

import com.gblfy.service.IFlowService;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Primary    // @Primary:自动装配时出现多个Bean候选者时，被注解为@Primary的Bean将作为首选者，否则将抛出异常
@Service   // 标注为业务层组件
public class FlowServiceImpl implements IFlowService {

    private static final Logger log = LoggerFactory.getLogger(FlowServiceImpl.class);
    /**
     * Flowable运行时服务
     */
    @Autowired
    private RepositoryService repositoryService;

    @Override
    public Deployment createFlow(String filePath) {
        //解析BPMN模型看是否成功
        XMLStreamReader reader = null;
        InputStream inputStream = null;
        try {
            BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
            XMLInputFactory factory = XMLInputFactory.newInstance();
            inputStream = new FileInputStream(new File(filePath));
            reader = factory.createXMLStreamReader(inputStream);
            BpmnModel model = bpmnXMLConverter.convertToBpmnModel(reader);
            List<Process> processes = model.getProcesses();
            Process curProcess = null;
            if (CollectionUtils.isEmpty(processes)) {
                log.error("BPMN模型没有配置流程");
                return null;
            }

            curProcess = processes.get(0);

            inputStream = new FileInputStream(new File(filePath));
            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment().name("TEST_FLOW")
                    .addInputStream(curProcess.getName(), inputStream);

            Deployment deployment = deploymentBuilder.deploy();

            log.warn("部署流程 name:" + curProcess.getName() + " " + deployment);
            return deployment;
        } catch (Exception e) {
            log.error("BPMN模型创建流程异常", e);
            return null;
        } finally {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                log.error("关闭异常", e);
            }
        }
    }
}

```

### 添加一个controller用来测试流程是否启动成功

```java
package com.gblfy.controller;

import com.gblfy.service.IFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "flow")
public class FlowController {
    private static final Logger log = LoggerFactory.getLogger(FlowController.class);

    /**
     * 流程处理服务
     */
    @Autowired
    private IFlowService flowService;

    @RequestMapping("/create")
    @ResponseBody
    public Map<String, String> creatFlow(){
        Map<String, String> result = new HashMap<>();

        String flowPath = "F:\\IDEATIME\\flowablesstudy\\flowablelech5\\src\\main\\resources\\processes\\测试BPMN模型.bpmn20.xml";

        if (null == flowService.createFlow(flowPath)){
            result.put("message","创建流程失败");
            result.put("result","0");
            return result;
        }

        result.put("message","创建流程成功");
        result.put("result","1");
        return result;
    }
}

```

### 创建mysql数据库启动服务

![image-20200729190037684](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200729190037684.png)

### 查看测试结果

![image-20200729190136238](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200729190136238.png)

![image-20200729190115144](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200729190115144.png)

### 查看数据库中的表

- 部署ID表:act_re_deployment

![image-20200729190228375](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200729190228375.png)

- 部署内容表: act_ge_bytearray

![image-20200729190339565](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200729190339565.png)

### 项目结构

![image-20200729190438103](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200729190438103.png)