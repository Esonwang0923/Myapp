package com.example.administrator.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.myapplication.commom.Constants;
import com.example.administrator.myapplication.commom.CreateExcel;
import com.example.administrator.myapplication.dao.Notes;
import com.example.administrator.myapplication.utils.NoteUtils;
import com.example.administrator.myapplication.utils.ProgressTextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yalantis.com.sidemenu.interfaces.ScreenShotable;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements ScreenShotable, OnScrollListener,
        OnItemClickListener, OnItemLongClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // TODO: Rename and change types of parameters
    private static String userID;
    private static String tagName;
    private View containerView;
    private Bitmap bitmap;
    protected int res;

    private Context mContext;
    private ListView listview;
    private SimpleAdapter simp_adapter;
    private List<Map<String, Object>> dataList;
    private Button addNote;
    private Button export;

    private TextView tv_content;
    private LinearLayout linearLayout;
    private Gson gson;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(int resId, String userId, String name) {
        userID = userId;
        tagName = name;
        DetailFragment contentFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        bundle.putString("userId",userId);
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container_detail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        //setContentView(R.layout.fragment_notes);
        tv_content = (TextView) rootView.findViewById(R.id.tv_content);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.content_note_tag);

        listview = (ListView) rootView.findViewById(R.id.detaillistview);
        dataList = new ArrayList<>();

        addNote = (Button) rootView.findViewById(R.id.btn_editnote);
        export = (Button) rootView.findViewById(R.id.btn_export);
//        mImageView = (ImageView) rootView.findViewById(R.id.image_detailcontent);
//        mImageView.setClickable(true);
//        mImageView.setFocusable(true);
//        mImageView.setImageResource(res);
        mContext = getContext();
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                noteEdit.ENTER_STATE = 0;
                Intent intent = new Intent(mContext, noteEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("info", "");
                bundle.putString("userId", userID);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        export.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.ico_main)//设置标题的图片
                        .setTitle("导出Excel")//设置对话框的标题
                        .setMessage("确定转换导出?")//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getContext(), "点击了取消按钮", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CreateExcel createExcel = new CreateExcel();
                                CreateExcel.verifyStoragePermissions(getActivity());
                                //createExcel.excelCreate();
                                List<String[]> datas = new ArrayList<>();
                                for (int i = 0; i < dataList.size(); i++) {
                                    Map map = dataList.get(i);
                                    String datae = (String)map.get("edate");
                                    String contents = (String) map.get("content");
                                    String[] content = {datae,contents};
                                    datas.add(content);
                                }

                                try {
                                    createExcel.saveDataToExcel(datas);
                                    Toast.makeText(getContext(), "导出成功", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                //Toast.makeText(getContext(), "点击了确定的按钮", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();

            }

        });

        RefreshNotesList();
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);
        listview.setOnScrollListener(this);

        return rootView;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            res = getArguments().getInt(Integer.class.getName());
        }
    }
    public void RefreshNotesList() {

        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            simp_adapter.notifyDataSetChanged();
            listview.setAdapter(simp_adapter);
        }
        simp_adapter = new SimpleAdapter(getContext(), getData(), R.layout.fragment_noteitem, new String[] { "tv_content", "tv_date" }, new int[] {R.id.tv_content, R.id.tv_date });
        listview.setAdapter(simp_adapter);
    }

    private List<Map<String, Object>> getData() {

        try {
            String result= ServiceUtil.getServiceInfo(Constants.findNotesByUserId+Long.valueOf(userID),Constants.ip,Constants.port);
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String note = jsonObject.getString("note").replace("\r\n","").replace("\t","").trim();
                String content = jsonObject.getString("content").replace("\r\n","").replace("\t","").trim();
                long userId = Long.valueOf(jsonObject.getString("userId"));
                long id = Long.valueOf(jsonObject.getString("id"));
                String date = jsonObject.getString("date");
                Map<String,Object> notes = new HashMap<>();
                notes.put("tv_content",note);
                notes.put("tv_date",date);
                notes.put("edate",date);
                notes.put("id",id);
                notes.put("userId",userId);
                notes.put("content",content);
                notes.put("note",note);

                dataList.add(notes);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataList;

    }

    @Override
    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    // 滑动listview监听事件
    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
        // TODO Auto-generated method stub
        switch (arg1) {
            case SCROLL_STATE_FLING:
                Log.i("main", "用户在手指离开屏幕之前，由于用力的滑了一下，视图能依靠惯性继续滑动");
            case SCROLL_STATE_IDLE:
                Log.i("main", "视图已经停止滑动");
            case SCROLL_STATE_TOUCH_SCROLL:
                Log.i("main", "手指没有离开屏幕，试图正在滑动");
        }
    }

    // 点击listview中某一项的监听事件
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        noteEdit.ENTER_STATE = 1;
//        Resources res = getResources(); //resource handle
//        Drawable drawable = res.getDrawable(R.drawable.content_note_bg1);
//        linearLayout.setBackground(drawable);
        // content=(TextView)listview.getChildAt(arg2).findViewById(R.id.tv_content);
        // String content1=content.toString();
        Map<String ,Object> map = (Map<String, Object>) listview.getItemAtPosition(arg2) ;
        //gson = new Gson().newBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        //Notes notes = gson.fromJson(jsonReader, Notes.class);

        Log.d("CONTENT", String.valueOf(map.get("content")));

        Intent myIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("info",  String.valueOf(map.get("content")));
        bundle.putString("userId", userID);

        noteEdit.id = (int) Integer.parseInt(String.valueOf(map.get("id")));
        myIntent.putExtras(bundle);
        myIntent.setClass(getActivity(), noteEdit.class);
        startActivityForResult(myIntent, 1);


    }

    @Override
    // 接受上一个页面返回的数据，并刷新页面
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            RefreshNotesList();
        }
    }

    // 点击listview中某一项长时间的点击事件
    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
        final int n=arg2;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("删除该便签");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Map<String ,Object> map = (Map<String, Object>) listview.getItemAtPosition(n) ;
                    long noteId = Long.valueOf(map.get("id").toString());
                    Log.i("you click","删除");
                    String result = NoteUtils.deleteNote(null,noteId);
                    if ("success".equals(result)){
                        ProgressTextUtils.toastShow(getActivity(),"已丢弃，主人！");
                    }
                    RefreshNotesList();

                }catch (Exception e){
                    e.printStackTrace();;
                }


//                while (c.moveToNext()) {
//                    String id = c.getString(c.getColumnIndex("_id"));
//                    String sql_del = "update note set content='' where _id="
//                            + id;
//                    //dbread.execSQL(sql_del);
//                    RefreshNotesList();
//                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
        return true;
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
                DetailFragment.this.bitmap = bitmap;
            }
        };

        thread.start();

    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}
