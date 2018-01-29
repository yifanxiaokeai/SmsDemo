package com.jiangyu.common.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.FrameLayout;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gyf.barlibrary.FlymeOSStatusBarFontUtils;
import com.orhanobut.logger.Logger;
import com.jiangyu.common.R;
import com.jiangyu.common.entity.TabEntity;
import com.jiangyu.common.utils.StatusBarCompat;

import java.util.ArrayList;

public abstract class BaseHomeActivity extends BaseActivity {
    private String[] mTitles;
    private int[] mIconUnSelectIds;
    private int[] mIconSelectIds;
    protected BaseFragment[] mFragments;
    protected CommonTabLayout mTabLayout;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList();
    protected ViewPager mViewPager;
    protected FrameLayout mFrameLayout;
    private Bundle bundle;
    private int initChooseTab;
    private boolean isFirst = true;

    public BaseHomeActivity() {
    }

    public void setInitChooseTab(int initChooseTab) {
        this.initChooseTab = initChooseTab;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setBundle(savedInstanceState);
    }

    protected void initComponents() {
        StatusBarCompat.translucentStatusBar(mActivity);
//        FlymeOSStatusBarFontUtils.setStatusBarDarkIcon(mActivity, Color.WHITE);

        this.mTabLayout = (CommonTabLayout)this.findViewById(R.id.base_tabLayout);
        this.mViewPager = (ViewPager)this.findViewById(R.id.base_tabLayout_viewPager);
        this.mFrameLayout = (FrameLayout)this.findViewById(R.id.base_tabLayout_frameLayout);
        this.initTab();
        if(null != this.mFragments && this.mFragments.length != 0) {
            this.initTabEntities();
            if(null == this.mTabLayout) {
                throw new RuntimeException("CommonTabLayout is null!");
            } else {
                if(null == this.mTitles || this.mTitles.length == 0) {
                    this.mTabLayout.setTextsize(0.0F);
                }

                if(null != this.mViewPager) {
                    Log.e("BaseHomeActivity", "Choose_ViewPager");
                    this.initViewpagerAdapter();
                } else {
                    this.initFragments();
                    Log.e("BaseHomeActivity", "Choose_frameLayout");
                }

                this.setTabSelect();
                if(null != this.mViewPager) {
                    this.mViewPager.setCurrentItem(this.initChooseTab);
                } else {
                    this.mTabLayout.setCurrentTab(this.initChooseTab);
                }

            }
        } else {
            throw new RuntimeException("mFragments is null!");
        }
    }

    private void initTabEntities() {
        if(null != this.mFragments && this.mFragments.length != 0 && this.mFragments.length == this.mIconSelectIds.length && this.mFragments.length == this.mIconUnSelectIds.length) {
            for(int i = 0; i < this.mFragments.length; ++i) {
                this.mTabEntities.add(new TabEntity(this.mTitles == null?"":this.mTitles[i], this.mIconSelectIds[i], this.mIconUnSelectIds[i]));
            }

            this.mTabLayout.setTabData(this.mTabEntities);
        } else {
            throw new RuntimeException("mFragments is null!or Fragments and the number of ICONS do not meet");
        }
    }

    private void initFragments() {
        if(this.getBundle() == null) {
            this.loadMultipleRootFragment(R.id.base_tabLayout_frameLayout, this.initChooseTab, this.mFragments);
        } else {
            for(int i = 0; i < this.mFragments.length; ++i) {
                Log.e("BaseHomeActivity", "initFragments" + i);
                this.mFragments[i] = (BaseFragment)this.findFragment(this.mFragments[i].getClass());
            }
        }

    }

    private void initViewpagerAdapter() {
        this.mViewPager.setAdapter(new MyPagerAdapter(this.getSupportFragmentManager()));
        this.mViewPager.setOffscreenPageLimit(this.mFragments.length - 1);
        this.mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                BaseHomeActivity.this.mTabLayout.setCurrentTab(position);
            }

            public void onPageScrollStateChanged(int state) {
                if(state == 0) {
                    BaseHomeActivity.this.onTabSelect(BaseHomeActivity.this.mViewPager.getCurrentItem());
                }
            }
        });
    }

    private void setTabSelect() {
        Log.e("BaseHomeActivity", "setTabSelect");
        this.mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            public void onTabSelect(int position) {
                if(null != BaseHomeActivity.this.mViewPager) {
                    BaseHomeActivity.this.mViewPager.setCurrentItem(position);
                } else {
                    int toDoHidden = -1;

                    for(int i = 0; i < BaseHomeActivity.this.mFragments.length; ++i) {
                        if(!BaseHomeActivity.this.mFragments[i].isHidden()) {
                            toDoHidden = i;
                            Log.e("BaseHomeActivity", "查找显示中的fragment-------" + i);
                        }
                    }

                    Log.e("BaseHomeActivity", "选中的fragment-------" + position);
                    Log.e("BaseHomeActivity", "确定显示中的fragment-------" + toDoHidden);
                    BaseHomeActivity.this.showHideFragment(BaseHomeActivity.this.mFragments[position], BaseHomeActivity.this.mFragments[toDoHidden]);
                }
//                BaseHomeActivity.this.onTabSelect(position);
            }

            public void onTabReselect(int position) {
                Log.e("BaseHomeActivity", "再次选中项" + position);
                BaseHomeActivity.this.onTabReselect(position);
            }
        });
    }

    protected abstract void onTabSelect(int var1);

    protected abstract void onTabReselect(int var1);

    protected abstract void initTab();

    public BaseFragment[] getmFragments() {
        return this.mFragments;
    }

    public void setmFragments(BaseFragment[] mFragments) {
        this.mFragments = mFragments;
    }

    public int[] getmIconSelectIds() {
        return this.mIconSelectIds;
    }

    public void setmIconSelectIds(int[] mIconSelectIds) {
        this.mIconSelectIds = mIconSelectIds;
    }

    public int[] getmIconUnSelectIds() {
        return this.mIconUnSelectIds;
    }

    public void setmIconUnSelectIds(int[] mIconUnSelectIds) {
        this.mIconUnSelectIds = mIconUnSelectIds;
    }

    public String[] getmTitles() {
        return this.mTitles;
    }

    public void setmTitles(String[] mTitles) {
        this.mTitles = mTitles;
    }

    public Bundle getBundle() {
        return this.bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public int getCount() {
            return BaseHomeActivity.this.mFragments.length;
        }

        public CharSequence getPageTitle(int position) {
            return BaseHomeActivity.this.mTitles == null?"":BaseHomeActivity.this.mTitles[position];
        }

        public Fragment getItem(int position) {
            return BaseHomeActivity.this.mFragments[position];
        }
    }
}
