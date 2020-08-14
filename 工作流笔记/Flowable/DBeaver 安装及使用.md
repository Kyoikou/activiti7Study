# DBeaver 安装及使用

![img](https://yun1.gree.com/microblog/filesvr/5f23d54684ae79c1708e95a6?big)

## 一、简介

一款免费开源的通用数据库可视化工具DBeaver，也就是上面的可爱的小河狸。github上有14.4K star

DBeaver 是一个基于 Java 开发，免费开源的通用数据库管理和开发工具，使用非常友好的 ASL 协议。可以通过官方网站或者 Github 进行下载。

由于 DBeaver 基于 Java 开发，可以运行在各种操作系统上，包括：Windows、Linux、macOS 等。

DBeaver 采用 Eclipse 框架开发，支持插件扩展，并且提供了许多数据库管理工具：ER 图、数据导入/导出、数据库比较、模拟数据生成等。 DBeaver 通过 JDBC 连接到数据库，可以支持几乎所有的数据库产品，包括：MySQL、PostgreSQL、MariaDB、SQLite、Oracle、Db2、SQL Server、Sybase、MS Access、Teradata、Firebird、Derby 等等。商业版本更是可以支持各种 NoSQL 和大数据平台：MongoDB、InfluxDB、Apache Cassandra、Redis、Apache Hive 等。

![img](https://yun1.gree.com/microblog/filesvr/5f23d63f84ae79c1708e98be?big)

​													(前往github搜索，找到Release下载对应系统稳定版本)

---

## 二、软件对比

### PL/SQL

![image-20200731163340380](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731163340380.png)



当前使用率最高的Oracle数据库工具，但是页面不太友好(个人意见)，并且只支持查询Oracle数据库。

**主要的是，时间用久了，它莫名其妙的就卡死~**



### Navicat

![img](https://yun1.gree.com/microblog/filesvr/5f23d80384ae79c1708ea403?big)



学生时代常用的软件，虽然是商业软件，但破解也很简单。缺点是下载数据库驱动麻烦

---

## 三、安装

![image-20200731163936358](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731163936358.png)

window用户使用下载好的setup.exe文件一路点点点傻瓜式安装就好了。

![image-20200731164137133](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731164137133.png)

组件根据个人喜好，无所谓。reset setting是重置缓存，就是以前安装过改软件恢复为默认设置，还有一个组件没研究透。

![image-20200731165529498](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731165529498.png)

安装路径最好别包含中文，养成良好的搭建开发环境习惯。

![image-20200731165721357](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731165721357.png)

然后就开始使用了

---

## 四、使用

> 使用前请确保你的电脑有oracle、mysql客户端的数据库服务！没有请快点去安装吧，详情请百度。。。
>
> ![image-20200731170014165](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731170014165.png)
>
> ![image-20200731170131835](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731170131835.png)





### 1、新建Mysql连接

![image-20200731170546425](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731170546425.png)



许多数据连接，这里先测试Mysql

![image-20200731170613087](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731170613087.png)



先选择自己安装的客服端，一般都选server

![image-20200731170711254](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731170711254.png)



查看驱动是否安装

![image-20200731170759490](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731170759490.png)



有外网的电脑**下载/更新**就好了，自动匹配适应的版本

主要是使用格力内网的伙伴，没有连接外网啊(懂网络的大佬可以内外网穿透)。

**这里选中所有的驱动库并删除，防止冲突**

![img](https://yun1.gree.com/microblog/filesvr/5f23de5584ae79c1708eb92d?big)



**点添加文件夹，将我发的文件解压缩，选择mysql文件夹，那是下载好的驱动包**

![image-20200731171210982](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731171210982.png)

![image-20200731171347486](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731171347486.png)



确定

![image-20200731171452406](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731171452406.png)



**开始填入主机与端口测试，Mysql数据库那块可以填空，这样默认访问主机所有的数据库**

![image-20200731171523782](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731171523782.png)

测试连接完成！

![image-20200731171633850](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731171633850.png)



左边窗口就有一个mysql的连接了

![image-20200731171736136](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731171736136.png)



该有的功能应有尽有，一次见可以查看ER图的软件

![image-20200731171848021](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731171848021.png)

---

### 2、ORACLE连接

同连接Mysql步骤一致

![image-20200731171938727](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731171938727.png)



**请选择正确的Oracle客户端服务！再进行驱动安装**

![image-20200731172023922](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731172023922.png)



**删干净没下载好的驱动，再选择oracle文件夹，驱动都下载好了，别怀疑**

![image-20200731172115144](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731172115144.png)



**确定好回到上一界面填好主机ip和账号密码，确认账号等级**

![image-20200731172351900](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731172351900.png)

![image-20200731172359085](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731172359085.png)



**ORACLE的存储结构不一样**

![image-20200731172443008](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731172443008.png)

![image-20200731172508500](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731172508500.png)



**右键表名就有相应操作**

![image-20200731172649104](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731172649104.png)





窗口上面有提示目前窗口连接的是哪个数据库的哪个表(模式)，方便在不同数据库查询中切换查询自如

![image-20200731172750728](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731172750728.png)



**新建SQL窗口在这**

![image-20200731173022159](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731173022159.png)



**执行建在这，默认快捷键是ctrl+Enter**

![image-20200731173201809](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731173201809.png)

**右键空白处就有格式化代码等操作，更多操作请自行发掘吧。。。**

![image-20200731173321459](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200731173321459.png)