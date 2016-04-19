package com.martin.banner;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.martin.library.IconPageIndicator;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    private static final String[] URLS = {
            "http://c.hiphotos.baidu.com/image/h%3D200/sign=c648b6971ed8bc3ed90801cab28aa6c8/7a899e510fb30f24e86eefaecf95d143ac4b03a2.jpg"
            , "http://img10.3lian.com/sc6/show/s11/19/20110711104935563.jpg"
            , "http://photo.l99.com/bigger/33/1365724353046_esa57x.jpg"
            , "http://img.sj33.cn/uploads/allimg/201302/1-130201105109.jpg"
    };

    @InjectView(R.id.pager)
    ViewPager mPager;

    @InjectView(R.id.indicator)
    IconPageIndicator mIconPageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        FragmentPagerAdapter adapter = new TestFragmentAdapter(getSupportFragmentManager(), mPager, Arrays.asList(URLS));
        mPager.setAdapter(adapter);

        mIconPageIndicator.setViewPager(mPager);
        mIconPageIndicator.startAutoScroll();
    }
}
