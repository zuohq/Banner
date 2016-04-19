package com.martin.banner;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.martin.library.IconPagerAdapter;

import java.util.List;

/**
 * @Description:
 * @author: Created by martin on 16-4-18.
 */
public abstract class BaseFragmentPagerAdapter<T> extends FragmentPagerAdapter implements IconPagerAdapter {

    protected List<T> items;

    private static final int FAKE_BANNER_SIZE = 20;
    private ViewPager mPager;

    public BaseFragmentPagerAdapter(FragmentManager fm, ViewPager pager, List<T> items) {
        super(fm);
        this.mPager = pager;
        this.items = items;
    }

    @Override
    public int getIconResId(int index) {
        return R.drawable.indicator;
    }

    @Override
    public int getCount() {
        if (items == null || items.isEmpty())
            return 0;
        else if (items.size() == 1)
            return 1;
        else
            return FAKE_BANNER_SIZE;
    }

    @Override
    public int getIndicatorCount() {
        return items.isEmpty() ? 0 : items.size();
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
        if (items.size() > 1) {
            int position = mPager.getCurrentItem();
            if (position == 0) {
                position = items.size();
                mPager.setCurrentItem(position, false);
            } else if (position == FAKE_BANNER_SIZE - 1) {
                position = items.size() - 1;
                mPager.setCurrentItem(position, false);
            }
        }
    }
}
