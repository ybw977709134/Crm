package com.onemeter.entity;

import java.util.List;

/**课程列表信息实体类
 * Created by G510 on 2016/5/8.
 */
public class courseInfo {
    private String  name;//课程名称
    private String  imagePath;//图片路径
    public  List<new_courseInfo>  url;//图片的url地址
    private String  price;//单价
    private String  hours;//课时数
    private String  ctime;//创建时间
    private String  miaoshu;//课程介绍
    private String  bomgmingNum;//已经报名的人数
    private String  classNUm;//班级人数

    public List<new_courseInfo> getUrl() {
        return url;
    }

    public void setUrl(List<new_courseInfo> url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getMiaoshu() {
        return miaoshu;
    }

    public void setMiaoshu(String miaoshu) {
        this.miaoshu = miaoshu;
    }

    public String getBomgmingNum() {
        return bomgmingNum;
    }

    public void setBomgmingNum(String bomgmingNum) {
        this.bomgmingNum = bomgmingNum;
    }

    public String getClassNUm() {
        return classNUm;
    }

    public void setClassNUm(String classNUm) {
        this.classNUm = classNUm;
    }
}
