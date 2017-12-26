//package com.sysu.workflow.database;
//
//import com.sysu.workflow.service.indentityservice.IdentityService;
//import org.hibernate.cfg.Configuration;
//import org.hibernate.tool.hbm2ddl.SchemaExport;
//import org.junit.Before;
//import org.junit.Test;
//
///**
// * Created with IntelliJ IDEA
// * Date: 2016/1/16
// * Time: 16:52
// * User: ThinerZQ
// * GitHub: <a>https://github.com/ThinerZQ</a>
// * Blog: <a>http://blog.csdn.net/c601097836</a>
// * Email: 601097836@qq.com
// */
//public class TableTest {
//
//    private IdentityService identityService = null;
//
//    @Before
//    public void before(){
//        identityService = new IdentityService();
//    }
//
//    @Test
//    public void testCreateTable() {
//
//        Configuration config = new Configuration().configure();
//        SchemaExport schema = new SchemaExport(config);
//        schema.setFormat(true).create(true, true);
//    }
//
//
//    public void testAddUser2() {
//
//    }
//}
