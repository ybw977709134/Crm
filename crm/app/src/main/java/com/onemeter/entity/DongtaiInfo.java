package com.onemeter.entity;

/**
 * 描述：获取联系动态实体类
 * 项目名称：CrmWei
 * 作者：john
 * 时间：2016/6/30
 * 备注：
 */
public class DongtaiInfo {
    private  String  dongtaiDate;//日期
    private  String  dongtaiAction;//动作
    private  String  dongtaiContext;//具体内容
    private  String  dongtaiSalesman;//业务员

    public String getDongtaiDate() {
        return dongtaiDate;
    }

    public void setDongtaiDate(String dongtaiDate) {
        this.dongtaiDate = dongtaiDate;
    }

    public String getDongtaiAction() {
        return dongtaiAction;
    }

    public void setDongtaiAction(String dongtaiAction) {
        this.dongtaiAction = dongtaiAction;
    }

    public String getDongtaiContext() {
        return dongtaiContext;
    }

    public void setDongtaiContext(String dongtaiContext) {
        this.dongtaiContext = dongtaiContext;
    }

    public String getDongtaiSalesman() {
        return dongtaiSalesman;
    }

    public void setDongtaiSalesman(String dongtaiSalesman) {
        this.dongtaiSalesman = dongtaiSalesman;
    }
}
