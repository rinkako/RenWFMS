package com.sysu.workflow.service.indentityservice;

import com.sysu.workflow.database.DBUtils;
import com.sysu.workflow.entity.GroupEntity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA
 * Date: 2015/12/16
 * Time: 13:17
 * User: ThinerZQ
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://blog.csdn.net/c601097836</a>
 * Email: 601097836@qq.com
 *
 */
public class GroupDao {

    public boolean addGroup(GroupEntity groupEntity) {
        Session session = null;
        try {
            session =DBUtils.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            session.save(groupEntity);

            session.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //DBUtils.closeSession(session);
        }
        return true;
    }

    public ArrayList<GroupEntity> findGroup(GroupEntity groupEntity) {
        Session session = null;

        try {
            session = DBUtils.getSessionFactory().getCurrentSession();

            if (groupEntity.getGroupId() != 0) {
                return findGroupByGroupId(String.valueOf(groupEntity.getGroupId()));
            } else if (groupEntity.getGroupName() != null) {
                return findGroupByGroupName(groupEntity.getGroupName());
            } else {
                //TODO:�������ַ��ʷ�ʽ
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //DBUtils.closeSession(session);
        }
        return null;
    }

    public ArrayList<GroupEntity> findGroupByGroupName(String groupName) {

        Session session = null;
        ArrayList<GroupEntity> groupEntityArrayList = new ArrayList<GroupEntity>();
        try {
            session = DBUtils.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(GroupEntity.class);

            Criterion groupNameCriterion = Restrictions.eq("groupName", groupName);
            criteria.add(groupNameCriterion);

            groupEntityArrayList = (ArrayList<GroupEntity>) criteria.list();

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //DBUtils.closeSession(session);
        }
        return groupEntityArrayList;

    }

    public ArrayList<GroupEntity> findGroupByGroupId(String groupId) {
        Session session = null;
        ArrayList<GroupEntity> groupEntityArrayList = new ArrayList<GroupEntity>();
        try {
            session = DBUtils.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(GroupEntity.class);

            Criterion groupIdCriterion = Restrictions.eq("groupId", groupId);
            criteria.add(groupIdCriterion);

            groupEntityArrayList = (ArrayList<GroupEntity>) criteria.list();

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //DBUtils.closeSession(session);
        }
        return groupEntityArrayList;
    }

    public boolean updateGroup(GroupEntity groupEntity) {
        return false;
    }

    public boolean deleteGroup(GroupEntity groupEntity) {
        return false;
    }

    public boolean deleteGroupById(String groupId) {
        return false;
    }


}
