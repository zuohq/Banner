/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2012 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.martin.library;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class IconPageIndicator extends HorizontalScrollView implements PageIndicator {

    private static final String TAG = IconPageIndicator.class.getSimpleName();

    /**
     * 默认自动滚动时间间隔
     */
    private static final int DELAY_TIME = 3 * 1000;

    private static final int FAKE_BANNER_SIZE = 20;

    private static final int MSG_WHAT_AUTO_SCROLL = 101;

    /***
     * child个数大于１才能滚动
     */
    private static final int MIN_PAGE_COUNT = 1;

    private ViewPager mViewPager;
    private OnPageChangeListener mListener;

    /***
     * 指示器布局
     */
    private final LinearLayout mIndicatorLayout;

    /***
     * 当前选中项
     */
    private int mSelectedIndex;


    private Handler mPagerHandler;

    /***
     * 滚动时间间隔
     */
    private long mIntervalTime = DELAY_TIME;

    /***
     * 是否自动滚动
     */
    private boolean isAutoScroll;

    /***
     * 指示器之间的间距
     */
    private int padding;


    public IconPageIndicator(Context context) {
        this(context, null);
    }

    public IconPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);

        mPagerHandler = new PagerHandler(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconPageIndicator);
        mIntervalTime = a.getInt(R.styleable.IconPageIndicator_delay_time, DELAY_TIME);
        padding = a.getDimensionPixelSize(R.styleable.IconPageIndicator_spacing, 0);
        a.recycle();

        setIntervalTime(DELAY_TIME);

        mIndicatorLayout = new LinearLayout(context);
        addView(mIndicatorLayout, new LayoutParams(WRAP_CONTENT, MATCH_PARENT, Gravity.CENTER));
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.removeOnPageChangeListener(this);
        }
        PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    /***
     * add 指示器
     */
    public void notifyDataSetChanged() {
        mIndicatorLayout.removeAllViews();
        IconPagerAdapter iconAdapter = (IconPagerAdapter) mViewPager.getAdapter();
        int count = iconAdapter.getIndicatorCount();
        for (int index = 0; index < count; index++) {
            ImageView view = new ImageView(getContext());
            view.setImageResource(iconAdapter.getIconResId(index));
            view.setPadding(padding, 0, padding, 0);
            mIndicatorLayout.addView(view);
        }

        mSelectedIndex = Math.min(mSelectedIndex, count - 1);
        setCurrentItem(mSelectedIndex);
        requestLayout();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    /****
     * 选中项(page /指示器)
     *
     * @param item
     */
    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedIndex = item;
        mViewPager.setCurrentItem(item);

        int count = getPageCount();

        int tabCount = mIndicatorLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            View child = mIndicatorLayout.getChildAt(i);
            boolean isSelected = (i == item % count);
            child.setSelected(isSelected);
        }
        //只有一页，不显示指示器
        mIndicatorLayout.setVisibility(mIndicatorLayout.getChildCount() > 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }

        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            mPagerHandler.removeMessages(MSG_WHAT_AUTO_SCROLL);
        } else if (state == ViewPager.SCROLL_STATE_IDLE && isAutoScroll) {
            startAutoScroll();
        }
    }

    public void setIntervalTime(long intervalTime) {
        if (intervalTime < 1000) {
            mIntervalTime = DELAY_TIME;
        } else {
            mIntervalTime = intervalTime;
        }
    }

    /***
     * 开启自动滚动
     */
    public void startAutoScroll() {
        stopAutoScroll();

        if (getPageCount() > MIN_PAGE_COUNT) {
            isAutoScroll = true;
            mPagerHandler.sendEmptyMessageDelayed(MSG_WHAT_AUTO_SCROLL, mIntervalTime);
        }
    }

    /***
     * 停止自动滚动
     */
    public void stopAutoScroll() {
        if (mPagerHandler.hasMessages(MSG_WHAT_AUTO_SCROLL)) {
            isAutoScroll = false;
            mPagerHandler.removeMessages(MSG_WHAT_AUTO_SCROLL);
        }
    }

    private void scrollToNextPage() {
        if (!isAutoScroll) return;

        int pageCount = getPageCount();

        if (pageCount > MIN_PAGE_COUNT) {
            int currentItem = mSelectedIndex + 1;
            if (currentItem == FAKE_BANNER_SIZE - 1) {
                mViewPager.setCurrentItem(pageCount - 1, false);
            } else {
                mViewPager.setCurrentItem(currentItem);
            }
        }
        startAutoScroll();
    }

    private int getPageCount() {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }

        IconPagerAdapter adapter = (IconPagerAdapter) mViewPager.getAdapter();
        return adapter.getIndicatorCount();
    }

    private static class PagerHandler extends Handler {

        private WeakReference<IconPageIndicator> mIconPageIndicator;

        public PagerHandler(IconPageIndicator iconPageIndicator) {
            this.mIconPageIndicator = new WeakReference<>(iconPageIndicator);
        }

        private boolean isActive(Context context) {
            if (context != null) {
                Activity activity = (Activity) context;
                return !activity.isFinishing();
            }
            return false;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            IconPageIndicator iconPageIndicator = mIconPageIndicator.get();
            if (iconPageIndicator != null && isActive(iconPageIndicator.getContext())) {
                if (msg.what == MSG_WHAT_AUTO_SCROLL) {
                    iconPageIndicator.scrollToNextPage();
                }
            }
        }
    }
}