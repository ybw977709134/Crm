package com.onemeter.entity;

/**班级成员列表实体类
 * Created by G510 on 2016/5/7.
 */
public class classDetailInfo {
    private  String  studentId;//学员id
    private  String  name;//学员姓名
    private  String  ctime;//日期
    private  String  hours;//课时数
    private  String  total;//总价

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
