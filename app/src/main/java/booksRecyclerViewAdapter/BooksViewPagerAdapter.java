package booksRecyclerViewAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shituocheng.calcalculateapplication.com.douban.BooksHotFragment;
import com.shituocheng.calcalculateapplication.com.douban.BooksNewFragment;

/**
 * Created by shituocheng on 16/6/10.
 */

public class BooksViewPagerAdapter extends FragmentStatePagerAdapter {

    int number;

    public BooksViewPagerAdapter(FragmentManager fm, int number) {
        super(fm);
        this.number = number;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                BooksNewFragment booksNewFragment = new BooksNewFragment();
                return booksNewFragment;
            case 1:
                BooksHotFragment booksHotFragment = new BooksHotFragment();
                return booksHotFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return number;
    }
}
