package com.onemeter.entity;

/**
 * 描述：获取学员合同实体类
 * 项目名称：CrmWei
 * 作者：Administrator
 * 时间：2016/5/6 18:11
 * 备注：
 */
public class StudentHetongInfo {
    private  String  heTongNum;//合同编号
    private  String  courseName;//课程
    private  String  className;//班级
    private  String  lessonNum;//课时数
    private  String  total;//总价
    private  String  refundMoney;//退款

    public String getHeTongNum() {
        return heTongNum;
    }

    public void setHeTongNum(String heTongNum) {
        this.heTongNum = heTongNum;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(String refundMoney) {
        this.refundMoney = refundMoney;
    }

    public String getLessonNum() {
        return lessonNum;
    }

    public void setLessonNum(String lessonNum) {
        this.lessonNum = lessonNum;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
