package com.onemeter.entity;

/**
 * 班级列表信息实体类
 */
public class ClasslistInfo {
    private String classId;//班级id
   private  String name;//班级名称
    private String course;//课程
    private String type;//班级类型
    private String purchasedNum;//已报人数
    private String maxStudents;//最大人数
    private String status;//状态
    private String startdate;//开始日期

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPurchasedNum() {
        return purchasedNum;
    }

    public void setPurchasedNum(String purchasedNum) {
        this.purchasedNum = purchasedNum;
    }

    public String getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(String maxStudents) {
        this.maxStudents = maxStudents;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
