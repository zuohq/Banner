package com.martin.banner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.List;

/**
 * @Description:
 * @author: Created by martin on 16-4-18.
 */
public class TestFragmentAdapter extends BaseFragmentPagerAdapter<String> {

    private static final String TAG = TestFragmentAdapter.class.getSimpleName();


    public TestFragmentAdapter(FragmentManager fm, ViewPager pager, List<String> items) {
        super(fm, pager, items);
    }

    @Override
    public Fragment getItem(int position) {
        position %= items.size();
        Log.i(TAG, "position=" + position);
        return TestFragment.newInstance(items.get(position));
    }
}
