## BPMN 2.0结构

## 自定义扩展

BPMN 2.0标准对流程的所有的参与者都很有用。最终用户不会因为依赖专有解决方案，而被供应商“绑架”。Flowable之类的开源框架，也可以提供与大型供应商的解决方案相同（经常是更好;-）的实现。有了BPMN 2.0标准，从大型供应商解决方案向Flowable的迁移，可以十分简单平滑。 

缺点则是标准通常是不同公司（不同观点）大量讨论与妥协的结果。作为阅读BPMN 2.0 XML流程定义的开发者，有时会觉得某些结构或方法十分笨重。Flowable将开发者的感受放在最高优先，因此引入了一些***Flowable BPMN扩展（extensions）***。这些“扩展”并不在BPMN 2.0规格中，有些是新结构，有些是对特定结构的简化。 

尽管BPMN 2.0规格明确指出可以支持自定义扩展，我们仍做了如下保证：

- 自定义扩展**保证**是在**标准方式**的基础上进行简化。因此当你决定使用自定义扩展时，不用担心无路可退（仍然可以用标准方式）。 
- 使用自定义扩展时，总是通过**lowable:**命名空间前缀，明确标识出XML元素、属性等。请注意Flowable引擎也支持**activiti:**命名空间前缀。 

因此是否使用自定义扩展，完全取决于你自己。有些其他因素会影响选择（图形化编辑器的使用，公司策略，等等）。我们提供扩展，只是因为相信标准中的某些地方可以用更简单或效率更高的方式处理。请不要吝啬给我们反馈你对扩展的评价（正面的或负面的），也可以给我们提供关于自定义扩展的新想法。说不定某一天，你的想法会成为标准的一部分！

---

## 事件 *（Event）*

### 定时器事件*(timerEventDefinition)*

定时器事件（timer event definition），是由定时器所触发的事件。可以用于开始事件，中间事件，或边界事件。定时器事件的行为取决于所使用的业务日历（business calendar）。定时器事件有默认的业务日历，但也可以为每个定时器事件定义，单独定义业务日历。

```xml
<timerEventDefinition flowable:businessCalendarName="custom">
    ...
</timerEventDefinition>
```

其中**businessCalendarName**指向流程引擎配置中的业务日历。如果省略业务日历定义，则使用默认业务日历。 定时器定义必须且只能包含下列的一种元素：

- **timeDate**。这个元素特点了ISO 8601格式的固定时间。在这个时间就好触发触发器。例如：

  ```xml
  <timerEventDefinition>
      <timeDate>2011-03-11T12:13:14</timeDate>
  </timerEventDefinition>
  ```

- **timeDuration**。要定义定时器需要等待多长时间再触发，可以用*timerEventDefinition*的子元素*timeDuration*。使用ISO 8601格式（BPMN 2.0规范要求）。例如（等待10天）：

  ```xml
  <timerEventDefinition>
      <timeDuration>P10D</timeDuration>
  </timerEventDefinition>
  ```

  

- **timeCycle**。指定重复周期，可用于周期性启动流程，或者为超期用户任务多次发送提醒。这个元素可以使用两种格式。第一种是按照ISO 8601标准定义的循环时间周期。例如（三次重复间隔，每次间隔为10小时）：

  - 也可以使用timeCycle的可选属性***endDate***，或者像这样直接写在时间表达式的结尾：*R3/PT10H/${EndDate}*。 当到达*endDate*时，应用会停止，并为该任务创建其他作业。 可以使用ISO 8601标准的常量，如"2015-02-25T16:42:11+00:00"。也可以使用变量，如${EndDate}

  ```xml
  <timerEventDefinition>
      <timeCycle flowable:endDate="2015-02-25T16:42:11+00:00">R3/PT10H</timeCycle>
  </timerEventDefinition>
  ```

  ```xml
  <timerEventDefinition>
      <timeCycle>R3/PT10H/${EndDate}</timeCycle>
  </timerEventDefinition>
  ```

  如果同时使用了两种格式，则系统会使用以属性方式定义的endDate。 目前只有*BoundaryTimerEvents*与*CatchTimerEvent*可以使用EndDate。 另外，也可以使用cron表达式指定定时周期。下面的例子展示了一个整点启动，每5分钟触发的触发器：

  `0 0/5 * * * ?`

边界事件例子

```xml
<boundaryEvent id="escalationTimer" cancelActivity="true" attachedToRef="firstLineSupport">
  <timerEventDefinition>
    <timeDuration>${duration}</timeDuration>
  </timerEventDefinition>
</boundaryEvent>
```

> 请注意：定时器只有在异步执行器启用时才能触发（需要在flowable.cfg.xml中，将asyncExecutorActivate设置为true。因为默认情况下异步执行器都是禁用的）。

### 错误事件定义*(errorEventDefinition)*

> 重要提示： BPMN错误与Java异常不是一回事。事实上，这两者毫无共同点。BPMN错误事件是建模业务异常（business exceptions）的方式。而Java异常会按它们自己的方式处理。

```xml
<endEvent id="myErrorEndEvent">
  <errorEventDefinition errorRef="myError" />
</endEvent>
```

### 信号事件定义*signal*

信号事件（signal event），是引用具名信号的事件。信号是全局范围（广播）的事件，并会被传递给所有激活的处理器（等待中的流程实例/捕获信号事件 catching signal events）。 

使用signalEventDefinition元素声明信号事件定义。其signalRef属性引用一个signal元素，该signal元素需要声明为definitions根元素的子元素。下面摘录一个流程，使用中间事件（intermediate event）抛出与捕获信号事件。

```xml
<definitions... >
    <!-- 声明信号 -->
    <signal id="alertSignal" name="alert" />

    <process id="catchSignal">
        <intermediateThrowEvent id="throwSignalEvent" name="Alert">
            <!-- 信号事件定义 -->
            <signalEventDefinition signalRef="alertSignal" />
        </intermediateThrowEvent>
        ...
        <intermediateCatchEvent id="catchSignalEvent" name="On Alert">
            <!-- 信号事件定义 -->
            <signalEventDefinition signalRef="alertSignal" />
        </intermediateCatchEvent>
        ...
    </process>
</definitions>
```

- **抛出信号事件**

  信号可以由流程实例使用BPMN结构抛出（throw），也可以通过编程方式使用Java API抛出。下面org.flowable.engine.RuntimeService中的方法可以用编程方式抛出信号：

```java
RuntimeService.signalEventReceived(String signalName);
RuntimeService.signalEventReceived(String signalName, String executionId);
```

signalEventReceived(String signalName)与signalEventReceived(String signalName, String executionId)的区别，是前者在全局范围为所有已订阅处理器抛出信号（广播），而后者只为指定的执行传递信号。

- **捕获信号事件**

  可以使用信号捕获中间事件（intermediate catch signal event）或者信号边界事件（signal boundary event）捕获信号事件。

```xml
<!-- 捕获事件 -->
<intermediateThrowEvent id="throwSignalEvent" name="Alert">
            <signalEventDefinition signalRef="alertSignal" />
 </intermediateThrowEvent>
```

- **信号事件的范围**

  默认情况下，信号事件在流程引擎全局广播。这意味着你可以在一个流程实例中抛出一个信号事件，而不同流程定义的不同流程实例都会响应这个事件。 但有时也会希望只在同一个流程实例中响应信号事件。例如，在流程实例中使用异步机制，而两个或多个活动彼此互斥的时候。 要限制信号事件的范围（scope），在信号事件定义中添加（非BPMN 2.0标准！）scope属性：

```xml
<signal id="alertSignal" name="alert" flowable:scope="processInstance">
```

​	这个属性的默认值为“global*（全局）*"。

- **信号事件示例**

下面是一个不同流程通过信号通信的例子。第一个流程在保险政策更新或变更时启动。在变更由人工审核之后，会抛出信号事件，指出政策已经发生了变更：

![img](https://yun1.gree.com/microblog/filesvr/5f2a5c8084ae79c1709597c4?big)

这个事件可以被所有感兴趣的流程实例捕获。下面是一个订阅这个事件的流程的例子。

![img](https://yun1.gree.com/microblog/filesvr/5f2a5cd884ae79c1709598e2?big)

### 消息事件定义*message*

消息事件（message event），是指引用具名消息的事件。消息具有名字与载荷。**与信号不同，消息事件只有一个接收者**。 

消息事件定义使用messageEventDefinition元素声明。其messageRef属性引用一个message元素，该message元素需要声明为definitions根元素的子元素。下面摘录一个流程，声明了两个消息事件，并由开始事件与消息捕获中间事件（intermediate catching message event）引用。

```xml
<definitions id="definitions"
  xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL";
  xmlns:flowable="http://flowable.org/bpmn";
  targetNamespace="Examples"
  xmlns:tns="Examples">

  <message id="newInvoice" name="newInvoiceMessage" />
  <message id="payment" name="paymentMessage" />

  <process id="invoiceProcess">

    <startEvent id="messageStart" >
    	<messageEventDefinition messageRef="newInvoice" />
    </startEvent>
    ...
    <intermediateCatchEvent id="paymentEvt" >
    	<messageEventDefinition messageRef="payment" />
    </intermediateCatchEvent>
    ...
  </process>

</definitions>
```

- **抛出消息事件**

  作为嵌入式的流程引擎，Flowable并不关心实际如何接收消息。因为这可能与环境相关，或需要进行平台定义的操作。例如连接至JMS（Java Messaging Service，Java消息服务）队列（Queue）/主题（Topic），或者处理Webservice或者REST请求。因此接收消息需要作为应用的一部分，或者是流程引擎所嵌入的基础框架中的一部分，由你自行实现。 在应用中接收到消息后，需要决定如何处理它。

  如果这个消息需要启动新的流程实例，可以选择一种由runtime服务提供的方法：

```java
ProcessInstance startProcessInstanceByMessage(String messageName);
ProcessInstance startProcessInstanceByMessage(String messageName, Map<String, Object> processVariables);
ProcessInstance startProcessInstanceByMessage(String messageName, String businessKey,
    Map<String, Object> processVariables);
```

这些方法使用消息启动流程实例。

如果需要由已有的流程实例接收消息，需要首先将消息与特定的流程实例关联（查看后续章节），然后触发等待中的执行，让流程继续进行。runtime服务提供了下列方法，可以触发订阅了消息事件的执行： 

```java
 void messageEventReceived(String messageName, String executionId); 
 void messageEventReceived(String messageName, String executionId, HashMap<String, Object> processVariables);
```

- **查询消息事件订阅**

  对于消息启动事件（message start event），消息事件的订阅与的流程定义相关。可以使用ProcessDefinitionQuery查询这种类型的消息订阅：

```java
ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()      .messageEventSubscription("newCallCenterBooking") 
   .singleResult();
```

​			因为一个消息只能被一个流程定义订阅，因此这个查询总是返回0或1个结果。如果流程定义更新了，只有该流程定义的最新版本会订阅这个消息事件。 

​			对于消息捕获中间事件（intermediate catch message event），消息事件的订阅与执行相关。可以使用ExecutionQuery查询这种类型的消息订阅：

```java
Execution execution = runtimeService.createExecutionQuery()          	    .messageEventSubscriptionName("paymentReceived")
.variableValueEquals("orderId", message.getOrderId())
.singleResult();
```

这种查询通常都会有关联查询，并且通常需要了解流程的情况（在这个例子里，对于给定的orderId，至多只有一个流程实例）。

- **消息事件示例**

  下面是一个流程的例子，可以使用两种不同的消息启动：

  ![img](https://yun1.gree.com/microblog/filesvr/5f2a626984ae79c17095bb42?big)

  在流程需要通过不同的方式启动，但是后续使用统一的方式处理时，就可以使用这种方法。

---



### 启动事件*startEvent*

启动事件（start event）是流程的起点。启动事件的类型（流程在消息到达时启动，在指定的时间间隔后启动，等等），定义了流程如何启动，并显示为启动事件中的小图标。在XML中，类型由子元素声明来定义。 

启动事件**随时捕获**：启动事件（保持）等候，直到特定的触发器被触发。 

在启动事件中，可以使用下列Flowable自定义参数：

- **initiator**:指明保存认证用户（authenticated user）ID用的变量名。在流程启动时，操作用户的ID会保存在这个变量中。例如：

  ```xml
  <startEvent id="request" flowable:initiator="initiator" />
  ```

认证用户必须在try-finally块中调用IdentityService.setAuthenticatedUserId(String)方法进行设置。像这样：

```java
try {
  identityService.setAuthenticatedUserId("bono");
  runtimeService.startProcessInstanceByKey("someProcessKey");
} finally {
  identityService.setAuthenticatedUserId(null);
}
```

这段代码已经集成在Flowable应用中，可以在表单中使用。

#### 空启动事件

- 描述

  ”空“启动事件(none Start Event)，指的是未指定启动流程触发器的启动事件。引擎将无法预知启动流程实例。空启动事件用于流程实例通过调用下列*startProcessInstanceByXXX API*方法启动的情况。

  ```java
  ProcessInsatance processInstance = runtimeService.startProcessInstanceByXXX();
  ```

  > *请注意*：子流程（sub-process）必须有空启动事件。

- 图示

  空启动事件用空心圆表示，中间没有图标（也就是说，没有触发器）。

  ![image-20200805160057526](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200805160057526.png)

- XML表示

  空启动事件的XML表示格式，就是普通的启动事件声明，而没有任何子元素（其他种类的启动事件都有用于什声明其类型的子元素）。

  ```xml
  <startEvent id="start" name="my start event" />
  ```

- 空启动事件的自定义扩展

  **formKey**:用表单定义，用户需要在启动新流程实例时填写该表单。可以在表单章节找到更多信息。例如

  ```xml
  <startEvent id="request" flowable:formKey="request" />
  ```

#### 定时器启动事件

- 描述

  定时器启动事件(timer start event)在指定时间创建流程实例。在流程只需要启动一次，或者流程需要在特定的事件间隔重复启动时，都可以使用。

  请注意：子流程不能有定时器启动事件。 请注意：定时器启动事件，在流程部署的同时就开始计时。不需要调用startProcessInstanceByXXX就会在时间启动。调用startProcessInstanceByXXX时会在定时启动之外额外启动一个流程。 请注意：当部署带有定时器启动事件的流程的更新版本时，上一版本的定时器作业会被移除。这是因为通常并不希望旧版本的流程仍然自动启动新的流程实例。

- 图示

  定时器启动事件，用其中一个钟表图标的圆圈来表示。

![image-20200805162108816](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200805162108816.png)

- XML表示

  定时器启动事件的XML表示格式，是普通的启动事件声明加上定时器定义子元素。请参考定时器定义了解详细配置方法。 示例：流程会启动4次，间隔5分钟，从2011年3月11日，12:13开始

  ```xml
  <startEvent id="theStart">
      <timerEventDefinition>
          <timeCycle>R4/2011-03-11T12:13/PT5M</timeCycle>
      </timerEventDefinition>
  </startEvent>
  ```

  示例：流程会在设定的时间启动一次

  ```xml
  <startEvent id="theStart">
      <timerEventDefinition>
          <timeDate>2011-03-11T12:13:14</timeDate>
      </timerEventDefinition>
  </startEvent>
  ```

#### 消息启动事件

- 描述

  消息启动事件（message start event）使用具名消息启动流程实例。消息名用于选择正确的启动事件。

  当**部署**具有一个或多个消息启动事件的流程定义时，会做如下判断：

  - 给定流程定义中，消息启动事件的名字必须是唯一的。一个流程定义不得包含多个同名的消息启动事件。如果流程定义中有两个或多个消息启动事件引用同一个消息，或者两个或多个消息启动事件引用了具有相同消息名字的消息，则Flowable会在部署这个流程定义时抛出异常。 
  - 在所有已部署的流程定义中，消息启动事件的名字必须是唯一的。如果在流程定义中，一个或多个消息启动事件引用了已经部署的另一流程定义中消息启动事件的消息名，则Flowable会在部署这个流程定义时抛出异常。 
  - 流程版本：在部署流程定义的新版本时，会取消上一版本的消息订阅，即使新版本中并没有这个消息事件）。 在启动流程实例时，可以使用下列RuntimeService中的方法，触发消息启动事件：

在**启动**流程实例时，可以使用下列RuntimeService中的方法，触发消息启动事件：

```java
ProcessInstance startProcessInstanceByMessage(String messageName);
ProcessInstance startProcessInstanceByMessage(String messageName, Map<String, Object> processVariables);
ProcessInstance startProcessInstanceByMessage(String messageName, String businessKey,
    Map<String, Object< processVariables);
```

messageName是由message元素的name属性给定的名字。messageEventDefinition的messageRef属性会引用message元素。当**启动**流程实例时，会做如下判断：

> - 只有顶层流程（top-level process）才支持消息启动事件。嵌入式子流程不支持消息启动事件。 
> - 如果一个流程定义中有多个消息启动事件，可以使用runtimeService.startProcessInstanceByMessage(…)选择合适的启动事件。 
> - 如果一个流程定义中有多个消息启动事件与一个空启动事件，则runtimeService.startProcessInstanceByKey(…)与runtimeService.startProcessInstanceById(…)会使用空启动事件启动流程实例。 
> - 如果一个流程定义中有多个消息启动事件而没有空启动事件，则runtimeService.startProcessInstanceByKey(…)与runtimeService.startProcessInstanceById(…)会抛出异常。 
> - 如果一个流程定义中只有一个消息启动事件，则runtimeService.startProcessInstanceByKey(…)与runtimeService.startProcessInstanceById(…)会使用这个消息启动事件启动新流程实例。 
> - 如果流程由调用活动（call activity）启动，则只有在下列情况下才支持消息启动事件 
>   - 除了消息启动事件之外，流程还有唯一的空启动事件 
>   - 或者流程只有唯一的消息启动事件，而没有其他启动事件。

- 图示

  消息启动事件用其中有一个消息事件标志的圆圈表示。这个标志并未填充，用以表示捕获（接受）行为。

  ![image-20200805165309652](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200805165309652.png)

- xml表示

  消息启动事件的XML表示格式，为普通启动事件声明加上messageEventDefinition子元素：

  ```xml
  <definitions id="definitions"
    xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL";
    xmlns:flowable="http://flowable.org/bpmn";
    targetNamespace="Examples"
    xmlns:tns="Examples">
  
    <message id="newInvoice" name="newInvoiceMessage" />
  
    <process id="invoiceProcess">
  
      <startEvent id="messageStart" >
      	<messageEventDefinition messageRef="tns:newInvoice" />
      </startEvent>
      ...
    </process>
  
  </definitions>
  ```

#### 信号启动事件

- 描述

  信号启动事件（signal start event），使用具名信号启动流程实例。这个信号可以由流程实例中的信号抛出中间事件（intermediary signal throw event），或者API（runtimeService.signalEventReceivedXXX方法）触发。两种方式都会启动所有拥有相同名字信号启动事件的流程定义。

  请注意可以选择异步还是同步启动流程实例。 

  需要为API传递的signalName，是由signal元素的name属性决定的名字。signal元素由signalEventDefinition的signalRef属性引用。

- 图示

  信号启动事件用其中一个信号事件标志的圆圈表示。这个标志并未填充。这个标志并未填充，用以表示捕获（接收）行为。

  ![image-20200805170101200](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200805170101200.png)

- XML表示

  信号启动事件的XML表示格式，为普通启动事件声明，加上signalEventDefinition子元素：

  ```xml
  <signal id="theSignal" name="The Signal" />
  
  <process id="processWithSignalStart1">
    <startEvent id="theStart">
      <signalEventDefinition id="theSignalEventDefinition" signalRef="theSignal"  />
    </startEvent>
    <sequenceFlow id="flow1" sourceRef="theStart" targetRef="theTask" />
    <userTask id="theTask" name="Task in process A" />
    <sequenceFlow id="flow2" sourceRef="theTask" targetRef="theEnd" />
    <endEvent id="theEnd" />
  </process>
  ```

#### 错误（异常）启动事件

- 描述

  错误启动事件（error start event），可用于触发事件子流程（Event Sub-Process）。错误启动事件不能用于启动流程实例。 

  错误启动事件总是中断。

- 图示

  错误启动事件用其中有一个错误事件标志的圆圈表示。这个标志并未填充，用以表示捕获（接收）行为。

  ![image-20200805171013461](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200805171013461.png)

- XML表示

  错误启动事件的XML表示格式，为普通启动事件声明加上errorEventDefinition子元素：

  ```xml
  <startEvent id="messageStart" >
  	<errorEventDefinition errorRef="someError" />
  </startEvent>
  ```

  ---

  

### 结束事件 endEvent

结束事件（end event）标志着流程或子流程中一个分支的结束。结束事件**总是抛出（型）事件**。这意味着当流程执行到达结束事件时，会抛出一个*结果*。结果的类型由事件内部的黑色图标表示。在XML表示中，类型由子元素声明给出。

#### 空结束事件

- 描述

  结束事件（end event）标志着流程或子流程中一个分支的结束。结束事件总是抛出（型）事件。这意味着当流程执行到达结束事件时，会抛出一个结果。结果的类型由事件内部的黑色图标表示。在XML表示中，类型由子元素声明给出。

- 图示

  空结束事件，用其中没有图标（没有结果类型）的粗圆圈表示。

  ![image-20200805172051623](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200805172051623.png)

- XML表示

  空事件的XML表示格式为普通结束事件声明，没有任何子元素（其它种类的结束事件都有子元素，用于声明其类型）。

  ```xml
  <endEvent id="end" name="my end event" />
  ```

#### 错误结束事件

- 描述

  当流程执行到达**错误结束事件（error end event）**时，结束执行的当前分支，并抛出错误。这个错误可以由**匹配的错误边界中间事件捕获**。如果找不到匹配的错误边界事件，将会抛出异常。

- 图示

  ![image-20200805172750374](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200805172750374.png)

- XML表示

  错误结束事件表示为结束事件，加上errorEventDefinition子元素：

  ```xml
  <endEvent id="myErrorEndEvent">
    <errorEventDefinition errorRef="myError" />
  </endEvent>
  ```

  errorRef属性可以引用在流程外定义的error元素：

  ```xml
  <error id="myError" errorCode="error123" />
  ...
  <process id="myProcess">
  ...
  ```

  error的**errorCode**用于查找匹配的错误捕获边界事件。如果errorRef不匹配任何已定义的error，则该errorRef会用做errorCode的快捷方式。这个快捷方式是Flowable特有的。下面的代码片段在功能上是相同的。

  ```
  <error id="myError" errorCode="error123" />
  ...
  <process id="myProcess">
  ...
    <endEvent id="myErrorEndEvent">
      <errorEventDefinition errorRef="myError" />
    </endEvent>
  ...
  ```

  与下面的代码功能相同

  ```xml
  <endEvent id="myErrorEndEvent">
      <errorEventDefinition errorRef="error123" />
  </endEvent>
  ```

> 请注意errorRef必须遵从BPMN 2.0概要(schema)，且必须时合法的QName.

#### 终止结束事件

- 描述

  当到达终止结束事件*（terminate end event）*时，当前的流程实例或子流程会被终止。也就是说，当执行到达终止结束事件时，会判断第一个范围 scope（流程或子流程）并终止它。请注意在BPMN 2.0中，子流程可以是嵌入式子流程，调用活动，事件子流程，或事务子流程。有一条通用规则：当存在多实例的调用过程或嵌入式子流程时，只会终止一个实例，其他的实例与流程实例不会受影响。 

  可以添加一个可选属性*terminateAll*。当其为*true*时，无论该终止结束事件在流程定义中的位置，也无论它是否在子流程（甚至是嵌套子流程）中，都会终止（根）流程实例。

- 图示

  终止结束事件用内部有一个全黑圆的标准结束事件（粗圆圈）表示。
  
  ![image-20200806140141255](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200806140141255.png)

- XML表示

  终止结束事件，表示为结束事件，加上terminateEventDefinition子元素。

  请注意terminateAll属性是可选的（默认为false）。

  ```xml
  <endEvent id="myEndEvent >
    <terminateEventDefinition flowable:terminateAll="true"></terminateEventDefinition>
  </endEvent>
  ```

#### 取消结束事件

- 描述

  取消结束事件（cancel end event）只能与BPMN事务子流程（BPMN transaction subprocess）一起使用。当到达取消结束事件时，会抛出取消事件，且必须由取消边界事件（cancel boundary event）捕获。取消边界事件将取消事务，并触发补偿（compensation）。

- 图示

  取消结束事件用内部有一个取消图标的标准结束事件（粗圆圈）表示。取消图标是全黑的，代表抛出的含义。

  ![image-20200806141059870](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200806141059870.png)

- XML表示

  取消结束事件，表示为结束事件，加上cancelEventDefinition子元素。

  ```xml
  <endEvent id="myCancelEndEvent">
    <cancelEventDefinition />
  </endEvent>
  ```

### 边界事件 *boundaryEvent*

边界事件（boundary event）是*捕获型*事件，依附在活动（activity）上。边界事件永远不会抛出。这意味着当活动运行时，事件将*监听*特定类型的触发器。当*捕获*到事件时，会终止活动，并沿该事件的出口顺序流继续。

所有的边界事件都用相同的方式定义：

```xml
<boundaryEvent id="myBoundaryEvent" attachedToRef="theActivity">
      <XXXEventDefinition/>
</boundaryEvent>
```

边界事件由下列元素定义：

- （流程范围内）唯一的标识符 
- 由**attachedToRef**属性定义的，对该事件所依附的活动的引用。请注意边界事件及其所依附的活动，应定义在相同级别（也就是说，边界事件并不包含在活动内）。 
- 定义了边界事件的类型的，形如XXXEventDefinition的XML子元素（例如TimerEventDefinition，ErrorEventDefinition，等等）。查阅特定的边界事件类型，以了解更多细节。

#### 定时器边界事件

- 描述

  定时器边界事件（timer boundary event）的行为像是跑表与闹钟。当执行到达边界事件所依附的活动时，将启动定时器。当定时器触发时（例如在特定时间间隔后），可以中断活动，并沿着边界事件的出口顺序流继续执行。

- 图示

  定时器边界事件用内部有一个定时器图标的标准边界事件（圆圈）表示。

  ![image-20200806144920573](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200806144920573.png)

- XML表示

  定时器边界事件与一般边界事件一样定义。其中类型子元素为**timerEventDefinition**元素。

  ```xml
  <boundaryEvent id="escalationTimer" cancelActivity="true" attachedToRef="firstLineSupport">
      <timerEventDefinition>
          <!--等待4小时后执行-->
        <timeDuration>PT4H</timeDuration>
      </timerEventDefinition>
  </boundaryEvent>
  ```

#### 错误边界事件

#### 信号边界事件

#### 取消边界事件

#### 补偿边界事件

### 捕获中间事件

#### 定时器捕获中间事件

#### 信号捕获中间事件

#### 消息捕获中间事件

### 抛出中间事件

#### 空抛出事件

#### 信号抛出事件

#### 补偿抛出中间事件

---

## 顺序流*（Sequence Flow）*

### 条件顺序流

### 默认顺序流

---

## 网关*（Gateway）*

网关(gateway)用于控制执行的流向（或者按BPMN 2.0的用词：执行的“*标志(token)*”）。网关可以消费(consuming)标志。网关用其中带有图标的菱形表示。该图标显示了网关的类型。

![img](https://yun1.gree.com/microblog/filesvr/5f2bb5f684ae79c170978c9b?big)

### 排他网关*（exclusive gateway）*

#### 描述

排他网关(exclusive gateway) （也叫异或网关 *XOR gateway*，或者更专业的，基于数据的排他网关 exclusive data-based gateway），用于对流程中过的决策建模。当执行到达这个网关时，会按照所有出口顺序流定义的顺序对它们进行计算。选择第一个条件计算为true的顺序流（当没有设置条件时，认为顺序流为true）继续流程。

**注意这里出口顺序流的含义与BPMN 2.0中的一般情况不一样。一般情况下，会选择所有条件计算为true的顺序流，并行执行。而使用排他网关时，只会选择一条顺序流。当多条顺序流的条件都计算为true时，会且仅会选择在XML中最先定义的顺序流继续流程。如果没有可选的顺序流，会抛出异常。**

#### 图示

排他网关用内部带有’X’图标的标准网关（菱形）表示，'X’图标代表*异或*的含义。请注意内部没有图标的网关默认为排他网关。BPMN 2.0规范不允许在同一个流程中混合使用有及没有X的菱形标志。

![image-20200806160032482](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200806160032482.png)

#### XML表示

排他网关的XML表示格式很简洁：一行定义网关的XML。条件表达式定义在其出口顺序流上。查看条件顺序流章节了解这种表达式的可用选项。

以下面的模型为例：

![img](https://yun1.gree.com/microblog/filesvr/5f2bb8e484ae79c170979edc?big)

其XML表示如下：

```xml
<exclusiveGateway id="exclusiveGw" name="Exclusive Gateway" />

<sequenceFlow id="flow2" sourceRef="exclusiveGw" targetRef="theTask1">
  <conditionExpression xsi:type="tFormalExpression">${input == 1}</conditionExpression>
</sequenceFlow>

<sequenceFlow id="flow3" sourceRef="exclusiveGw" targetRef="theTask2">
  <conditionExpression xsi:type="tFormalExpression">${input == 2}</conditionExpression>
</sequenceFlow>

<sequenceFlow id="flow4" sourceRef="exclusiveGw" targetRef="theTask3">
  <conditionExpression xsi:type="tFormalExpression">${input == 3}</conditionExpression>
</sequenceFlow>
```

### 并行网关*（parallel gateway）*

#### 描述

网关也可以建模流程中的并行执行。在流程模型中引入并行的最简单的网关，就是并行网关（parallel gateway）。它可以将执行分支（fork）为多条路径，也可以合并（join）多条入口路径的执行。

并行网关的功能取决于其入口与出口顺序流：

- **分支**：所有的出口顺序流都并行执行，为每一条顺序流创建一个并行执行。
- **合并**：所有到达并行网关的并行执行都会在网关处等待，直到每一条入口顺序流都到达了有个执行。然后流程经过该合并网关继续。

请注意，如果并行网关同时具有多条入口与出口顺序流，可以**同时具有分支与合并的行为**。在这种情况下，网关首先合并所有入口顺序流，然后分裂为多条并行执行路径。 **与其他网关类型有一个重要区别：并行网关不计算条件。如果连接到并行网关的顺序流上定义了条件，会直接忽略该条件**。

#### 图示

并行网关，用内部带有’加号’图标的网关（菱形）表示，代表与*（AND）*的含义。

![img](https://yun1.gree.com/microblog/filesvr/5f2bc02384ae79c17097f9a3?big)

#### XML表示

定义并行网关只需要一行XML：

```xml
<parallelGateway id="myParallelGateway" />
```

实际行为（分支，合并或者两者皆有），由连接到该并行网关的顺序流定义。

例如，上面的模型表示为下面的XML:

```xml
<startEvent id="theStart" />
<sequenceFlow id="flow1" sourceRef="theStart" targetRef="fork" />

<parallelGateway id="fork" />
<sequenceFlow sourceRef="fork" targetRef="receivePayment" />
<sequenceFlow sourceRef="fork" targetRef="shipOrder" />

<userTask id="receivePayment" name="Receive Payment" />
<sequenceFlow sourceRef="receivePayment" targetRef="join" />

<userTask id="shipOrder" name="Ship Order" />
<sequenceFlow sourceRef="shipOrder" targetRef="join" />

<parallelGateway id="join" />
<sequenceFlow sourceRef="join" targetRef="archiveOrder" />

<userTask id="archiveOrder" name="Archive Order" />
<sequenceFlow sourceRef="archiveOrder" targetRef="theEnd" />

<endEvent id="theEnd" />
```

在上面的例子中，当流程启动后会创建两个任务：

```java
ProcessInstance pi = runtimeService.startProcessInstanceByKey("forkJoin");
TaskQuery query = taskService.createTaskQuery()
    .processInstanceId(pi.getId())
    .orderByTaskName()
    .asc();

List<Task> tasks = query.list();
assertEquals(2, tasks.size());

Task task1 = tasks.get(0);
assertEquals("Receive Payment", task1.getName());
Task task2 = tasks.get(1);
assertEquals("Ship Order", task2.getName());
```

当这两个任务完成后，第二个并行网关会合并这两个执行。由于它只有一条出口顺序流，因此就不会再创建并行执行路径，而只是激活Archive Order(存档订单)任务。 

请注意并行网关不需要“平衡”（也就是说，前后对应的两个并行网关，其入口/出口顺序流的数量不需要一致）。每个并行网关都会简单地等待所有入口顺序流，并为每一条出口顺序流创建并行执行，而不受流程模型中的其他结构影响。因此，下面的流程在BPMN 2.0中是合法的：

![img](https://yun1.gree.com/microblog/filesvr/5f2bc63784ae79c1709846ae?big)

### 包容网关*（inclusiveGateway）*

#### 描述

可以把包容网关（inclusive gateway）看做排他网关与并行网关的组合。与排他网关一样，可以在包容网关的出口顺序流上定义条件，包容网关会计算条件。然而主要的区别是，包容网关与并行网关一样，可以同时选择多于一条出口顺序流。

包容网关的功能取决于其入口与出口顺序流：

- **分支**：流程会计算所有出口顺序流的条件。对于每一条计算为true的顺序流，流程都会创建一个并行执行。
- **合并**：所有到达包容网关的并行执行，都会在网关处等待。直到每一条具有流程标志（process token）的入口顺序流，都有一个执行到达。这是与并行网关的重要区别。换句话说，包容网关只会等待可以被执行的入口顺序流。在合并后，流程穿过合并并行网关继续。

请注意，如果包容网关同时具有多条入口与出口顺序流，可以**同时具有分支与合并的行为**。在这种情况下，网关首先合并所有具有流程标志的入口顺序流，然后为每一个条件计算为true的出口顺序流分裂出并行执行路径。

>包容网关的汇聚行为比并行网关更复杂。所有到达包容网关的并行执行，都会在网关等待，直到所有“可以到达”包容网关的执行都“到达”包容网关。 判断方法为：计算当前流程实例中的所有执行，检查从其位置是否有一条到达包容网关的路径（忽略顺序流上的任何条件）。如果存在这样的执行（可到达但尚未到达），则不会触发包容网关的汇聚行为。

#### 图示

![img](https://yun1.gree.com/microblog/filesvr/5f2bc9f384ae79c170987290?big)

#### XML表示

```xml
<startEvent id="theStart" />
<sequenceFlow id="flow1" sourceRef="theStart" targetRef="fork" />

<inclusiveGateway id="fork" />
<sequenceFlow sourceRef="fork" targetRef="receivePayment" >
  <conditionExpression xsi:type="tFormalExpression">${paymentReceived == false}</conditionExpression>
</sequenceFlow>
<sequenceFlow sourceRef="fork" targetRef="shipOrder" >
  <conditionExpression xsi:type="tFormalExpression">${shipOrder == true}</conditionExpression>
</sequenceFlow>

<userTask id="receivePayment" name="Receive Payment" />
<sequenceFlow sourceRef="receivePayment" targetRef="join" />

<userTask id="shipOrder" name="Ship Order" />
<sequenceFlow sourceRef="shipOrder" targetRef="join" />

<inclusiveGateway id="join" />
<sequenceFlow sourceRef="join" targetRef="archiveOrder" />

<userTask id="archiveOrder" name="Archive Order" />
<sequenceFlow sourceRef="archiveOrder" targetRef="theEnd" />

<endEvent id="theEnd" />
```

在上面的例子中，当流程启动后，如果流程变量paymentReceived == false且shipOrder == true，会创建两个任务。如果只有一个流程变量等于true，则只会创建一个任务。如果没有条件计算为true，会抛出异常（可通过指定默出口顺序流避免）。在下面的例子中，只会创建ship order（传递订单）一个任务：

```java
HashMap<String, Object> variableMap = new HashMap<String, Object>();
variableMap.put("receivedPayment", true);
variableMap.put("shipOrder", true);

ProcessInstance pi = runtimeService.startProcessInstanceByKey("forkJoin");

TaskQuery query = taskService.createTaskQuery()
    .processInstanceId(pi.getId())
    .orderByTaskName()
    .asc();

List<Task> tasks = query.list();
assertEquals(1, tasks.size());

Task task = tasks.get(0);
assertEquals("Ship Order", task.getName());
```

当这个任务完成后，第二个包容网关会合并这两个执行。并且由于它只有一条出口顺序流，所有不会再创建并行执行路径，而只会激活Archive Order(存档订单)任务。 

请注意包容网关不需要“平衡”（也就是说，对应的包容网关，其入口/出口顺序流的数量不需要匹配）。包容网关会简单地等待所有入口顺序流，并为每一条出口顺序流创建并行执行，不受流程模型中的其他结构影响。 

请注意包容网关不需要“平衡”（也就是说，前后对应的两个包容网关，其入口/出口顺序流的数量不需要一致）。每个包容网关都会简单地等待所有入口顺序流，并为每一条出口顺序流创建并行执行，不受流程模型中的其他结构影响。

### 基于事件的网关

#### 描述

基于事件的网关（event-based gateway）提供了根据事件做选择的方式。网关的每一条出口顺序流都需要连接至一个捕获中间事件。当流程执行到达基于事件的网关时，与等待状态类似，网关会暂停执行，并且为每一条出口顺序流创建一个事件订阅。 

请注意：基于事件的网关的出口顺序流与一般的顺序流不同。这些顺序流从不实际执行。相反，它们用于告知流程引擎：当执行到达一个基于事件的网关时，需要订阅什么事件。有以下限制：

- 一个基于事件的网关，必须有两条或更多的出口顺序流。 
- 基于事件的网关，只能连接至intermediateCatchEvent（捕获中间事件）类型的元素（Flowable不支持在基于事件的网关之后连接“接收任务 Receive Task”）。 
- 连接至基于事件的网关的intermediateCatchEvent，必须只有一个入口顺序流。

#### 图示

![image-20200806173030911](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200806173030911.png)

#### XML表示

用于定义基于事件的网关的XML元素为eventBasedGateway。

示例：

下面是一个带有基于事件的网关的示例流程。当执行到达基于事件的网关时，流程执行暂停。流程实例订阅alert信号事件，并创建一个10分钟后触发的定时器。流程引擎会等待10分钟，并同时等待信号事件。如果信号在10分钟内触发，则会取消定时器，流程沿着信号继续执行，激活Handle alert用户任务。如果10分钟内没有触发信号，则会继续执行，并取消信号订阅。

![img](https://yun1.gree.com/microblog/filesvr/5f2bce6484ae79c17098a06d?big)

```xml
<definitions id="definitions"
	xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL";
	xmlns:flowable="http://flowable.org/bpmn";
	targetNamespace="Examples">

    <signal id="alertSignal" name="alert" />

    <process id="catchSignal">

        <startEvent id="start" />

        <sequenceFlow sourceRef="start" targetRef="gw1" />

        <eventBasedGateway id="gw1" />

        <sequenceFlow sourceRef="gw1" targetRef="signalEvent" />
        <sequenceFlow sourceRef="gw1" targetRef="timerEvent" />

        <intermediateCatchEvent id="signalEvent" name="Alert">
            <signalEventDefinition signalRef="alertSignal" />
        </intermediateCatchEvent>

        <intermediateCatchEvent id="timerEvent" name="Alert">
            <timerEventDefinition>
                <timeDuration>PT10M</timeDuration>
            </timerEventDefinition>
        </intermediateCatchEvent>

        <sequenceFlow sourceRef="timerEvent" targetRef="exGw1" />
        <sequenceFlow sourceRef="signalEvent" targetRef="task" />

        <userTask id="task" name="Handle alert"/>

        <exclusiveGateway id="exGw1" />

        <sequenceFlow sourceRef="task" targetRef="exGw1" />
        <sequenceFlow sourceRef="exGw1" targetRef="end" />

        <endEvent id="end" />
    </process>
</definitions>
```

---

## 任务*（task）*

### 用户任务*（userTask）*

用于对需要人工执行的任务进行建模。当流程执行到达用户任务时，会为指派至该任务的用户或组的任务列表创建一个新任务。

![image-20200806182912304](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200806182912304.png)

```xml
<userTask id="theTask" name="First line supprt" />
```

也可以为用户任务添加描述（description）。事实上任何BPMN 2.0元素都可以有描述。描述由**documentation**元素定义。

```xml
<userTask id="theTask" name="First line supprt" >
    <documentation>
        Schedule an engineering meeting for next week with the new hire
    </documentation>
</userTask>
```

可以使用标准Java方式获取描述文本：

```java
task.getDescription();
```

#### 到期日期

每个任务都可以使用一个字段标志该任务的到期日期（due date）。可以使用查询API，查询在给定日期前或后到期的任务。 

可以在任务定义中使用扩展指定表达式，以在任务创建时设定到期日期。**该表达式必须解析为java.util.Date，java.util.String (ISO8601格式)，ISO8601时间长度（例如PT50M），或者null**。例如，可以使用在流程里前一个表单中输入的日期，或者由前一个服务任务计算出的日期。如果使用的是时间长度，则到期日期基于当前时间加上给定长度计算。例如当dueDate使用“PT30M”时，任务在从现在起30分钟后到期。

```xml
<userTask id="theTask" name="First line supprt"  flowable:dueDate="${dateVariable}" />
```

任务的到期日期也可以使用TaskService，或者在TaskListener中使用传递的Delegate*（委派）*修改。

#### 用户指派



### 脚本任务*(scriptTask)*

### Java服务任务*(serviceTask)*