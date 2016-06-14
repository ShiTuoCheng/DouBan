package com.shituocheng.calcalculateapplication.com.douban;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import customViewPagerAdapter.DetailViewPagerAdapter;

public class MovieDetailActivity extends AppCompatActivity {
    private NetworkImageView networkImageView;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private String shared_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String movie_name = getIntent().getStringExtra("movie_name");
        String movie_title_image = getIntent().getStringExtra("movie_title_image");
        shared_url = getIntent().getStringExtra("movie_url");

        networkImageView = (NetworkImageView)findViewById(R.id.header);
        networkImageView.setImageUrl(movie_title_image,imageLoader);

        getSupportActionBar().setTitle(movie_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);

        collapsingToolbar.setTitleEnabled(false);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);

        tabLayout.addTab(tabLayout.newTab().setText("电影信息"));
        tabLayout.addTab(tabLayout.newTab().setText("电影短评"));
        tabLayout.addTab(tabLayout.newTab().setText("电影长评"));

        DetailViewPagerAdapter detailViewPagerAdapter = new DetailViewPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        viewPager.setAdapter(detailViewPagerAdapter);

        viewPager.setOffscreenPageLimit(tabLayout.getTabCount()*10);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

}
