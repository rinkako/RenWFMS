import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import org.sysu.renNameService.entity.RenRuntimerecordEntity;

import java.sql.Timestamp;
import java.util.Date;

public class RuntimeRecordTest {
    private Session session = null;
    private Transaction tx = null;
    private String testId = "622649d6-c422-49d3-b24e-5c8451830f9d";

    @Before
    public void init() {
        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
    }
    //增加
    @Test
    public void insert() {
        RenRuntimerecordEntity ue = new RenRuntimerecordEntity();
        ue.setRtid(testId);
        ue.setLaunchTimestamp(new Timestamp((new Date()).getTime()));
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