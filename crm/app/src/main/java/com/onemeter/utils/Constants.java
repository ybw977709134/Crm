package com.onemeter.utils;

/**
 * 描述：微秀api接口类
 * 作者：angelyin
 * 时间：2015/12/23 10:57
 * 备注：未标注的请求一律用get
 */
public class Constants {
    /**+
     * 服务器的的路径(测试服务器)
     **/
    public static String SERVER_UL = "http://dev01.onemeter.co:81/";
//    public static String SERVER_UL = "http://crm.migoedu.com/";
//    public static String SERVER_UL = "http://gaozhio2o.com/";
//    public static String SERVER_UL = "http://120.26.119.4/";
    /**
     * html5测试地址g
     **/
    public final static String API_GET_HTML5_ADRESS = "https://www.baidu.com/";
    /**
     * 登陆页面
     */
    public  final static String API_POST_LOGIN="api/login";

    /**
     * 查询学员列表信息
     *
     */
    public final static String API_POST_STUDENT_LIST="api/queryStudents";

    /**
     * 查询学员详情信息
     *
     */
    public final static String API_POST_STUDENT_DETAIL="api/studentDetail";



}
