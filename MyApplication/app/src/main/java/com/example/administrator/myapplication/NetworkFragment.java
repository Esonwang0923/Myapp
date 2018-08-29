package com.example.administrator.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

    private WebView webView;

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
        webView = (WebView)rootView.findViewById(R.id.webView);
//        webView.loadUrl("file:///android_asset/normal.html");
        webView.loadUrl("https://www.baidu.com/");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
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

        webView.getSettings().setBuiltInZoomControls(true);
        return rootView;
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
