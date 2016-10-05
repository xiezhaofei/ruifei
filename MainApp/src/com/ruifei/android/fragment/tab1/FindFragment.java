package com.ruifei.android.fragment.tab1;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.ruifei.android.R;
import com.ruifei.android.fragment.tab1.child.CategoryFragment;
import com.ruifei.android.fragment.tab1.child.HistoryFragment;
import com.ruifei.android.fragment.tab1.child.PopFragment;
import com.ruifei.android.fragment.tab1.child.RecommendFragment;
import com.ruifei.framework.fragment.BaseFragment;

/**
 * Created by Administrator on 2016/8/24.
 */
public class FindFragment extends BaseFragment {

    private ViewPager mViewPager;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    @Override
    public void initUi() {
        mViewPager = (ViewPager)findViewById(R.id.tab1_viewpager);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip)findViewById(R.id.sliding_tab);
        super.initUi();
    }

    @Override
    public void loadData() {
        super.loadData();
        mViewPager.setAdapter(new myPagerAdapter(getChildFragmentManager()));
        mPagerSlidingTabStrip.setViewPager(mViewPager);
    }

    @Override
    public int getContainerLayoutId() {
        return R.layout.find_fragment_lay;
    }

    class myPagerAdapter extends FragmentPagerAdapter {
        String[] title = { "推荐", "热门", "分类","历史" };

        public myPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    RecommendFragment fragment = new RecommendFragment();
//                    startFragment(fragment);
                    return fragment;
                case 1:
                    PopFragment popFragment = new PopFragment();
                    return popFragment;
                case 2:
                    CategoryFragment categoryFragment = new CategoryFragment();
                    return categoryFragment;
                case 3:
                    HistoryFragment historyFragment = new HistoryFragment();
                    return historyFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {

            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

    }

}
