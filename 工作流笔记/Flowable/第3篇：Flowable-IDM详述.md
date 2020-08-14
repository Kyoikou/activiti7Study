# Flowable-IDM详述

## 源

身份管理(IDM IDentity Management)

该组件模块自Flowable V6起，从Flowable引擎模块中抽出，并将其逻辑移至几个不同的模块

- flowable-idm-api
- flowable-idm-engine
- flowable-idm-spirng
- flowable-idm-engine-configurator

分离IDM主要是因为它不是Flowable引擎的核心，并且在很多将Flowable引擎嵌入应用的用例中，并不使用或需要这部分身份管理逻辑。

> 登录页面
>
> ![image-20200729093400996](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200729093400996.png)

---



## IDM功能

> 页面
>
> ![image-20200729093556339](C:\Users\180681\AppData\Roaming\Typora\typora-user-images\image-20200729093556339.png)

默认情况下，IDM引擎在Flowable引擎启动时初始化并启动。这样身份管理逻辑在Flowable V5中也可以使用。IDM引擎管理自己的数据库表结构及下列实体及对应功能：

- User与UserEntity，提供用户管理功能：可以添加用户、编辑用户、删除用户和密码修改功能
- Group与GroupEntity，提供用户分组功能：提供用户组的创建、用户组的删除、添加删除用户到组，方便统一管理用户权限，是一个简化版的角色处理
- MembershipEntity，组中的用户成员
- Privilege与PrivilegeEntity，权限定义：权限简单分为idm/admin/modeler/workflow/rest的访问权限控制，通过配置用户和组来管理用户的访问权限
- PrivilegeMappingEntity，将用户及/或组与权限关联。
- Token与TokenEntity，应用界面程序使用的认证令牌：单点登录使用。

> 历史与当前进行中的流程实例都在数据库中保存历史实体，因此可以选择直接查询历史表，以减少对运行时实例数据的访问，并提高有运行时执行的性能

---



## IDM特性

- IDM的相关表以ACT_ID开头。如ACT_ID_USER、ACT_ID_GROUP

- Rest-Api权限需要flowable.rest.app.authentication-mode设置为verify-privilege，默认值也是该值，如果没权限，则返回403无权限 

- 如果不用自带的用户体系，可以设置 flowable.idm.ldap.enabled=true使用ldap server来设置用户鉴权，不过只是用户和组，权限配置还是在Flowable的表中，所以如果使用LDAP鉴权，那么确保Ldap的用户权限在Flowable中正确配置 

- 如果使用LDAP，那么第一次启动会给配置的flowable.common.app.idm-admin.user 用户所有的默认的4个权限，防止没有一个用户能够登录系统

- IDM的常用参数配置

  ![img](https://yun1.gree.com/microblog/filesvr/5f20d93084ae79c17089f878?big)