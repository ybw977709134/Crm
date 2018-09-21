package com.onemeter.entity;

/**
 * 描述：学员列表实体类
 * 项目名称：CrmWei
 * 作者：Administrator
 * 时间：2016/5/4 21:35
 * 备注：
 */
public class StudentInfo {
    private  String student_id;
    private  String  name;
    private  String  card;
    private  String  nclass;
    private  String  phone;
    private  String  contacts;
    private  String  school;

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getNclass() {
        return nclass;
    }

    public void setNclass(String nclass) {
        this.nclass = nclass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
