package com.example.administrator.myapplication;

import android.os.Bundle;
import android.app.Activity;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.myapplication.commom.Constants;
import com.example.administrator.myapplication.commom.NotesDB;

import org.json.JSONException;
import org.json.JSONObject;

public class noteEdit extends Activity {

    private TextView tv_date;
    private EditText et_content;
    private Button btn_ok;
    private Button btn_cancel;
    private NotesDB DB;
    private SQLiteDatabase dbread;
    public static int ENTER_STATE = 0;
    public static String last_content;
    public static int id;
    public static long userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_note_edit);

        tv_date = (TextView) findViewById(R.id.tv_date);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = sdf.format(date);
        tv_date.setText(dateString);

        et_content = (EditText) findViewById(R.id.et_content);
        // 设置软键盘自动弹出
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        Bundle myBundle = this.getIntent().getExtras();
        last_content = myBundle.getString("info");
        userId = Long.valueOf(myBundle.getString("userId"));

        Log.d("LAST_CONTENT", last_content);
        et_content.setText(last_content);
        // 确认按钮的点击事件
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // 获取日志内容
                String content = et_content.getText().toString();
                Log.d("LOG1", content);
                // 获取写日志时间
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateNum = sdf.format(date);
                String sql;
                Log.d("ENTER_STATE", ENTER_STATE + "");
                // 添加一个新的日志
                if (ENTER_STATE == 0) {
                    if (!content.equals("")) {
                        try {
                            JSONObject jsonObj = new JSONObject();
                            jsonObj.put("note",content);
                            jsonObj.put("content",content);
                            jsonObj.put("userId",userId);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-mm");
                            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                            String timestamp = formatter.format(curDate);
                            jsonObj.put("date",timestamp);
                            ServiceUtil serviceUtil = new ServiceUtil();
                            String result= serviceUtil.getServiceInfoPost(Constants.addNotes,jsonObj.toString());
                            if(!"success".equals(result)){
                                throw new JSONException("插入报错");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } else {  // 查看并修改一个已有的日志
                    Log.d("执行命令", "执行了该函数");
                    try {
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("note",content);
                        jsonObj.put("content",content);
                        jsonObj.put("id",id);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-mm");
                        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                        String timestamp = formatter.format(curDate);
                        jsonObj.put("date",timestamp);
                        ServiceUtil serviceUtil = new ServiceUtil();
                        String result= serviceUtil.getServiceInfoPost(Constants.updateNote,jsonObj.toString());
                        if(!"success".equals(result)){
                            throw new JSONException("插入报错");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // et_content.setText(last_content);
                }
                Intent data = new Intent();
                setResult(2, data);
                finish();
            }
        });
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        // if (requestCode == 3 && resultCode == 4) {
        // last_content=data.getStringExtra("data");
        // Log.d("LAST_STRAING", last_content+"gvg");
        // }
    }
}

