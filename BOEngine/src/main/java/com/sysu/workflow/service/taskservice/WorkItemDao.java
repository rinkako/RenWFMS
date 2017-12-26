package com.sysu.workflow.service.taskservice;

import com.sysu.workflow.database.DBUtils;
import com.sysu.workflow.entity.GroupWorkItemEntity;
import com.sysu.workflow.entity.UserWorkItemEntity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA
 * Date: 2015/12/16
 * Time: 13:16
 * User: ThinerZQ
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://blog.csdn.net/c601097836</a>
 * Email: 601097836@qq.com
 *
 */
public class WorkItemDao {

    public long insertIntoWorkItem(UserWorkItemEntity workItemEntity) {

        Session session = null;
        long id = -1;
        try {
            session =DBUtils.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            session.flush();
            session.merge(workItemEntity);
            session.getTransaction().commit();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
           // DBUtils.closeSession(session);
        }
        return id;
    }

    public long insertIntoWorkItem(GroupWorkItemEntity groupWorkItemEntity) {

        Session session = null;
        long id = -1;
        try {
            session = DBUtils.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            session.merge(groupWorkItemEntity);

            session.getTransaction().commit();
            id=0;
        } catch (Exception e) {
            e.printStackTrace();
            id=-1;
        } finally {
            //DBUtils.closeSession(session);
        }
        return id;
    }

    public ArrayList<UserWorkItemEntity> findUserWorkItem(UserWorkItemEntity userWorkItemEntity) {
        Session session = null;
        ArrayList<UserWorkItemEntity> workItemEntityArrayList = new ArrayList<UserWorkItemEntity>();
        try {
            session = DBUtils.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(UserWorkItemEntity.class);


            Criterion allCriterion = Restrictions.allEq(userWorkItemEntity.getNotNullPropertyMap());

            criteria.add(allCriterion);
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            workItemEntityArrayList = (ArrayList<UserWorkItemEntity>) criteria.list();
            //System.out.println(workItemEntityArrayList.size());

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //DBUtils.closeSession(session);
        }
        return workItemEntityArrayList;
    }

    public ArrayList<GroupWorkItemEntity> findGroupWorkItem(GroupWorkItemEntity groupWorkItemEntity) {
        Session session = null;
        ArrayList<GroupWorkItemEntity> groupWorkItemEntityArrayList = new ArrayList<GroupWorkItemEntity>();
        try {
            session = DBUtils.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(GroupWorkItemEntity.class);

            Criterion allCriterion = Restrictions.allEq(groupWorkItemEntity.getNotNullPropertyMap());

            criteria.add(allCriterion);
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            groupWorkItemEntityArrayList = (ArrayList<GroupWorkItemEntity>) criteria.list();
            //System.out.println(workItemEntityArrayList.size());

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           // DBUtils.closeSession(session);
        }
        return groupWorkItemEntityArrayList;
    }

    public boolean updateUserWorkItem(UserWorkItemEntity userWorkItemEntity) {
        Session session = null;
        boolean flag = false;
        try {
            session = DBUtils.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.update(userWorkItemEntity);
            session.getTransaction().commit();
            flag =true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //DBUtils.closeSession(session);
        }
        return flag;
    }

    public boolean updateGroupWorkItem(GroupWorkItemEntity groupWorkItemEntity) {
        Session session = null;
        boolean flag = false;
        try {
            session = DBUtils.getSessionFactory().getCurrentSession();
            session.beginTransaction();


            session.merge(groupWorkItemEntity);
            //session.update(groupWorkItemEntity);
            session.getTransaction().commit();
            flag =true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           // DBUtils.closeSession(session);
        }
        return flag;
    }
}
