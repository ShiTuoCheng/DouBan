package customViewPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.shituocheng.calcalculateapplication.com.douban.CommingSoonMovieFragment;
import com.shituocheng.calcalculateapplication.com.douban.NowMovieFragment;
import com.shituocheng.calcalculateapplication.com.douban.Top250MovieFragment;
import com.shituocheng.calcalculateapplication.com.douban.WeeklyMovieFragment;

import customRecyclerViewAdapter.WeeklyRecyclerViewAdapter;

/**
 * Created by shituocheng on 16/6/1.
 */

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {

    int number;


    public MainViewPagerAdapter(FragmentManager fm, int number) {
        super(fm);
        this.number = number;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                NowMovieFragment nowMovieFragment = new NowMovieFragment();
                return nowMovieFragment;
            case 1:
                CommingSoonMovieFragment commingSoonMovieFragment = new CommingSoonMovieFragment();
                return commingSoonMovieFragment;
            case 2:
                Top250MovieFragment top250MovieFragment = new Top250MovieFragment();
                return top250MovieFragment;
            case 3:
                WeeklyMovieFragment weeklyMovieFragment = new WeeklyMovieFragment();
                return weeklyMovieFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return number;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
