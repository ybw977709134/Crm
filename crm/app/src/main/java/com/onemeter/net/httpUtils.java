package com.onemeter.net;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.onemeter.activity.AddStudentActivity;
import com.onemeter.activity.ClassDetailActivity;
import com.onemeter.activity.UserLoginActivity;
import com.onemeter.fragment.ClassFragment;
import com.onemeter.fragment.CourseFragment;
import com.onemeter.fragment.DongTaiFragment;
import com.onemeter.fragment.HeTongFragment;
import com.onemeter.fragment.StudentDetailFragment;
import com.onemeter.fragment.StudentFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 描述：网络请求工具类（原生方法）
 * 项目名称：CrmWei
 * 作者：Administrator
 * 时间：2016/5/5 16:12
 * 备注：
 */
public class httpUtils {
    Handler mHandler = new Handler();
    Context context;

    public httpUtils(Context context) {
        this.context = context;
    }

    /**
     * 发送请求获取数据的方法
     *
     * @param apiUrl
     * @param apiid
     * @param apiKey
     * @param datas
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */

    public String getData(String apiUrl, String apiid, String apiKey, Map<String, String> datas, final Object observer, final String action) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        String apitime = Long.toString(System.currentTimeMillis() / 1000);
        String data = getMessage(apiid, apitime, datas);
        String Apihash = getHmac(data, apiKey);
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        conn.setRequestProperty("apiid", apiid);
        conn.setRequestProperty("apitime", apitime);
        conn.setRequestProperty("Apihash", Apihash);

        PrintWriter output = new PrintWriter(conn.getOutputStream());
        output.print(http_build_query(datas));
        output.flush();
        output.close();

        String line;
        final StringBuilder sbCache = new StringBuilder(1000);
        Log.i("log", "sbCache");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        while ((line = in.readLine()) != null) {
            sbCache.append(line);
            sbCache.append("\n");
        }

        in.close();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                handlePostReslut(sbCache.toString(), observer, action);
            }
        });

        return sbCache.toString();
    }


    /**
     * 获得getMessage（）的方法
     *
     * @param apiid
     * @param apitime
     * @param datas
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getMessage(String apiid, String apitime, Map<String, String> datas) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(200);
        sb.append(apitime);
        sb.append(apiid);
        sb.append(http_build_query(datas));

        return sb.toString();
    }


    /**
     * 生成http_build_query（）的方法
     *
     * @param datas
     * @return
     * @throws UnsupportedEncodingException
     */
    private String http_build_query(Map<String, String> datas) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(200);
        for (Map.Entry<String, String> en : datas.entrySet()) {
            if (sb.length() > 0)
                sb.append("&");
            sb.append(en.getKey());
            sb.append("=");
            sb.append(java.net.URLEncoder.encode(en.getValue(), "utf-8"));
        }
        return sb.toString();
    }

    /**
     * 生成Apihash 的方法
     *
     * @param value
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public String getHmac(String value, String key) throws NoSuchAlgorithmException, InvalidKeyException {//
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");//HmacMD5
        Mac mac = Mac.getInstance("HmacSHA256");//HmacMD5
        mac.init(keySpec);
        byte[] hashBytes = mac.doFinal(value.getBytes());
        return Bytes2HexString(hashBytes).toLowerCase();
    }


    public String Bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    /**
     * 根据请求代码（即请求的方法）来处理Post、get等请求服务器返回的结果
     *
     * @param
     */

    private void handlePostReslut(String result, Object observer, String action) {
        if (action.equals("登入")) {
            if (observer instanceof UserLoginActivity) {
                ((UserLoginActivity) observer).onCompleted(result, action);
            }
        }

        if (action.equals("获取学员列表信息")) {
            if (observer instanceof StudentFragment) {
                ((StudentFragment) observer).onCompleted(result, action);
            }
        }

        if (action.equals("获取合同信息")) {
            if (observer instanceof HeTongFragment) {
                ((HeTongFragment) observer).onCompleted(result, action);
            }
        }

        if (action.equals("获取联系动态信息")) {
            if (observer instanceof DongTaiFragment) {
                ((DongTaiFragment) observer).onCompleted(result, action);
            }
        }

        if (action.equals("获取指定学员详细信息")) {
            if (observer instanceof StudentDetailFragment) {
                ((StudentDetailFragment) observer).onCompleted(result, action);
            }
        }
        if (action.equals("修改学员详情信息")) {
            if (observer instanceof StudentDetailFragment) {
                ((StudentDetailFragment) observer).onCompleted(result, action);
            }
        }

        if (action.equals("添加学员详情信息")) {
            if (observer instanceof AddStudentActivity) {
                ((AddStudentActivity) observer).onCompleted(result, action);
            }
        }
        if (action.equals("获取课程列表信息")) {
            if (observer instanceof CourseFragment) {
                ((CourseFragment) observer).onCompleted(result, action);
            }
        }

        if (action.equals("获取班级列表信息")) {
            if (observer instanceof ClassFragment) {
                ((ClassFragment) observer).onCompleted(result, action);
            }
        }

        if (action.equals("获取班级详情信息")) {
            if (observer instanceof ClassDetailActivity) {
                ((ClassDetailActivity) observer).onCompleted(result, action);
            }
        }
        if (action.equals("更新班级列表信息")) {
            if (observer instanceof ClassFragment) {
                ((ClassFragment) observer).onCompleted(result, action);
            }

        }
    }
}
