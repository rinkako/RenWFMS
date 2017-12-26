package com.sysu.workflow.service.formservice;

import com.sysu.workflow.entity.FormEntity;

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
public class FormQuery {
    private FormDao formDao;

    private FormEntity formEntity;

    public FormQuery(FormEntity formEntity) {
        this.formEntity = formEntity;
    }

    public FormQuery() {
        this.formEntity = new FormEntity();
    }

    public FormQuery formSrc(String formSrc) {
        formEntity.setFormSrc(formSrc);
        return this;
    }

    public FormQuery formId(long formId) {
        formEntity.setFormId(formId);
        return this;
    }

    public FormQuery formName(String formName) {
        formEntity.setFormName(formName);
        return this;
    }

    public FormEntity SingleResult() {
        if (formDao == null) {
            formDao = new FormDao();
        }
        ArrayList<FormEntity> arrayList = formDao.findForm(formEntity);
        int size = arrayList.size();
        return size >= 1 ? arrayList.get(0) : null;
    }

}
