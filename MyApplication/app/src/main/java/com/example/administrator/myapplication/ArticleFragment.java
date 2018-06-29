package com.example.administrator.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

import com.example.administrator.myapplication.adapter.MyAdapter;
import com.example.administrator.myapplication.commom.Constants;
import com.example.administrator.myapplication.dao.Article;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Konstantin on 22.12.2014.
 */
public class ArticleFragment extends Fragment implements ScreenShotable ,AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    public static final String CLOSE = "Close";
    public static final String BUILDING = "Building";
    public static final String BOOK = "Book";
    public static final String PAINT = "Paint";
    public static final String CASE = "Case";
    public static final String SHOP = "Shop";
    public static final String PARTY = "Party";
    public static final String MOVIE = "Movie";

    private View containerView;
    protected ImageView mImageView;
    protected int res;
    private Bitmap bitmap;
    private static String userID;
    private ListView listview;

    private MyAdapter mAdapter;
    private static String tagName;
    private List<Article> dataList;
    private Article item;


    public static ArticleFragment newInstance(int resId, String userId, String name) {
        userID = userId;
        tagName = name;
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        bundle.putString("userId",userId);
        articleFragment.setArguments(bundle);
        return articleFragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container_article);
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
        String userId = getArguments().getString("userId");
        dataList=new ArrayList<Article>();

        View rootView = inflater.inflate(R.layout.fragment_article, container, false);
        //getData();//填充数据
        listview = (ListView) rootView.findViewById(R.id.titleListview);
        RefreshArticleList();
        //下拉刷新作用
        RefreshLayout refreshLayout = (RefreshLayout)rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1200);
//                getData();
//                mAdapter.refresh(data);
                RefreshArticleList();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1200);
            }
        });

        return rootView;
    }

    public void RefreshArticleList() {
        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            mAdapter.notifyDataSetChanged();
            listview.setAdapter(mAdapter);
        }

        //为数据绑定适配器
        getData();
        mAdapter = new MyAdapter(this.getActivity(),dataList);
        listview.setAdapter(mAdapter);

        //设定列表项的选择模式为单选
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);
        listview.setOnScrollListener(this);
        listview.setAdapter(mAdapter);
    }


    private void getData(){
        try {
            String result= ServiceUtil.getServiceInfo(Constants.findAllByuserId+Long.valueOf(userID),Constants.ip,Constants.port);
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("articleTitle").replace("\r\n","").replace("\t","").trim();
                Long id = Long.valueOf(jsonObject.getString("articleId"));
                Article article = new Article();
                article.setId(id);
                article.setTitle(title);
                article.setUserArticleId(jsonObject.getLong("id"));
                article.setNodes(jsonObject.getString("isRead"));

                dataList.add(article);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    // 接受上一个页面返回的数据，并刷新页面
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            RefreshArticleList();
        }
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
        //获得选中项的HashMap对象
        Article map= (Article)listview.getItemAtPosition(arg2);
        item = map;
        //mAdapter.notifyDataSetChanged();
        Long articleId = item.getId();
        Long userArticleId = item.getUserArticleId();
        try {
            String result= ServiceUtil.getServiceInfo(Constants.ArticleById+articleId,Constants.ip,Constants.port);
            JSONObject jsonObject = new JSONObject(result);
            String title = jsonObject.getString("title");
            String detail = jsonObject.getString("detail");

            Intent i = new Intent(getActivity(), ScrollingActivity.class);

            //用Bundle携带数据
            Bundle bundle=new Bundle();
            //传递name参数为tinyphp
            bundle.putString("content", detail);
            bundle.putLong("id", articleId);
            bundle.putLong("userArticleId", userArticleId);
            bundle.putString("title", title);
            i.putExtras(bundle);
            startActivity(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    // 点击listview中某一项长时间的点击事件
    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
        final int n=arg2;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("删除该日志");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = listview.getItemAtPosition(n) + "";
                String content1 = content.substring(content.indexOf("=") + 1,
                        content.indexOf(","));

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
                ArticleFragment.this.bitmap = bitmap;
            }
        };

        thread.start();

    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }


}

