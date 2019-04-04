package com.example.admin.godzillaandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.alibaba.fastjson.JSON;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.example.admin.godzillaandroid.dao.People;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity  implements taskAddFragment.OnFragmentInteractionListener,taskListFragment.OnFragmentInteractionListener{

    private ViewPager viewPager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(1);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        TypefaceProvider.registerDefaultIconSets();

        Intent intent=getIntent();
        String u =intent.getStringExtra("person");
        com.alibaba.fastjson.JSONObject jsonObject= com.alibaba.fastjson.JSONObject.parseObject(u);
        People stu=(People)JSON.toJavaObject(jsonObject, People.class);

        viewPager = (ViewPager) findViewById(R.id.viewpager_launch);
        List<Fragment> list_fragment = new ArrayList<>();
        list_fragment.add(taskAddFragment.newInstance(stu.getId(),stu.getCount()));
        list_fragment.add(taskListFragment.newInstance(stu.getId(),stu.getCount()));
        BottomViewAdapter adapter = new BottomViewAdapter(getSupportFragmentManager(), list_fragment);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);//设置缓存view 的个数

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
