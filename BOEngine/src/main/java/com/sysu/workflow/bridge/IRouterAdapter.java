package com.sysu.workflow.bridge;

import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * 为状态机引擎提供与消息处理路由的接口
 * Created by Rinkako on 2017/5/24.
 */
public interface IRouterAdapter {
    /* Methods of Connection */
    /**
     * 连接SCXML引擎到消息处理路由
     * @param username 登录路由服务的用户名
     * @param password 登录路由服务的密码串（路由可能要求发送已加密的串）
     * @param outResult [out] 登录结果
     * @return 操作是否成功
     */
    boolean ConnectToRouter(String username, String password, StringBuilder outResult);

    /**
     * 获取当前是否成功连接到消息路由
     * @param handle 连接句柄
     * @param outResult [out] 是否连接到路由
     * @return 操作是否成功
     */
    boolean CheckConnectToRouter(String handle, StringBuilder outResult);

    /**
     * 断开SCXML引擎到消息路由的连接
     * @param handle 连接句柄
     * @param outResult [out] 断开的结果
     * @return 操作是否成功
     */
    boolean DisconnectFromRouter(String handle, StringBuilder outResult);

    /* Methods of Participant and Nonhuman Service */

    /**
     * 添加一个参与者
     * @param handle 连接句柄
     * @param userid 用户名
     * @param password 密码明文
     * @param encrypt 密码是否加密保存
     * @param lastname 姓
     * @param firstname 名
     * @param admin 是否为管理员
     * @param description 描述文本
     * @param notes 备注文本
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean addParticipant(String handle, String userid, String password, boolean encrypt,
                           String lastname, String firstname, boolean admin,
                           String description, String notes, StringBuilder outResult);

    /**
     * 添加一个非人资源
     * @param handle 连接句柄
     * @param name 名字
     * @param category 目录
     * @param subcategory 子目录
     * @param description 描述文本
     * @param notes 备注文本
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean addNonHumanResource(String handle, String name, String category, String subcategory,
                                String description, String notes, StringBuilder outResult);

    /**
     * 添加一个角色
     * @param handle 连接句柄
     * @param name 角色名字
     * @param description 描述文本
     * @param notes 备注文本
     * @param containingRoleID 包含角色id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean addRole(String handle, String name, String description, String notes,
                    String containingRoleID, StringBuilder outResult);

    /**
     * 将一个参与者添加到角色
     * @param handle 连接句柄
     * @param participantID 参与者Pid
     * @param roleID 角色id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean addParticipantToRole(String handle, String participantID, String roleID, StringBuilder outResult);

    /**
     * 添加一个非人力资源目录
     * @param handle 连接句柄
     * @param category 目录名
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean addNonHumanCategory(String handle, String category, StringBuilder outResult);

    /* Methods of User */
    /**
     * 通过用户唯一标识符获取用户的ParticipantId
     * @param handle 连接句柄
     * @param username 要检索Pid的用户名
     * @param outResult [out] 该用户的Pid
     * @return 操作是否成功
     */
    boolean GetParticipantFromUserID(String handle, String username, StringBuilder outResult);

    /* Methods of Case */
    /**
     * 上传一个过程描述文件
     * @param handle 连接句柄
     * @param filePath 过程描述文件在本地的绝对路径（含文件名）
     * @param fileName 过程描述文件的文件名
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean UploadSpecificationFile(String handle, String filePath, String fileName, StringBuilder outResult);

    /**
     * 卸载一个过程
     * 关于第2到第4个参数的意义，请参考YSpecificationID的toMap方法
     * @param handle 连接句柄
     * @param specidentifier a system generated UUID string
     * @param specversion verion
     * @param specuri the user-defined name
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean UnloadSpecification(String handle, String specidentifier, String specversion, String specuri, StringBuilder outResult);

    /**
     * 获取当前加载的过程
     * @param handle 连接句柄
     * @param outResult [out] 当前加载的过程集合的串表达
     * @return 操作是否成功
     */
    boolean GetLoadedSpecificationList(String handle, StringBuilder outResult);

    /**
     * 获取一个过程的数据
     * 关于第2到第4个参数的意义，请参考YSpecificationID的toMap方法
     * @param handle 连接句柄
     * @param specidentifier a system generated UUID string
     * @param specversion verion
     * @param specuri the user-defined name
     * @param outResult [out] 数据的XML格式串
     * @return 操作是否成功
     */
    boolean GetSpecificationData(String handle, String specidentifier, String specversion, String specuri, StringBuilder outResult);

    /**
     * 获取一个过程的名称
     * 关于第2到第4个参数的意义，请参考YSpecificationID的toMap方法
     * @param handle 连接句柄
     * @param specidentifier a system generated UUID string
     * @param specversion verion
     * @param specuri the user-defined name
     * @param outResult [out] 过程名称
     * @return 操作是否成功
     */
    boolean GetSpecificationName(String handle, String specidentifier, String specversion, String specuri, StringBuilder outResult);


    /**
     * 启动一个已经上传的过程描述文件
     * 关于第2到第4个参数的意义，请参考YSpecificationID的toMap方法
     * @param handle 连接句柄
     * @param specidentifier a system generated UUID string
     * @param specversion verion
     * @param specuri the user-defined name
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean launchCase(String handle, String specidentifier, String specversion, String specuri, StringBuilder outResult);

    /**
     * 获取指定流程的所有运行中的案例
     * 关于第2到第4个参数的意义，请参考YSpecificationID的toMap方法
     * @param handle 连接句柄
     * @param specidentifier a system generated UUID string
     * @param specversion verion
     * @param specuri the user-defined name
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean GetRunningCases(String handle, String specidentifier, String specversion, String specuri, StringBuilder outResult);

    /**
     * 取消一个Case
     * @param handle 连接句柄
     * @param caseId Case的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean CancelCase(String handle,  String caseId, StringBuilder outResult);



    /* Methods of WorkQueue */
    /**
     * 通过ParticipantId获取该人物各类型的工作列表
     * UNDEFINED     -1
     * OFFERED       0
     * ALLOCATED     1
     * STARTED       2
     * SUSPENDED     3
     * UNOFFERED     4
     * WORKLISTED    5
     * @param handle 连接句柄
     * @param pid 用户的ParticipantId
     * @param queueType 要检索的队列的类型
     * @param outResult [out] 工作列表的XML表达
     * @return 操作是否成功
     */
    boolean GetWorkQueueByPid(String handle, String pid, String queueType, StringBuilder outResult);

    /**
     * 用户接受一个工作项
     * @param handle 连接句柄
     * @param pid 用户的ParticipantId
     * @param itemId 工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean AcceptOfferItemByPid(String handle, String pid, String itemId, StringBuilder outResult);

    /**
     * 让一个用户开始一个工作项
     * @param handle 连接句柄
     * @param pid 用户ParticipantId
     * @param itemId 工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean StartItemByPid(String handle, String pid, String itemId, StringBuilder outResult);

    /**
     * 取消一个用户的一个工作项
     * @param handle 连接句柄
     * @param pid 用户ParticipantId
     * @param itemId 工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean DeallocateByPid(String handle, String pid, String itemId, StringBuilder outResult);

    /**
     * 重分派一个用户的一个工作项
     * @param handle 连接句柄
     * @param pid 用户ParticipantId
     * @param itemId 工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean ReallocateByPid(String handle, String pid, String itemId, StringBuilder outResult);

    /**
     * 保存一个工作项参数的编辑
     * @param handle 连接句柄
     * @param pid 用户ParticipantId
     * @param itemId 工作项的Id
     * @param dataString 更新时刻的字符串
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean UpdateByPid(String handle, String pid, String itemId, String dataString, StringBuilder outResult);

    /**
     * 更新一个用户的一个工作项
     * @param handle 连接句柄
     * @param pid 用户ParticipantId
     * @param itemId 工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean CompleteByPid(String handle, String pid, String itemId, StringBuilder outResult);

    /**
     * 跳过一个用户的一个工作项
     * @param handle 连接句柄
     * @param pid 用户ParticipantId
     * @param itemId 工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean SkipByPid(String handle, String pid, String itemId, StringBuilder outResult);

    /**
     * 将一个工作项从一个用户委派给另一个用户
     * @param handle 连接句柄
     * @param pidFrom 用户ParticipantId
     * @param pidTo 用户ParticipantId
     * @param itemId 工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean DelegateByPid(String handle, String pidFrom, String pidTo, String itemId, StringBuilder outResult);

    /**
     * 工作项批处理
     * @param handle 连接句柄
     * @param pid 用户ParticipantId
     * @param itemId 工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean PileByPid(String handle, String pid, String itemId, StringBuilder outResult);

    /**
     * 挂起一个用户的一个工作项
     * @param handle 连接句柄
     * @param pid 用户ParticipantId
     * @param itemId 工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean SuspendByPid(String handle, String pid, String itemId, StringBuilder outResult);

    /**
     * 解除一个用户的一个工作项的挂起状态
     * @param handle 连接句柄
     * @param pid 用户ParticipantId
     * @param itemId 工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    boolean UnsuspendByPid(String handle, String pid, String itemId, StringBuilder outResult);

    /**
     * 由一个工作项的id获取一个工作项
     * @param handle 连接句柄
     * @param itemId 工作项的Id
     * @param outResult [out] 工作项的XML表达
     * @return 操作是否成功
     */
    boolean GetWorkItemByIid(String handle, String itemId, StringBuilder outResult);

    /**
     * 由一个工作项的id获取一个工作项的参数列表
     * @param handle 连接句柄
     * @param itemId 工作项的Id
     * @param outResult [out] 工作项参数列表的XML表达
     * @return 操作是否成功
     */
    boolean GetWorkItemParametersByIid(String handle, String itemId, StringBuilder outResult);

    /**
     * 由一个工作项的id获取一个工作项的数据模式
     * @param handle 连接句柄
     * @param itemId 工作项的Id
     * @param outResult [out] 工作项数据模式的字符串表达
     * @return 操作是否成功
     */
    boolean GetWorkItemDataSchemaByIid(String handle, String itemId, StringBuilder outResult);
}
