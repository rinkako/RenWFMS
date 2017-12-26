package com.sysu.workflow;

import com.sysu.workflow.entity.UserEntity;
import com.sysu.workflow.service.indentityservice.IdentityService;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA
 * Date: 2016/1/22
 * Time: 17:12
 * User: ThinerZQ
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://blog.csdn.net/c601097836</a>
 * Email: 601097836@qq.com
 */
public class TestIdentityService {


    IdentityService identityService = null;

    @Before
    public void before() {
        identityService = new IdentityService();
    }

    @Test
    public void testFindTask() {
    /*    UserEntity userEntity = IdentityService.createUserQuery().userRealName("judger1").SingleResult();

        System.out.println(userEntity.toString());*/
    }
}
