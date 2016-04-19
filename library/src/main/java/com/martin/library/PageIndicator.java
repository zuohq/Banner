package com.martin.library;

import android.support.v4.view.ViewPager;

/**
 * @Description:
 * @author: Created by martin on 16-4-18.
 */
public interface PageIndicator extends ViewPager.OnPageChangeListener {

    void setViewPager(ViewPager viewPager);

    void setViewPager(ViewPager viewPager, int initialPosition);

    void setCurrentItem(int item);

    void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);

    void notifyDataSetChanged();
}
