package com.example.administrator.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.utils.AudioRecoderDialog;
import com.example.administrator.myapplication.utils.AudioRecoderUtils;
import com.example.administrator.myapplication.utils.NoteUtils;
import com.example.administrator.myapplication.utils.ProgressTextUtils;
import com.example.administrator.myapplication.utils.SpeechRecognizerTool;

import java.io.File;
import java.util.ArrayList;

import yalantis.com.sidemenu.interfaces.ScreenShotable;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DefaultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DefaultFragment extends Fragment implements ScreenShotable, View.OnTouchListener, AudioRecoderUtils.OnAudioStatusUpdateListener,SpeechRecognizerTool.ResultsCallback  {
    // TODO: Rename parameter arguments, choose names that match
    // TODO: Rename and change types of parameters
    private int res;
    private static String userID;
    private static String tagName;
    private ImageView mImageView,imageView;
    private View containerView;
    private Bitmap bitmap;
    private TextView button;
    private EditText mTextView;
    private AudioRecoderUtils audioRecoderUtils;
    private AudioRecoderDialog recoderDialog;
    private AudioRecoderUtils recoderUtils;
    private long downT;
    private SpeechRecognizerTool mSpeechRecognizerTool;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment DefaultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DefaultFragment newInstance(int resId, String userId,String name) {
        userID = userId;
        tagName = name;
        DefaultFragment fragment = new DefaultFragment();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container_default);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_default, container, false);

        mImageView = (ImageView) rootView.findViewById(R.id.image_detailcontent);
        //判断SDK版本是否大于等于19，大于就让他显示，小于就要隐藏，不然低版本会多出来一个
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            //还有设置View的高度，因为每个型号的手机状态栏高度都不相同
        }

        button = (TextView) rootView.findViewById(R.id.startButton);
        mTextView = (EditText) rootView.findViewById(R.id.speechTextView);
        button.setOnTouchListener(this);

        recoderDialog = new AudioRecoderDialog(getContext());
        recoderDialog.setShowAlpha(0.98f);

        recoderUtils = new AudioRecoderUtils(new File(Environment.getExternalStorageDirectory() + "/recoder.amr"));
        recoderUtils.setOnAudioStatusUpdateListener(this);

        mImageView.setClickable(true);
        mImageView.setFocusable(true);
        mImageView.setImageResource(res);


        mSpeechRecognizerTool = new SpeechRecognizerTool(getContext());


        return rootView;
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getActivity().getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initPermission();
                mSpeechRecognizerTool.startASR(this);
                recoderUtils.startRecord();
                downT = System.currentTimeMillis();
                recoderDialog.showAtLocation(view, Gravity.CENTER, 0, 0);
                //button.setBackgroundResource(R.drawable.shape_recoder_btn_recoding);
                button.setBackgroundResource(R.drawable.main_press_mic);

                return true;
            case MotionEvent.ACTION_UP:
                recoderUtils.stopRecord();
                recoderDialog.dismiss();
                button.setBackgroundResource(R.drawable.main_unpress_mic);
                return true;
        }
        return false;
    }


    @Override
    public void onStart() {
        super.onStart();
        mSpeechRecognizerTool.createTool();
    }
    @Override
    public void onStop() {
        super.onStop();
        mSpeechRecognizerTool.destroyTool();
    }
    @Override
    public void onResults(String result) {
        final String finalResult = result;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if("全部清空".equals(finalResult) || "全部青空".equals(finalResult)){
                    mTextView.setText("");
                }else{
                    String[] arrStr = {"好的保存","请保存","保存一下","保存为便签","就这样保存"};
                    String content=mTextView.getText().toString();
                    String cont = content;
                    content +=finalResult+"。";
                    for(int i=0;i<arrStr.length;i++){
                        String item = arrStr[i];
                        if (finalResult.contains(item)){
                            String result = NoteUtils.addNote(cont,Long.valueOf(userID));
                            String msg = "";
                            if("success".equals(result)){
                                msg = "已经为小主人保存到便签啦";
                            }else{
                                msg = "对不起!小主,保存失败啦";
                            }
                            ProgressTextUtils.toastShow(getActivity(),msg);
                            mTextView.setText("");
                            break;
                        }
                    }

                }
            }
        });
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm :permissions){
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getContext(), perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()){
            ActivityCompat.requestPermissions(getActivity(), toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onUpdate(double db) {
        if(null != recoderDialog) {
            int level = (int) db;
            recoderDialog.setLevel((int)db);
            recoderDialog.setTime(System.currentTimeMillis() - downT);
        }
    }


    //录音结束，filePath为保存路径
    @Override
    public void onStop(String filePath) {
        Toast.makeText(getActivity(), "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void takeScreenShot() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
                        containerView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                containerView.draw(canvas);
                DefaultFragment.this.bitmap = bitmap;
            }
        };

        thread.start();

    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}
