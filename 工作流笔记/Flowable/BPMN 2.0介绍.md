# BPMN 2.0介绍

## 是什么

BPMN是一个广泛接受与支持的，展现流程的注记方法。详见[OMG BPMN标准](http://www.bpmn.org/)。

## 定义流程

流程文件是xml文件，确保文件名以**.bpmb20.xml或.bpmn**结尾，否则引擎不会在部署时使用这个文件

BPMN 2.0的概要（schema）的根元素（root element）是**definitions**元素。在这个元素中，可以定义多个流程定义（然而我们建议在每个文件中，只有一个流程定义。这样可以简化之后的部署过程）。下面给出的是一个空流程定义。请注意definitions元素最少需要包含xmlns与targetNameSpace声明。targetNameSpace可以为空，它用于对流程定义进行分类。

```xml
<definitions
  xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL";
  xmlns:flowable="http://flowable.org/bpmn";
  targetNamespace="Examples">

  <process id="myProcess" name="My First Process">
    ..
  </process>

</definitions>
```

**process**元素有两个属性：

- **id**:必填属性，将映射为Flowable ProcessDefinition对象的**key**参数。可以使用RuntimeService中的startProcessInstanceByKey方法，使用id来启动这个流程定义的新流程实例。这个方法总会使用流程定义的**最新部署版本**。

  `ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess");`

- 请注意这与调用startProcessInstanceById方法不同。startProcessInstanceById方法的参数为Flowable引擎在部署时生成的字符串ID(可以通过调用processDefinition.getId()方法获取)。生成ID的格式为key:version，长度限制为64字符。请注意限制流程key的长度，否则会抛出FlowableException异常，提示生成的ID过长。 
- **name**: 可选属性，将映射为ProcessDefinition的name参数。引擎本身不会使用这个参数。可以用于在用户界面上显示更友好的名字。

---

## 流程执行过程

### 部署*(deploy)*流程定义。

部署流程定义意味着两件事： 

- 流程定义将会存储在Flowable引擎配置的持久化数据库中。因此部署业务流程保证了引擎在重启后也能找到流程定义。 
- BPMN 2.0流程XML会解析为内存中的对象模型，供Flowable API使用。

部署有许多种方式，与Flowable流程引擎交互都要通过他的服务*(services)*进行。

```java
Deployment deployment = repositoryService.createDeployment()
  .addClasspathResource("FinancialReportProcess.bpmn20.xml")
  .deploy();
```

### 启动流程实例*(instance)*

`ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("financialReport");`

### 执行流程

创建流程实例后，首先通过开始事件，就是那个空心圆圈，会沿着所有出口顺序流，继续执行。直到到达**等待状态*(wait site)***，例如用户任务。流程实例的当前状态会存储在数据库中并保持，直到用户决定完成任务。这时，引擎会继续执行，直到遇到新的等待状态，或者流程结束。如果在这期间引擎重启或崩溃，流程的状态也仍在数据库中安全的保存。

![img](https://yun1.gree.com/microblog/filesvr/5f2a157284ae79c170950cd7?big)

我们看到的是一个空启动事件（左边的圆圈），接下来是两个用户任务：'Write monthly financial report（撰写月度财务报告）'与'Verify monthly financial report（审核月度财务报告）'。最后是空结束事件（右边的粗线条圆圈）。

### 任务列表

现在可以通过如下代码获取这个任务： 

`List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("kermit").list();`

请注意传递给这个操作的用户需要是accountancy组的成员，因为在流程定义中是这么声明的：
```xml
<potentialOwner>
<resourceAssignmentExpression>					       		      <formalExpression>accountancy</formalExpression>
</resourceAssignmentExpression>
</potentialOwner>
```
也可以使用任务查询API，用组名查得相同结果。可以在代码中添加下列逻辑： 

```java
TaskService taskService = processEngine.getTaskService();

 List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
```

### 申领任务

会计师（accountancy组的成员）现在需要申领任务***（claim）***。申领任务后，这个用户会成为任务的执行人***（assignee）***，这个任务也会从accountancy组的其他成员的任务列表中消失。可以通过如下代码实现申领任务： 

`taskService.claim(task.getId(), "fozzie");`

 这个任务现在在申领任务者的个人任务列表中。 

`List<Task> tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();`

### 完成任务

会计师（accountancy组的成员）现在需要开始撰写财务报告了。完成报告后，他就可以完成任务***（complete）**，代表任务的所有工作都已完成。 

`taskService.complete(task.getId()); `

对于Flowable引擎来说，这是个外部信号，指示流程实例可以继续执行。**Flowable会从运行时数据中移除任务**，并沿着这个任务唯一的出口转移线（outgoing transition），将执行移至第二个任务*（'verification of the report 审核报告'）*。为第二个任务分配执行人的机制，与上面介绍的第一个任务使用的机制相同。唯一的区别是这个任务会分配给management组。 在演示设置中，完成任务可以通过点击任务列表中的complete按钮。因为Fozzie不是经理，我们需要登出Flowable Task应用，并用kermit（他是经理）登录。这样就可以在未分配任务列表中看到第二个任务。

### 结束流程

可以使用与之前完全相同的方式获取并申领审核任务。完成这个第二个任务会将流程执行移至结束事件，并结束流程实例。这个流程实例，及所有相关的运行时执行数据都会从数据库中移除。 

也可以通过编程方式，使用historyService验证流程已经结束

```java
HistoryService historyService = processEngine.getHistoryService();
HistoricProcessInstance historicProcessInstance =
historyService.createHistoricProcessInstanceQuery().processInstanceId(procId).singleResult();
System.out.println("Process instance end time: " + historicProcessInstance.getEndTime());
```

### 代码总结

```java
public class TenMinuteTutorial {

  public static void main(String[] args) {

    // 创建Flowable流程引擎
    ProcessEngine processEngine = ProcessEngineConfiguration
      .createStandaloneProcessEngineConfiguration()
      .buildProcessEngine();

    // 获取Flowable服务
    RepositoryService repositoryService = processEngine.getRepositoryService();
    RuntimeService runtimeService = processEngine.getRuntimeService();

    // 部署流程定义
    repositoryService.createDeployment()
      .addClasspathResource("FinancialReportProcess.bpmn20.xml")
      .deploy();

    // 启动流程实例
    String procId = runtimeService.startProcessInstanceByKey("financialReport").getId();

    // 获取第一个任务
    TaskService taskService = processEngine.getTaskService();
    List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
    for (Task task : tasks) {
      System.out.println("Following task is available for accountancy group: " + task.getName());

      // 申领任务
      taskService.claim(task.getId(), "fozzie");
    }

    // 验证Fozzie获取了任务
    tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
    for (Task task : tasks) {
      System.out.println("Task for fozzie: " + task.getName());

      // 完成任务
      taskService.complete(task.getId());
    }

    System.out.println("Number of tasks for fozzie: "
            + taskService.createTaskQuery().taskAssignee("fozzie").count());
      // 获取并申领第二个任务
    tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
    for (Task task : tasks) {
      System.out.println("Following task is available for management group: " + task.getName());
      taskService.claim(task.getId(), "kermit");
    }

    // 完成第二个任务并结束流程
    for (Task task : tasks) {
      taskService.complete(task.getId());
    }

    // 验证流程已经结束
    HistoryService historyService = processEngine.getHistoryService();
    HistoricProcessInstance historicProcessInstance =
      historyService.createHistoricProcessInstanceQuery().processInstanceId(procId).singleResult();
    System.out.println("Process instance end time: " + historicProcessInstance.getEndTime());
  }

}
```

### 后续增强

可以看出这个业务流程太简单了，不能实际使用。但只要继续学习Flowable中可用的BPMN 2.0结构，就可以通过以下元素增强业务流程： 

- 定义**网关*（gateway）***使经理可以选择：驳回财务报告，并重新为会计师创建任务；或者接受报告。 
- 定义并使用**变量*（variables）***存储或引用报告，并可以在表单中显示它。 在流程结束处定义服务任务（service task），将报告发送给每一个投资人。 
- 等等。