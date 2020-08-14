# 使用Flowable-modeler-ui定义流程

## 创建

![img](https://yun1.gree.com/microblog/filesvr/5f20e8d284ae79c1708a2350?big)

![img](https://yun1.gree.com/microblog/filesvr/5f20e8ee84ae79c1708a23b5?big)

- 等5秒钟，就出来了：查看帮助。

![img](https://yun1.gree.com/microblog/filesvr/5f20e8f384ae79c1708a23c7?big)

---



## 工作流设计的准则/原理

### 概念

> 工作流（Workflow），就是“业务过程的部分或整体在计算机应用环境下的自动化”，它主要解决的是“使在多个参与者之间按照某种预定义的规则传递文档、信息或任务的过程自动进行，从而实现某个预期的业务目标，或者促使此目标的实现”。通俗的说，流程就是多个人在一起合作完成某件事情的步骤，把步骤变成计算机能理解的形式就是工作流。

### 总结

再结合之前clone下来的demo，可以看得出来，工作流的核心是多人合作。也就是以人或者角色来作为衡量标准。

---



## 具体实施

1.启动事件

![img](https://yun1.gree.com/microblog/filesvr/5f20ec7e84ae79c1708a2cb7?big)

> 发起人由程序规定

- 定义维修任务用户组

![img](https://yun1.gree.com/microblog/filesvr/5f20ed6384ae79c1708a2e4e?big)

没有候选组，需要去idm定义

![img](https://yun1.gree.com/microblog/filesvr/5f20ed6384ae79c1708a2e4e?big)

![img](https://yun1.gree.com/microblog/filesvr/5f20ef7f84ae79c1708a3bf3?big)

- 先添加用户再添加组，并把用户放到对应的组里

![img](https://yun1.gree.com/microblog/filesvr/5f20eff184ae79c1708a3cc3?big)

- 定义维修任务的关联组 

- 定义确认报修完毕任务

![img](https://yun1.gree.com/microblog/filesvr/5f20eff384ae79c1708a3cd0?big)



- 定义结束



## 验证

```
act_re_model：这张表，在xml进行部署时，它没有内容(flowable放弃了此表改用act_de_model保存流程模型信息）
```



没有乱码。OK