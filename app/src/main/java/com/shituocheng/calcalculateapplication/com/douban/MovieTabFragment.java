package com.shituocheng.calcalculateapplication.com.douban;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.getbase.floatingactionbutton.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import customViewPagerAdapter.MainViewPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieTabFragment extends Fragment {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_tab, container, false);
        getActivity().setTitle("豆瓣电影");
        FloatingActionsMenu floatingActionsMenu = (FloatingActionsMenu)view.findViewById(R.id.fab);
        FloatingActionButton searchAction = new FloatingActionButton(getActivity());
        searchAction.setIcon(android.R.drawable.ic_search_category_default);
        searchAction.setColorNormalResId(R.color.colorPrimary);
        searchAction.setTitle("搜索");
        searchAction.setColorPressedResId(R.color.colorPrimaryDark);
        searchAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                        .title(R.string.search_name)
                        .content(R.string.search_name)
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS)
                        .input("", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {

                            }
                        }).build();
                dialog.setActionButton(DialogAction.NEGATIVE,R.string.cancel);
                dialog.setActionButton(DialogAction.POSITIVE,R.string.ok);
                dialog.getActionButton(DialogAction.NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String inputText = dialog.getInputEditText().getText().toString();

                        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                        if (!inputText.isEmpty()){

                            Intent intent = new Intent(getActivity(),SearchMovieResultActivity.class);
                            intent.putExtra("movie_search",inputText);
                            startActivity(intent);

                        }else {
                            Snackbar.make(v,"输入点什么东西",Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.show();


            }
        });
        floatingActionsMenu.addButton(searchAction);

        mViewPager = (ViewPager)view.findViewById(R.id.viewPager);

        mTabLayout = (TabLayout)view.findViewById(R.id.tab_Layout);

        mTabLayout.addTab(mTabLayout.newTab().setText("正在热映"));
        mTabLayout.addTab(mTabLayout.newTab().setText("即将上映"));
        mTabLayout.addTab(mTabLayout.newTab().setText("TOP250"));
        //mTabLayout.addTab(mTabLayout.newTab().setText("口碑榜"));//API被加密 无法获取
        //mTabLayout.addTab(mTabLayout.newTab().setText("北美票房榜"));
        //mTabLayout.addTab(mTabLayout.newTab().setText("新片榜"));//API被加密,无法获取

        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getFragmentManager(),mTabLayout.getTabCount());

        mViewPager.setAdapter(mainViewPagerAdapter);

        mViewPager.setOffscreenPageLimit(mTabLayout.getTabCount()*10);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

}
