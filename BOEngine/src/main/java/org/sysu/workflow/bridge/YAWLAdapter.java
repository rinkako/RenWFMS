package org.sysu.workflow.bridge;

//import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rinkako on 2017/6/15.
 */
public class YAWLAdapter extends EngineClient implements IRouterAdapter {

    private static YAWLAdapter syncObject = new YAWLAdapter();

    private static final String _serviceURI = "http://localhost:8080/resourceService/gateway";

    private YAWLAdapter() { }

    public static YAWLAdapter GetInstance() { return YAWLAdapter.syncObject; }

    public static String SessionHandle;

    /**
     * Initialises a map for transporting parameters - used by extending classes
     * @param action the name of the action to take
     * @param handle the current engine session handle
     * @return the initialised Map
     */
    public Map<String, String> prepareParamMap(String action, String handle) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("action", action);
        if (handle != null) paramMap.put("sessionHandle", handle) ;
        return paramMap;
    }

    /**
     * 加密指定的串
     * @param text 要加密的串
     * @return 加密完毕的串
     */
    private static synchronized String encrypt(String text)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(text.getBytes("UTF-8"));
        byte raw[] = md.digest();
        //return new Base64(-1).encodeToString(raw); // -1 means no line breaks
        return "";
    }

    /**
     * 加密指定的串
     * @param text 要加密的串
     * @param defText 失败返回的串
     * @return 加密完毕的串
     */
    private static synchronized String encrypt(String text, String defText) {
        if (defText == null) defText = text;
        try {
            return encrypt(text);
        }
        catch (Exception e) {
            return defText;
        }
    }

    /**
     * 连接SCXML引擎到消息处理路由
     *
     * @param username  登录路由服务的用户名
     * @param password  登录路由服务的密码串（路由可能要求发送已加密的串）
     * @param outResult [out] 登录结果
     * @return 操作是否成功
     */
    @Override
    public boolean ConnectToRouter(String username, String password, StringBuilder outResult) {
        try {
            Map<String, String> params = prepareParamMap("connect", null);
            params.put("userid", username);
            params.put("password", this.encrypt(password, null));
            outResult.append(executeGet(_serviceURI, params));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取当前是否成功连接到消息路由
     *
     * @param handle    连接句柄
     * @param outResult [out] 是否连接到路由
     * @return 操作是否成功
     */
    @Override
    public boolean CheckConnectToRouter(String handle, StringBuilder outResult) {
        try {
            outResult.append(executeGet(_serviceURI, prepareParamMap("checkConnection", handle)));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 断开SCXML引擎到消息路由的连接
     *
     * @param handle    连接句柄
     * @param outResult [out] 断开的结果
     * @return 操作是否成功
     */
    @Override
    public boolean DisconnectFromRouter(String handle, StringBuilder outResult) {
        try {
            outResult.append(executePost(_serviceURI, prepareParamMap("disconnect", handle)));
            YAWLAdapter.SessionHandle = null;
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 添加一个参与者
     *
     * @param handle      连接句柄
     * @param userid      用户名
     * @param password    密码明文
     * @param encrypt     密码是否加密保存
     * @param lastname    姓
     * @param firstname   名
     * @param admin       是否为管理员
     * @param description 描述文本
     * @param notes       备注文本
     * @param outResult   [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean addParticipant(String handle, String userid, String password, boolean encrypt, String lastname, String firstname, boolean admin, String description, String notes, StringBuilder outResult) {
        try {
            Map<String, String> params = prepareParamMap("addParticipant", handle);
            params.put("userid", userid);
            params.put("password", password);
            params.put("encrypt", String.valueOf(encrypt));
            params.put("lastname", lastname);
            params.put("firstname", firstname);
            params.put("admin", String.valueOf(admin));
            params.put("description", description);
            params.put("notes", notes);
            outResult.append(executeGet(_serviceURI, params));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 添加一个非人资源
     *
     * @param handle      连接句柄
     * @param name        名字
     * @param category    目录
     * @param subcategory 子目录
     * @param description 描述文本
     * @param notes       备注文本
     * @param outResult   [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean addNonHumanResource(String handle, String name, String category, String subcategory, String description, String notes, StringBuilder outResult) {
        try {
            Map<String, String> params = prepareParamMap("addNonHumanResource", handle);
            params.put("name", name);
            params.put("category", category);
            params.put("subcategory", subcategory);
            params.put("description", description);
            params.put("notes", notes);
            outResult.append(executeGet(_serviceURI, params));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 添加一个角色
     *
     * @param handle           连接句柄
     * @param name             角色名字
     * @param description      描述文本
     * @param notes            备注文本
     * @param containingRoleID 包含角色id
     * @param outResult        [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean addRole(String handle, String name, String description, String notes, String containingRoleID, StringBuilder outResult) {
        try {
            Map<String, String> params = prepareParamMap("addRole", handle);
            params.put("name", name);
            params.put("description", description);
            params.put("notes", notes);
            params.put("containingroleid", containingRoleID);
            outResult.append(executeGet(_serviceURI, params));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 将一个参与者添加到角色
     *
     * @param handle        连接句柄
     * @param participantID 参与者Pid
     * @param roleID        角色id
     * @param outResult     [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean addParticipantToRole(String handle, String participantID, String roleID, StringBuilder outResult) {
        try {
            Map<String, String> params = prepareParamMap("addParticipantToRole", handle);
            params.put("participantid", participantID);
            params.put("roleid", roleID);
            outResult.append(executeGet(_serviceURI, params));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 添加一个非人力资源目录
     *
     * @param handle    连接句柄
     * @param category  目录名
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean addNonHumanCategory(String handle, String category, StringBuilder outResult) {
        try {
            Map<String, String> params = prepareParamMap("addNonHumanCategory", handle);
            params.put("category", category);
            outResult.append(executeGet(_serviceURI, params));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 通过用户唯一标识符获取用户的ParticipantId
     *
     * @param handle    连接句柄
     * @param username  要检索Pid的用户名
     * @param outResult [out] 该用户的Pid
     * @return 操作是否成功
     */
    @Override
    public boolean GetParticipantFromUserID(String handle, String username, StringBuilder outResult) {
        return false;
    }

    /**
     * 上传一个过程描述文件
     *
     * @param handle    连接句柄
     * @param filePath  过程描述文件在本地的绝对路径（含文件名）
     * @param fileName  过程描述文件的文件名
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean UploadSpecificationFile(String handle, String filePath, String fileName, StringBuilder outResult) {
        return false;
    }

    /**
     * 卸载一个过程
     * 关于第2到第4个参数的意义，请参考YSpecificationID的toMap方法
     *
     * @param handle         连接句柄
     * @param specidentifier a system generated UUID string
     * @param specversion    verion
     * @param specuri        the user-defined name
     * @param outResult      [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean UnloadSpecification(String handle, String specidentifier, String specversion, String specuri, StringBuilder outResult) {
        return false;
    }

    /**
     * 获取当前加载的过程
     *
     * @param handle    连接句柄
     * @param outResult [out] 当前加载的过程集合的串表达
     * @return 操作是否成功
     */
    @Override
    public boolean GetLoadedSpecificationList(String handle, StringBuilder outResult) {
        return false;
    }

    /**
     * 获取一个过程的数据
     * 关于第2到第4个参数的意义，请参考YSpecificationID的toMap方法
     *
     * @param handle         连接句柄
     * @param specidentifier a system generated UUID string
     * @param specversion    verion
     * @param specuri        the user-defined name
     * @param outResult      [out] 数据的XML格式串
     * @return 操作是否成功
     */
    @Override
    public boolean GetSpecificationData(String handle, String specidentifier, String specversion, String specuri, StringBuilder outResult) {
        return false;
    }

    /**
     * 获取一个过程的名称
     * 关于第2到第4个参数的意义，请参考YSpecificationID的toMap方法
     *
     * @param handle         连接句柄
     * @param specidentifier a system generated UUID string
     * @param specversion    verion
     * @param specuri        the user-defined name
     * @param outResult      [out] 过程名称
     * @return 操作是否成功
     */
    @Override
    public boolean GetSpecificationName(String handle, String specidentifier, String specversion, String specuri, StringBuilder outResult) {
        return false;
    }

    /**
     * 启动一个已经上传的过程描述文件
     * 关于第2到第4个参数的意义，请参考YSpecificationID的toMap方法
     *
     * @param handle         连接句柄
     * @param specidentifier a system generated UUID string
     * @param specversion    verion
     * @param specuri        the user-defined name
     * @param outResult      [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean launchCase(String handle, String specidentifier, String specversion, String specuri, StringBuilder outResult) {
        return false;
    }

    /**
     * 获取指定流程的所有运行中的案例
     * 关于第2到第4个参数的意义，请参考YSpecificationID的toMap方法
     *
     * @param handle         连接句柄
     * @param specidentifier a system generated UUID string
     * @param specversion    verion
     * @param specuri        the user-defined name
     * @param outResult      [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean GetRunningCases(String handle, String specidentifier, String specversion, String specuri, StringBuilder outResult) {
        return false;
    }

    /**
     * 取消一个Case
     *
     * @param handle    连接句柄
     * @param caseId    Case的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean CancelCase(String handle, String caseId, StringBuilder outResult) {
        return false;
    }

    /**
     * 通过ParticipantId获取该人物各类型的工作列表
     * UNDEFINED     -1
     * OFFERED       0
     * ALLOCATED     1
     * STARTED       2
     * SUSPENDED     3
     * UNOFFERED     4
     * WORKLISTED    5
     *
     * @param handle    连接句柄
     * @param pid       用户的ParticipantId
     * @param queueType 要检索的队列的类型
     * @param outResult [out] 工作列表的XML表达
     * @return 操作是否成功
     */
    @Override
    public boolean GetWorkQueueByPid(String handle, String pid, String queueType, StringBuilder outResult) {
        return false;
    }

    /**
     * 用户接受一个工作项
     *
     * @param handle    连接句柄
     * @param pid       用户的ParticipantId
     * @param itemId    工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean AcceptOfferItemByPid(String handle, String pid, String itemId, StringBuilder outResult) {
        return false;
    }

    /**
     * 让一个用户开始一个工作项
     *
     * @param handle    连接句柄
     * @param pid       用户ParticipantId
     * @param itemId    工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean StartItemByPid(String handle, String pid, String itemId, StringBuilder outResult) {
        return false;
    }

    /**
     * 取消一个用户的一个工作项
     *
     * @param handle    连接句柄
     * @param pid       用户ParticipantId
     * @param itemId    工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean DeallocateByPid(String handle, String pid, String itemId, StringBuilder outResult) {
        return false;
    }

    /**
     * 重分派一个用户的一个工作项
     *
     * @param handle    连接句柄
     * @param pid       用户ParticipantId
     * @param itemId    工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean ReallocateByPid(String handle, String pid, String itemId, StringBuilder outResult) {
        return false;
    }

    /**
     * 保存一个工作项参数的编辑
     *
     * @param handle     连接句柄
     * @param pid        用户ParticipantId
     * @param itemId     工作项的Id
     * @param dataString 更新时刻的字符串
     * @param outResult  [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean UpdateByPid(String handle, String pid, String itemId, String dataString, StringBuilder outResult) {
        return false;
    }

    /**
     * 更新一个用户的一个工作项
     *
     * @param handle    连接句柄
     * @param pid       用户ParticipantId
     * @param itemId    工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean CompleteByPid(String handle, String pid, String itemId, StringBuilder outResult) {
        return false;
    }

    /**
     * 跳过一个用户的一个工作项
     *
     * @param handle    连接句柄
     * @param pid       用户ParticipantId
     * @param itemId    工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean SkipByPid(String handle, String pid, String itemId, StringBuilder outResult) {
        return false;
    }

    /**
     * 将一个工作项从一个用户委派给另一个用户
     *
     * @param handle    连接句柄
     * @param pidFrom   用户ParticipantId
     * @param pidTo     用户ParticipantId
     * @param itemId    工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean DelegateByPid(String handle, String pidFrom, String pidTo, String itemId, StringBuilder outResult) {
        return false;
    }

    /**
     * 工作项批处理
     *
     * @param handle    连接句柄
     * @param pid       用户ParticipantId
     * @param itemId    工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean PileByPid(String handle, String pid, String itemId, StringBuilder outResult) {
        return false;
    }

    /**
     * 挂起一个用户的一个工作项
     *
     * @param handle    连接句柄
     * @param pid       用户ParticipantId
     * @param itemId    工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean SuspendByPid(String handle, String pid, String itemId, StringBuilder outResult) {
        return false;
    }

    /**
     * 解除一个用户的一个工作项的挂起状态
     *
     * @param handle    连接句柄
     * @param pid       用户ParticipantId
     * @param itemId    工作项的Id
     * @param outResult [out] 执行结果
     * @return 操作是否成功
     */
    @Override
    public boolean UnsuspendByPid(String handle, String pid, String itemId, StringBuilder outResult) {
        return false;
    }

    /**
     * 由一个工作项的id获取一个工作项
     *
     * @param handle    连接句柄
     * @param itemId    工作项的Id
     * @param outResult [out] 工作项的XML表达
     * @return 操作是否成功
     */
    @Override
    public boolean GetWorkItemByIid(String handle, String itemId, StringBuilder outResult) {
        return false;
    }

    /**
     * 由一个工作项的id获取一个工作项的参数列表
     *
     * @param handle    连接句柄
     * @param itemId    工作项的Id
     * @param outResult [out] 工作项参数列表的XML表达
     * @return 操作是否成功
     */
    @Override
    public boolean GetWorkItemParametersByIid(String handle, String itemId, StringBuilder outResult) {
        return false;
    }

    /**
     * 由一个工作项的id获取一个工作项的数据模式
     *
     * @param handle    连接句柄
     * @param itemId    工作项的Id
     * @param outResult [out] 工作项数据模式的字符串表达
     * @return 操作是否成功
     */
    @Override
    public boolean GetWorkItemDataSchemaByIid(String handle, String itemId, StringBuilder outResult) {
        return false;
    }
}
