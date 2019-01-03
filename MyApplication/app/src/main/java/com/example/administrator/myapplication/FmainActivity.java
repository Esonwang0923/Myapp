package com.example.administrator.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;


public class FmainActivity extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    private ArticleFragment articleFragment;
    private DetailFragment detailFragment;
    private DefaultFragment defaultFragment;
    private NetworkFragment networkFragment;
    private MusicFragment musicFragment;



    private ViewAnimator viewAnimator;
    private int res = R.drawable.content_main;
    private LinearLayout linearLayout;
    private String userId;
    private Boolean isTrue =true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收content值
        userId = bundle.getString("userId");
        setContentView(R.layout.activity_fmain);
        if(savedInstanceState == null) {
            defaultFragment = DefaultFragment.newInstance(R.drawable.content_main,userId,null);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, defaultFragment)
                    .commit();
        }


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        setActionBar();
        createMenuList();
        viewAnimator = new ViewAnimator<>(this, list, defaultFragment, drawerLayout, this);

    }

    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(ArticleFragment.CLOSE, R.drawable.icn_close);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem(ArticleFragment.BUILDING, R.drawable.icn_1);
        list.add(menuItem);
        SlideMenuItem menuItem2 = new SlideMenuItem(ArticleFragment.BOOK, R.drawable.icn_2);
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(ArticleFragment.PAINT, R.drawable.icn_3);
        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem(ArticleFragment.CASE, R.drawable.icn_4);
        list.add(menuItem4);
        SlideMenuItem menuItem5 = new SlideMenuItem(ArticleFragment.SHOP, R.drawable.icn_5);
        list.add(menuItem5);
        SlideMenuItem menuItem6 = new SlideMenuItem(ArticleFragment.PARTY, R.drawable.icn_6);
        list.add(menuItem6);
        SlideMenuItem menuItem7 = new SlideMenuItem(ArticleFragment.MOVIE, R.drawable.icn_7);
        list.add(menuItem7);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ScreenShotable replaceFragment(ScreenShotable screenShotable, int topPosition,String name) {
        this.res = this.res == R.drawable.content_main ? R.drawable.content_main : R.drawable.content_main;
        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, 0, topPosition, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);

        findViewById(R.id.content_overlay).setBackgroundDrawable(new BitmapDrawable(getResources(), screenShotable.getBitmap()));
        animator.start();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if("Book".equals(name)){

            if(articleFragment == null){
                articleFragment = ArticleFragment.newInstance(this.res,userId,name);
                transaction.add(R.id.content_frame, articleFragment,"book");
            }
            hideFragment(transaction);
            transaction.show(articleFragment);
            transaction.commit();
            return articleFragment;

        }else if("Close".equals(name)){
            if(defaultFragment == null){
                defaultFragment = DefaultFragment.newInstance(R.drawable.content_main,userId,null);
                transaction.add(R.id.content_frame, defaultFragment,"close");
            }
            hideFragment(transaction);
            transaction.show(defaultFragment);
            transaction.commit();
            return defaultFragment;

        }else if("Paint".equals(name)){
            if(networkFragment == null){
                networkFragment = NetworkFragment.newInstance(this.res,userId,null);
                transaction.add(R.id.content_frame, networkFragment,"Paint");
            }
            hideFragment(transaction);
            transaction.show(networkFragment);
            transaction.commit();
            return networkFragment;

        }else if("Case".equals(name)){
            if(musicFragment == null){
                musicFragment = MusicFragment.newInstance(this.res,userId,null);
                transaction.add(R.id.content_frame, musicFragment,"Paint");
            }
            hideFragment(transaction);
            transaction.show(musicFragment);
            transaction.commit();
            return musicFragment;

        }else{

            if(detailFragment == null){
                detailFragment = DetailFragment.newInstance(this.res,userId,name);
                transaction.add(R.id.content_frame, detailFragment,"notes");
            }
            hideFragment(transaction);
            transaction.show(detailFragment);
            transaction.commit();
            return detailFragment;

        }

    }


    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (articleFragment != null) {
            transaction.hide(articleFragment);
        }
        if (detailFragment != null) {
            transaction.hide(detailFragment);
        }
        if (networkFragment != null) {
            transaction.hide(networkFragment);
        }
        if (defaultFragment != null) {
            transaction.hide(defaultFragment);
        }
        if (musicFragment != null) {
            transaction.hide(musicFragment);
        }
    }

        @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        String name = slideMenuItem.getName();

        switch (slideMenuItem.getName()) {
            case ArticleFragment.CLOSE:
                //return screenShotable;
                return replaceFragment(screenShotable, position,name);
            case ArticleFragment.BOOK:
                return replaceFragment(screenShotable, position,name);
            case ArticleFragment.PAINT:
                return replaceFragment(screenShotable, position,name);
            case ArticleFragment.CASE:
                return replaceFragment(screenShotable, position,name);
            default:
                return replaceFragment(screenShotable, position,name);
        }
    }

    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);

    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();

    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }


}
