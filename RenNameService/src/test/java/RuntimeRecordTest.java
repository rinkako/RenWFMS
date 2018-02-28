import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.sysu.renNameService.RenNameServiceApplication;
import org.sysu.renNameService.entity.RenRuntimerecordEntity;
import org.sysu.renCommon.utility.TimestampUtil;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RenNameServiceApplication.class)
@WebAppConfiguration
public class RuntimeRecordTest {
    private Session session = null;
    private Transaction tx = null;
    private String testId = "622649d6-c422-49d3-b24e-5c8451830f9d";

    @Autowired
    private SessionFactory sessionFactory;

    @Before
    public void init() {
//        Configuration config = new Configuration().configure();
//        SessionFactory sessionFactory = config.buildSessionFactory();
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
    }
    //增加
    @Test
    public void insert() {
        RenRuntimerecordEntity ue = new RenRuntimerecordEntity();
        ue.setRtid(testId);
        ue.setLaunchTimestamp(TimestampUtil.GetCurrentTimestamp());
        ue.setLaunchFrom("insert func");
        session.save(ue);
        tx.commit();
    }
    //修改
    @Test
    public void update() {
        RenRuntimerecordEntity user = (RenRuntimerecordEntity)session.get(RenRuntimerecordEntity.class, testId);
        user.setLaunchFrom("update func");
        session.update(user);
        tx.commit();
        session.close();
    }
    //查找
    @Test
    public void getById() {
        RenRuntimerecordEntity user = (RenRuntimerecordEntity) session.get(RenRuntimerecordEntity.class, testId);
        tx.commit();
        session.close();
        System.out.println("ID号：" + user.getRtid() + "；Launch from：" + user.getLaunchFrom());
    }
    //删除
    @Test
    public void delete() {
        RenRuntimerecordEntity user = (RenRuntimerecordEntity) session.get(RenRuntimerecordEntity.class, testId);
        session.delete(user);
        tx.commit();
        session.close();
    }
}