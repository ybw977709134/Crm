package com.onemeter.entity;

/**
 * 描述：课程分类实体类
 * 项目名称：CrmWei
 * 作者：Administrator
 * 时间：2016/5/10 15:56
 * 备注：
 */
public class courseType {
    private  String courseId;//课程iD
    private  String courseName;//课程名称

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
