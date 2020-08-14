# flowable理论(二) flowable简介

## 一、流程定义

Flowable---定义：使用Java编写的轻量级业务流程引擎

工作流引擎：指workflow作为应用系统的一部分，并为之提供对各应用系统由决定作用的根据角色、分工和条件的不同决定信息传递路由、内容等级等核心解决方案，

​	工作流引擎功能包括流程的节点管理，流向管理、流程样例管理......重要功能

> 开发历史
>
> jbmn3 -> jbmn-4 ->activiti5 -> flowable

flowable流程的运行主要需要包含两个文件： bpmn文件和流程图片

Flowable--功能：1、部署BPMN2.0流程定义

​							2、创建流程定义的流程实例

​							3、查询/访问运行种(或历史)的流程实例及相关数据

Flowable--优点： 1、可以将JAR形式发布的Flowable库加入应用或服务，来嵌入引擎。

​								2、可以使用Flowable REST API进行HTTP调用

​								3、提供了可使用简单方便的UI的Flowable应用



Flowable是activiti的fork

​	activiti是一个著名的工作流引擎(一个工作审批流)。Activiti项目是一项新的基于Apache许可的开源BPM平台(业务流程管理)，从基础开始构建，旨在提供支持新的BPMN2.0标准，包括支持对象管理组(OMG),可以定义流程、执行流程并以不同方式对其实现运行。

---



## 二、五大引擎及包含的服务

每个引擎由对应的EngineConfiguration进行创建，在创建过程钟对每个引擎使用的服务进行初始化。

> 如下，连接数据库构建流程引擎的配置信息,
>
> ​	创建processEngine配置对象cfg，使用buildProcessEngine()方法，创建processEngine对象。
>
> ```
> ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
>                 .setJdbcUrl("jdbc:mysql://localhost:3306/test?		                useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true")
>                 .setJdbcUsername("root")
>                 .setJdbcPassword("yuzaixia95..")
>                 .setJdbcDriver("com.mysql.jdbc.Driver")
>               .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
> 
>  // 2、用实例的配置创建 ProcessEngine对象
>   ProcessEngine processEngine = cfg.buildProcessEngine();
> ```



### 1、内容引擎 ContentEngine

包含服务

- **ContentService**

  > 实现对内容的创建、删除、保存和获取的基本操作。

- **ContenManagementService**

  > 提供对数据库表的管理操作，包括：

  1. `Map<String, Long> getTableCount();`获取每个表的记录数量
  2. `String getTableName(Class<?> flowableEntityClass);` 根据实体类获得对应的数据库表名
  3. `TableMetaData getTableMetaData(String tableName);` 根据数据库表名获得表的列名和列类型； 
  4. `TablePageQuery createTablePageQuery(); `创建一个可以进行排序、根据条件分页的查询类。



### 2、身份识别引擎 IdmEngine

详情可见文档Flowable-IDM详述.md

包含服务：

- **IdmIdentityService**

  > 提供用户的创建、修改、删除、密码修改、登录、用户头像设置等；

  > 提供Group的创建、删除、用户与组关系的关联、删除关联；

  > 提供权限的创建、删除、关联等。

- **IdmManagementService**

  > 对身份识别相关的数据库进行统计、获取表的列信息。

- **IdmEngineConfiguration**

  > 提供数据库配置信息

  

### 3、表单引擎 FormEngine

- **FormManagementService**

  > 提供对数据库的管理操作

- **FormRepositoryService**

  > 表单资源服务

- **FormService**

  > 提供表单实例的增删改查操作服务



### 4、决策引擎 DmnEngine 

- **DmnManagementService** 

  > 该类主要用于获取一系列的数据表元数据信息。 

- **DmnRepositoryService**

	> 动态部署流程资源。
	
- **DmnRuleService** 

  > 按照规则启动流程实例。 

- **DmnHistoryService** 

  > 提供对决策执行历史的访问的服务。 
  
  

### 5、流程引擎 ProcessEngine✨ 

流程因API是与Flowable交互的最常用手段。总口入点是ProcessEngine。可使用多种方式创建

> ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
>
> >ProcessEngines会扫描flowable.cfg.xml与flowable-context.xml文件。对于flowavle.cfg.xml文件，流程引擎会以标准Flowable方式构建引擎；
> >
> >`ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(inputStream).buildProcessEngine()`
>
> >对于flowable-context.xml文件，流程引擎会以Spring的方式构建：首先构建Spring应用上下文，然后从该上下文中获取流程引擎。



- **RepositoryService** 

  > **activiti仓库服务类** 。多数情况下接触Flowable的第一个服务，提供了管理与控制部署(deloyments)与流程定义(process definitions)的操作。
  >
  > RespositoryService repositoryService = processEngine.getRepositoryService();
  >
  > - 查询引擎现有的部署与流程定义。
  > - 暂停或激活部署中的某些流程，或整个部署。暂停意味着不能再对它进行操作，激活刚好相反，重新使它可以操作。
  > - 获取各种资源，比如部署中保存的文件，或者引擎自动生成的流程图。
  > - 获取POJO版本的流程定义。它可以用Java而不是XML方式查看流程。
  
- **RuntimeService** 
  
  > **activiti流程执行服务类**，用于启动流程定义的新流程实例，并为实例配置好启动参数。同一时刻，一个流程定义通常有多个运行中的实例。
  >
  > - ```java
  >   RuntimeService runtimeService = processEngine.getRuntimeService();
  >   // 启动流程
  >   ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday");
  >   
  >   
  >   System.out.println("流程部署ID " + processInstance.getDeploymentId());
  >           System.out.println("流程定义ID " + processInstance.getProcessDefinitionId());
  >           System.out.println("流程实例ID " + processInstance.getId());
  >   
  >           System.out.println("活动ID具体的哪个节点 " + processInstance.getActivityId());
  >   ```
  
- **TaskService**
  
  > **activiti的任务管理类**，可以使用此服务创建任务、声明并完成任务，分配任务等
  >
  > - 查询分派给用户或组的任务(所有待审单)
  >
  > - 创建独立运行(standalone)任务。这是一个没有关联到流程实例的任务。
  >
  > - 决定任务的执行用户(assignee)，或者将用户通过某种方式与任务关联。
  >
  > - 认领(claim)与完成(complete)任务。任务是指某人决定成为任务的执行用户，也即它将会完成任务。
  >
  > - ```java
  >   TaskService taskService = processEngine.getTaskService();
  >   
  >   List<Task> tasksList = taskService.createTaskQuery()
  >           .processDefinitionKey("holiday")
  >           .taskAssignee("zhangsan")
  >           .list();
  >   
  >   System.out.println("你当前代办的工作有：");
  >   int i = 0;
  >   for (Task task : tasksList){
  >       System.out.println("序号：" + (++i) +"  任务ID：" + task.getId() + "   任务名称：" + task.getName() + "   任务负责人：" + task.getAssignee());
  >   }
  >   
  >   System.out.println("请选择你需要处理的任务：");
  >   
  >   Scanner scanner = new Scanner(System.in);
  >   
  >   int taskIndex = Integer.valueOf(scanner.nextLine());
  >   
  >   Task task = tasksList.get(taskIndex - 1);
  >   
  >   taskService.complete(task.getId());
  >   // 关闭引擎
  >   processEngine.close();
  >   ```
  
- **HistoryService** 
  
  > 暴露Flowable引擎收集的所有历史数据，**activiti的历史管理类**。
  >
  > activiti查询历史信息类历史管理(执行完的数据的管理)  ，提供访问任务和表单相关的服务，提供正在进行或过去的流程实例的信息的服务。这与运行时信息有所不同，因为该运行时信息在任何给定时刻仅包含实际运行时状态，并且针对运行时过程执行性能进行了优化。历史记录信息经过优化，易于查询，并在永久性存储中永久保留。
  
- **IdentityService** 
  
  > 组织机构管理(创建，更新，删除，查询......)组与用户。Flowable实际上在运行时并不做任何用户检测。
  
- **FormService** 
> 一个可选服务，任务表单管理 

- **ManagerService**

 > **activiti的引擎管理类**，获取引擎所在的数据库中存在的表、获取表的元数据信息、创建删除等作业、执行命令类、执行自定义SQL、操作事件日志。
- **DynamicBpmnService** 
  
  > 动态修改Bpmn流程定义以及部署库等操作



Activiti服务架构图

activiti7  已经删除FromService与IdentityService，flowable6.5还在保留

![img](https://yun1.gree.com/microblog/filesvr/5f17ecda84ae79c17080725d?big)

这些service 就是提供了操作对应数据库的接口

工作流引擎的作用  简化  domain---dao---service  操作

---



## 三、一个设计器Modeler

> 一个不太漂亮的流程设计器，采用Angular.js开发，详情见<Flowable-Modeler详述.md>主要工作有:

- A.需要自己整合到项目中，主要工作整合到自己的spring boot工程，前端单页v例如vue/react还要自己搞定如何整合后端资源来调用。 
- B.完全汉化 
- C.流程图线条中文标注生成图时丢失。 
- D.去掉Spring security的安全登录认证

---



## 四、一套数据库

Flowable的所有数据库表都以`act_`或者`flw_`开头。第二部分时说明表用途的两字符标识符。服务API的命名也大略符合这个规则。

**Activiti引擎业务流程化自动控制，主要是控制25张表 *Service**

创建时详细的sql语句，可下载flowable发布的源文件夹中找到，位于database子文件夹。引擎JAR (flowable-engine-x.jar)的org/flowable/db/create包中也有一份(drop文件夹存放删除脚本)。SQL文件的格式为：

`flowable.{db}.{create|drop}.{type}.sql`

![img](https://yun1.gree.com/microblog/filesvr/5f27abca84ae79c1709111ab?big)



Flowable6.5自动创建会有一共78张表

- act_re_* :  re代表respository。带有这个前缀的表包含“静态”信息，例如流程定义域流程资源(图片、规则等)。
- act_ru_*:  ru代表runtime。这些表存储运行时信息，例如流程实例(process instance)、用户任务(user task)、变量(variable)、作业(job)等。Flowable只在流程实例运行中保存运行时数据，并在流程实例结束时删除记录。这样保证运行时表小和快。
- act_hi_*:  hi代表history。这些表存储历史数据，例如已完成的流程实例、变量、任务等。
- act_ge_*: 通用数据，在多处使用。

| 表分类       | 表名称                | 表含义                                |
| ------------ | --------------------- | ------------------------------------- |
|              | act_evt_log           | 事件处理日志表                        |
| 一般数据     | act_ge_bytearray      | 通用的流程定义和流程资源 bmpn配置文件 |
|              | act_ge_property       | 系统相关属性                          |
| 流程历史记录 | act_hi_actinst        | 历史的流程实例                        |
|              | act_hi_attachment     | 历史的流程附件                        |
|              | act_hi_comment        | 历史的说明性信息                      |
|              | act_hi_detail         | 历史的流程运行中的细节信息            |
|              | act_hi_identitylink   | 历史的流程运行过程中的用户关系        |
|              | act_hi_procinst       | 历史的流程实例                        |
|              | act_hi_taskinst       | 历史的任务实例                        |
|              | act_hi_varinst        | 历史的流程运行中的变量信息            |
| 用户组表     | act_id_group          | 身份信息-组信息                       |
|              | act_id_info           | 身份信息-组信息                       |
|              | act_id_membership     | 身份信息-用户和组关系的中间表         |
|              | act_id_user           | 身份信息-用户信息                     |
|              | act_procdef_info      | 死信任务                              |
| 流程定义表   | act_re_deployment     | 部署单元信息                          |
|              | act_re_model          | 模型信息                              |
|              | act_re_procdef        | 已部署的流程定义                      |
| 运行实例表   | act_ru_deadletter_job | 执行失败任务表                        |
|              | act_ru_event_subscr   | 运行时事件                            |
|              | act_ru_execution      | 运行时流程执行实例                    |
|              | act_ru_identitylink   | 运行时用户关系信息                    |
|              | act_ru_job            | 运行时作业                            |
|              | act_ru_suspended_job  | 运行时暂停任务                        |
|              | act_ru_task           | 运行任务                              |
|              | act_ru_timer_job      | 运行时定时任务                        |
|              | act_ru_variable       | 运行时变量表                          |

---

## 五、如何逐步掌握flowable的建议

- 0.flowable资料很少，建设先看activiti，API有90%相似之处。
- 1.在spring boot中配置flowable。 
- 2.下载它的发布版本，同时把它的5个war成功在tomcat上跑起来。 
- 3.了解一个流程模板文件bmpmn2.0中的内容元素是什么？同时学会用Modeler设计一个最简单的请假流程图，然后导出采用xml的发布方式，一开始不要去整合Modeler，难度太大，把生成的xml放到自己spring boot项目中的resource目录下创建一个process的子目录。 
- 4.学会如何通过xml部署一个流程（理解什么叫部署），启动创建一个流程实例，完成一个用户任务节点。 
- 5.了解用户节点配置人员有三种策略：assignee/候选用户/候选组的区别。 
- 6.再去深化：会签多实例的配置，排他网关和并行网关的应用，子流程应用等，熟悉的常用接口例如：待办任务，完成任务，转化任务，委派任务，挂起/激活流程实例，流程模板其它部署方式，驳回/退回的研究,当前节点给下一个节点选人的研究等，熟悉它的核心数据库表的每一个字段。 
- 7.升华：改造Modeler或者用bpmn.js当成的流程设计器，表单设计器，流程模型和流程实例管理，任务管理等工作。 
- 8.精通：开发出一系列自己需要的一些公用功能，满足常用功能需求。