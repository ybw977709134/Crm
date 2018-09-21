package com.onemeter.net;

import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.onemeter.utils.Constants;


/**
 * 和服务器进行数据交互的工具类
 *
 * @author angelyin
 * @date 2015-6-1 下午3:35:19
 */
public class NetUtil {
    private HttpUtils hu;

    public NetUtil(Context context) {
        hu = new HttpUtils(10000);
        hu.configCurrentHttpCacheExpiry(1000);
    }

    /**
     * 向服务期发送Post请求(此方法只能在UI线程中调用)
     *
     * @param observer 被观察者
     * @param params   请求参数
     * @param api      请求的接口
     */
    public void sendPostToServer(RequestParams params, final String api,
                                 final Object observer, String action) {
        send(params, api, observer, action, HttpMethod.POST);
    }

    public void send(RequestParams params, final String api,
                     final Object observer, final String action, HttpMethod method) {
        StringBuffer sb = new StringBuffer(Constants.SERVER_UL);
        sb.append(api);
        Log.i("Onemeter", "post,api:" + sb.toString());
        hu.send(method, sb.toString(), params, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                Log.i("Onemeter", "conn...");
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                if (isUploading) {
                    Log.i("Onemeter", "upload: " + current + "/" + total);
                } else {
                    Log.i("Onemeter", "reply: " + current + "/" + total);
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                handlePostReslut(api, responseInfo.result, true, observer,
                        action);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.i("Onemeter", error.getEntity() + ":" + msg);
                handlePostReslut(api, msg, false, observer, action);
            }
        });
    }

/***********************************************************************************************/
    /**
     * 向服务器发送get请求
     *
     * @param api
     * @param observer
     * @param action
     */
    public void sendGetToServer(final String api, final Object observer, String action) {
        sendd(api, observer, action, HttpMethod.GET);
    }

    public void sendd(final String api, final Object observer, final String action, HttpMethod method) {
        StringBuffer sb = new StringBuffer(Constants.SERVER_UL);
        sb.append(api);

        Log.i("Onemeter", sb.toString());
        hu.send(method, sb.toString(), new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                handlePostReslut(api, responseInfo.result, true, observer, action);
                Log.i("Onemeter", "成功：" + responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.i("Onemeter", error.getEntity() + ":" + msg);
                handlePostReslut(api, msg, false, observer, action);
            }
        });
    }

    /**
     * 根据请求代码（即请求的方法）来处理Post、get等请求服务器返回的结果
     *
     * @param api
     * @param result
     * @param isSuccess
     * @param observer
     */
    protected void handlePostReslut(String api, String result,
                                    boolean isSuccess, Object observer, String action) {
        // 将服务器返回的结果交给业务逻辑页处理
        Log.i("Onemeter", "api:" + api + "\nresult:" + result);



    }

}
