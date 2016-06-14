package customViewPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shituocheng.calcalculateapplication.com.douban.MovieDetailInfoFragment;
import com.shituocheng.calcalculateapplication.com.douban.MovieLongReviewFragment;
import com.shituocheng.calcalculateapplication.com.douban.MovieShortReviewFragment;

/**
 * Created by shituocheng on 16/6/2.
 */

public class DetailViewPagerAdapter extends FragmentStatePagerAdapter {

    int number;

    public DetailViewPagerAdapter(FragmentManager fm, int number) {
        super(fm);
        this.number = number;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                MovieDetailInfoFragment movieDetailInfoFragment = new MovieDetailInfoFragment();
                return movieDetailInfoFragment;
            case 1:
                MovieShortReviewFragment movieShortReviewFragment = new MovieShortReviewFragment();
                return movieShortReviewFragment;
            case 2:
                MovieLongReviewFragment movieLongReviewFragment = new MovieLongReviewFragment();
                return movieLongReviewFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return number;
    }
}
