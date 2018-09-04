package com.example.administrator.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.administrator.myapplication.commom.Constants;
import com.example.administrator.myapplication.utils.AudioRecoderUtils;
import com.example.administrator.myapplication.utils.SpeechRecognizerTool;

import yalantis.com.sidemenu.interfaces.ScreenShotable;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NetworkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NetworkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NetworkFragment extends Fragment implements ScreenShotable, View.OnTouchListener, AudioRecoderUtils.OnAudioStatusUpdateListener,SpeechRecognizerTool.ResultsCallback{

    // TODO: Rename and change types of parameters
    private int res;
    private static String userID;
    private ProgressDialog progressDialog;

    private WebView webView;
    private Button back_button,pre_button,brower_main_button;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NetworkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NetworkFragment newInstance(int resId, String userId,String name) {
        userID = userId;
        NetworkFragment fragment = new NetworkFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        bundle.putString("userId",userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            res = getArguments().getInt(Integer.class.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_network, container, false);
        back_button = (Button) rootView.findViewById(R.id.back_button);
        webView = (WebView)rootView.findViewById(R.id.webView);
//        webView.loadUrl("file:///android_asset/normal.html");
        webView.loadUrl(Constants.BROWERURL);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebViewClient(new WebViewClient() {

            //重写页面打开和结束的监听。打开时弹出菊花，关闭时隐藏菊花
            /**
             * 界面打开的回调
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //弹出菊花
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("提示");
                progressDialog.setMessage("软软正在拼命加载……");
                progressDialog.show();

            }

            /**
             * 界面打开完毕的回调
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                //隐藏菊花:不为空，正在显示。才隐藏关闭
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

        });

        //支持App内部javascript交互
        webView.getSettings().setJavaScriptEnabled(true);

        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webView.getSettings().setLoadWithOverviewMode(true);

        //设置可以支持缩放
        webView.getSettings().setSupportZoom(true);

        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);

        //设置是否出现缩放工具
        webView.getSettings().setBuiltInZoomControls(false);
        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // final User user ;
        Button btn_send = (Button) getActivity().findViewById(R.id.back_button);
        pre_button = (Button) getActivity().findViewById(R.id.pre_button);
        brower_main_button = (Button) getActivity().findViewById(R.id.brower_main_button);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这是一个监听用的按键的方法，keyCode 监听用户的动作，如果是按了返回键，同时Webview要返回的话，WebView执行回退操作，因为mWebView.canGoBack()返回的是一个Boolean类型，所以我们把它返回为true
                if(webView.canGoBack()){
                    webView.goBack();
                }
            }
        });

        pre_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这是一个监听用的按键的方法，keyCode 监听用户的动作，如果是按了返回键，同时Webview要返回的话，WebView执行回退操作，因为mWebView.canGoBack()返回的是一个Boolean类型，所以我们把它返回为true
                if(webView.canGoBack()){
                    webView.goForward();
                }
            }
        });

        brower_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这是一个监听用的按键的方法，keyCode 监听用户的动作，如果是按了返回键，同时Webview要返回的话，WebView执行回退操作，因为mWebView.canGoBack()返回的是一个Boolean类型，所以我们把它返回为true
                if(webView.canGoBack()){
                    webView.loadUrl(Constants.BROWERURL);
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onUpdate(double db) {

    }

    @Override
    public void onStop(String filePath) {

    }

    @Override
    public void onResults(String result) {

    }

    @Override
    public void takeScreenShot() {

    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
