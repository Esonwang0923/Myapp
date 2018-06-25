package com.example.administrator.myapplication.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

import com.example.administrator.myapplication.FmainActivity;
import com.example.administrator.myapplication.LoginActivity;
import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.ScrollingActivity;
import com.example.administrator.myapplication.ServiceUtil;
import com.example.administrator.myapplication.adapter.MyAdapter;
import com.example.administrator.myapplication.commom.Constants;
import com.example.administrator.myapplication.dao.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Konstantin on 22.12.2014.
 */
public class ContentFragment extends Fragment implements ScreenShotable {
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
    private List<Article> data;
    private Article item;
    private boolean DEV_MODE = false;



    public static ContentFragment newInstance(int resId,String userId,String name) {
        userID = userId;
        tagName = name;
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        bundle.putString("userId",userId);
        contentFragment.setArguments(bundle);
        return contentFragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getArguments().getInt(Integer.class.getName());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String userId = getArguments().getString("userId");

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if (BOOK.equals(tagName)){
            listview = (ListView) rootView.findViewById(R.id.listviewId);
            //添加点击事件
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
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

            });


            getData();//填充数据
            //设定列表项的选择模式为单选
            listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            //为数据绑定适配器
            mAdapter = new MyAdapter(this.getActivity(),data);
            listview.setAdapter(mAdapter);

        }else{
            mImageView = (ImageView) rootView.findViewById(R.id.image_content);
            mImageView.setClickable(true);
            mImageView.setFocusable(true);
            mImageView.setImageResource(res);
        }

        return rootView;
    }



    private void getData(){
        data=new ArrayList<Article>();
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

                data.add(article);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                ContentFragment.this.bitmap = bitmap;
            }
        };

        thread.start();

    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}

