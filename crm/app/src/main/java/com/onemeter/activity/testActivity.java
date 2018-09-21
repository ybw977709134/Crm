package com.onemeter.activity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.view.MyWebView;

/**
 * 描述：
 * 项目名称：crm
 * 时间：2016/9/22 10:46
 * 备注：
 */
public class testActivity  extends BaseActivity {
    private WebView  test_web_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_test_main);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        test_web_login= (MyWebView) findViewById(R.id.test_web_login);
        WebSettings wSet = test_web_login.getSettings();
        //加上这句话才能使用javascript方法
        wSet.setJavaScriptEnabled(true);
        wSet.setAllowFileAccess(true);// 设置允许访问文件数据
//        wSet.setSupportZoom(true);
        wSet.setDefaultTextEncodingName("utf-8");
        wSet.setBuiltInZoomControls(true);
        wSet.setUseWideViewPort(true);
        wSet.setLoadWithOverviewMode(true);
        wSet.setJavaScriptCanOpenWindowsAutomatically(true);
        wSet.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wSet.setDomStorageEnabled(true);
        wSet.setDatabaseEnabled(true);
        //WebView加载web资源
        test_web_login.loadUrl("http://dev01.onemeter.co:81/login.php");
//        test_web_login.loadUrl("http://crm.migoedu.com/login.php");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        test_web_login.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        test_web_login.setWebChromeClient(new WebChromeClient());

    }
}
