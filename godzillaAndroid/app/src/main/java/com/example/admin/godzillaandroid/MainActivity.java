package com.example.admin.godzillaandroid;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.font.FontAwesome;

public class MainActivity extends AppCompatActivity {

    WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//必须放在setContentView之前执行，不然会报错的。
        setContentView(R.layout.activity_main);
        TypefaceProvider.registerDefaultIconSets();

        wv = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true); //加此行即可出来百度地图

        // 加载需要显示的网页
        http://www.minidou.top:8080/#/index
        wv.loadUrl("https://www.baidu.com/");
        // 设置Web视图
        wv.setWebViewClient(new HelloWebViewClient());

        // 此方法可以处理webview 在加载时和加载完成时一些操作
        wv.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 这里是设置activity的标题， 也可以根据自己的需求做一些其他的操作
                    //title.setText("加载完成");
                } else {
                    //title.setText("加载中.......");
                }
            }
        });


        // 第二种方法
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }


    public boolean onKeyDown(int keyCoder,KeyEvent event){
        if(wv.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK){
            wv.goBack();   //goBack()表示返回webView的上一页面
            return true;
        }
        return false;
    }
    // Web视图
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

