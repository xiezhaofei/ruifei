package com.ruifei.technology.android.fragment.tab1.child;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruifei.framework.adapter.BaseViewPagerAdapter;
import com.ruifei.framework.fragment.BaseFragment;
import com.ruifei.framework.utils.DisplayUtil;
import com.ruifei.technology.android.R;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhaofei.xie on 2016/10/5.
 */
public class CategoryFragment extends BaseFragment {

    private GridView mGridView;
    private String[] data;
    private BaseViewPagerAdapter mAdapter;

    @Override
    public int getContainerLayoutId() {
        return R.layout.category_fragment_lay;
    }

    @Override
    public void initUi() {
        super.initUi();
        mGridView = (GridView) findViewById(R.id.gridview);

    }

    @Override
    public void loadData() {
        super.loadData();

        data = new String[]{
                "你好1","你好2","你好3","你好4","你好5","你好6","你好7","你好8","你好9","你好10",
                "你好11","你好12","你好13","你好14","你好15","你好16","你好17","你好18","你好19","你好20",
                "你好21","你好22","你好23","你好24","你好25","你好26","你好27","你好28","你好29","你好30"
        };
        MyGridViewAdapter adapter = new MyGridViewAdapter(data);
        mGridView.setAdapter(adapter);
    }

    private class MyGridViewAdapter extends BaseAdapter {
        private String[] data;
        public MyGridViewAdapter(String[] data)
        {
            this.data = data;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if(convertView==null){
                convertView = new TextView(mContext);
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                        AbsListView.LayoutParams.FILL_PARENT, 200);

                convertView.setLayoutParams(params);
                TextView view = (TextView) convertView;
                view.setTextColor(Color.BLACK);
                view.setTextSize(20);
                convertView.setBackgroundColor(Color.GRAY);
            }

            ((TextView)convertView).setText(data[position]);
            ((TextView)convertView).setGravity(Gravity.CENTER);
            return convertView;
        }

    }

}
