# flowable数据库配置



## Mysql

低于5.6.4的Mysql版本不支持timestaments或包含毫秒精度的日期。更糟的的是部分版本会在创建类似的列时抛出异常。

## 数据库表名说明

Flowable的所有数据库表都以act_开头。第二部分时说明表用途的两字符标识符。服务API的命名也大略符合这个规则。

Flowable6.5一共46张表

- act_re_* :  re代表respository。带有这个前缀的表包含“静态”信息，例如流程定义域流程资源(图片、规则等)。
- act_ru_*:  ru代表runtime。这些表存储运行时信息，例如流程实例(process instance)、用户任务(user task)、变量(variable)、作业(job)等。Flowable只在流程实例运行中保存运行时数据，并在流程实例结束时删除记录。这样保证运行时表小和快。
- act_hi_*:  hi代表history。这些表存储历史数据，例如已完成的流程实例、变量、任务等。
- act_ge_*: 通用数据，在多处使用。

| 表分类       | 表名称                | 表含义                         |
| ------------ | --------------------- | ------------------------------ |
|              | act_evt_log           | 事件处理日志表                 |
| 一般数据     | act_ge_bytearray      | 通用的流程定义和流程资源       |
|              | act_ge_property       | 系统相关属性                   |
| 流程历史记录 | act_hi_actinst        | 历史的流程实例                 |
|              | act_hi_attachment     | 历史的流程附件                 |
|              | act_hi_comment        | 历史的说明性信息               |
|              | act_hi_detail         | 历史的流程运行中的细节信息     |
|              | act_hi_identitylink   | 历史的流程运行过程中的用户关系 |
|              | act_hi_procinst       | 历史的流程实例                 |
|              | act_hi_taskinst       | 历史的任务实例                 |
|              | act_hi_varinst        | 历史的流程运行中的变量信息     |
| 用户组表     | act_id_group          | 身份信息-组信息                |
|              | act_id_info           | 身份信息-组信息                |
|              | act_id_membership     | 身份信息-用户和组关系的中间表  |
|              | act_id_user           | 身份信息-用户信息              |
|              | act_procdef_info      | 死信任务                       |
| 流程定义表   | act_re_deployment     | 部署单元信息                   |
|              | act_re_model          | 模型信息                       |
|              | act_re_procdef        | 已部署的流程定义               |
| 运行实例表   | act_ru_deadletter_job | 执行失败任务表                 |
|              | act_ru_event_subscr   | 运行时事件                     |
|              | act_ru_execution      | 运行时流程执行实例             |
|              | act_ru_identitylink   | 运行时用户关系信息             |
|              | act_ru_job            | 运行时作业                     |
|              | act_ru_suspended_job  | 运行时暂停任务                 |
|              | act_ru_task           | 运行任务                       |
|              | act_ru_timer_job      | 运行时定时任务                 |
|              | act_ru_variable       | 运行时变量表                   |

