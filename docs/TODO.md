## BO Engine
* 支持使用subprocess标签调用其她工作流系统的子流程

## Resource Service
* 异常处理只做到把异常工作项重定向到异常池，后续没做
* 配合DataStore在分发任务时支持分发数据（附件），参考YAWL的Document
Store功能

## Name Service
* 需要实现工作流事务机制，即完全成功/失败，可以基于**ZooKeeper**
去做，具体方法不难，和分布式数据库事务同理（写binlog和redolog，多
阶段提交），在此不赘述。

## Data Store Service
* 分布式配置<br/>
现在各部分服务的配置都是分散保存在各自的`GlobalDataContext`中，
这样改配置很麻烦，要重新编译。考虑使用**ZooKeeper**框架来实现配
置动态管理分发，DataStore已经实现了一部分。

## Web UI
* 流程上传可视化界面
* 业务角色映射可视化界面，包括映射模板的便捷保存
* 权限验证还有一定的问题，先前为了赶紧把一个测试版本做出来，有些
POST请求处理函数是权限不安全的，比如管理员编辑某个用户信息和用户
自己编辑用的是同一个POST Perform函数，这显然很不安全，应该修正。