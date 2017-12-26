package com.sysu.workflow.service.formservice;

import com.sysu.workflow.database.DBUtils;
import com.sysu.workflow.entity.FormEntity;
import com.sysu.workflow.entity.UserEntity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA
 * Date: 2016/1/22
 * Time: 13:38
 * User: ThinerZQ
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://blog.csdn.net/c601097836</a>
 * Email: 601097836@qq.com
 */
public class FormDao {

    public ArrayList<FormEntity> findForm(FormEntity formEntity) {
        Session session = null;
        ArrayList<FormEntity> formEntityArrayList = new ArrayList<FormEntity>();
        try {
            session = DBUtils.getSessionFactory().getCurrentSession();
            Criteria criteria = session.createCriteria(UserEntity.class);

            Criterion userCriterion = Restrictions.allEq(formEntity.getNotNullPropertyMap());
            criteria.add(userCriterion);

            formEntityArrayList = (ArrayList<FormEntity>) criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //DBUtils.closeSession(session);
        }
        return formEntityArrayList;
    }
}
