package com.shituocheng.calcalculateapplication.com.douban;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import booksRecyclerViewAdapter.BooksViewPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class BooksTabFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public BooksTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_books_tab, container, false);

        FloatingActionsMenu floatingActionsMenu = (FloatingActionsMenu)view.findViewById(R.id.fab);
        FloatingActionButton SearchContenButton = new FloatingActionButton(getActivity());
        SearchContenButton.setTitle("根据书的内容,标题等搜索");
        SearchContenButton.setIcon(android.R.drawable.ic_search_category_default);
        SearchContenButton.setColorNormalResId(R.color.colorPrimary);
        SearchContenButton.setColorPressedResId(R.color.colorPrimaryDark);

        FloatingActionButton SearchQRButton = new FloatingActionButton(getActivity());
        SearchQRButton.setTitle("扫描书ISBN码");
        SearchQRButton.setIcon(android.R.drawable.ic_menu_camera);
        SearchQRButton.setColorNormalResId(R.color.colorPrimary);
        SearchQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialDialog dialog = new MaterialDialog.Builder(getActivity()).
                        title("根据书后ISBN码扫码查询").
                        content("根据书后的ISBN码扫码查询").build();

                dialog.setActionButton(DialogAction.POSITIVE,"点我扫码");
                dialog.setActionButton(DialogAction.NEGATIVE,"取消扫码");
                dialog.setActionButton(DialogAction.NEUTRAL,"什么是ISBN码?");

                dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentIntegrator.forSupportFragment(BooksTabFragment.this).initiateScan();
                    }
                });

                dialog.getActionButton(DialogAction.NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.getActionButton(DialogAction.NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                dialog.show();
            }
        });

        SearchQRButton.setColorPressedResId(R.color.colorPrimaryDark);

        floatingActionsMenu.addButton(SearchContenButton);
        floatingActionsMenu.addButton(SearchQRButton);
        viewPager = (ViewPager)view.findViewById(R.id.book_viewPager);
        tabLayout = (TabLayout)view.findViewById(R.id.tab_Layout);

        getActivity().setTitle("豆瓣读书");
        tabLayout.addTab(tabLayout.newTab().setText("新书榜单"));
        tabLayout.addTab(tabLayout.newTab().setText("热销榜单"));
        BooksViewPagerAdapter booksViewPagerAdapter = new BooksViewPagerAdapter(getFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(booksViewPagerAdapter);

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
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(),BookSearchResultActivity.class);
                intent.putExtra("book_isbn",result.getContents());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
