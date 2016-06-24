# Banner
Android auto scroll viewpager

# Usage

    java code
FragmentPagerAdapter adapter = new TestFragmentAdapter(getSupportFragmentManager(), mPager, Arrays.asList(URLS));
        mPager.setAdapter(adapter);
        mIconPageIndicator.setViewPager(mPager);
        mIconPageIndicator.startAutoScroll();
        
    layout xml
  
  <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      tools:context="com.martin.banner.MainActivity">
  
      <android.support.v4.view.ViewPager
          android:id="@+id/pager"
          android:layout_width="match_parent"
          android:layout_height="150dp" />
  
      <com.martin.library.IconPageIndicator
          android:id="@+id/indicator"
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:layout_gravity="bottom"
          android:layout_marginBottom="10dp"
          app:spacing="2dp" />
  </FrameLayout>