## BO Engine


## Resource Service


## Name Service


## Data Store Service
* 分布式配置<br/>
现在各部分服务的配置都是分散保存在各自的`GlobalDataContext`中，
这样改配置很麻烦，要重新编译。考虑使用**ZooKeeper**框架来实现配
置动态管理分发，DataStore已经实现了一部分

## Web UI
* 流程上传可视化界面
* 业务角色映射可视化界面，包括映射模板的便捷保存
* 