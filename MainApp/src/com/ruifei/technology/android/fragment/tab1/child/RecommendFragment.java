package com.ruifei.technology.android.fragment.tab1.child;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ruifei.technology.android.R;
import com.ruifei.technology.android.data.source.PullData;
import com.ruifei.framework.fragment.BaseFragment;
import com.zhy.magicviewpager.transformer.RotateYTransformer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhaofei.xie on 2016/10/5.
 */
public class RecommendFragment extends BaseFragment {

    private PullToRefreshListView pullToRefresh;
    private myAdapter adapter;
    private List<PullData> dataSource;
    private View headView;
    private View footView;
    private ViewPager mViewPager;
    private FrameLayout container;
    private myViewPagerAdapter viewPagerAdapter;
    private List<View> data;
    @Override
    public void initUi() {
        pullToRefresh = (PullToRefreshListView)findViewById(R.id.listview);
        initData();
        super.initUi();
    }

    @Override
    public void loadData() {
        adapter = new myAdapter(dataSource);
        pullToRefresh.setAdapter(adapter);
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        init();
        pullToRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        super.loadData();
    }

    @Override
    public int getContainerLayoutId() {
        return R.layout.recommend_fragment_lay;
    }

    private class myAdapter extends BaseAdapter
    {
        List<PullData> list;
        public myAdapter(List<PullData> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, android.view.View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LinearLayout.inflate(mContext,R.layout.data_item,null);
                ViewHolder holder = new ViewHolder();
                holder.title = (TextView)convertView.findViewById(R.id.title);
                holder.sig = (TextView)convertView.findViewById(R.id.sig);
                holder.pic = (ImageView)convertView.findViewById(R.id.pic);
                convertView.setTag(holder);
            }
            ViewHolder holder = (ViewHolder)convertView.getTag();
            holder.title.setText(list.get(position).title);
            holder.sig.setText(list.get(position).sig);
            holder.pic.setImageResource(list.get(position).pic);
            return convertView;
        }
    }

    private class ViewHolder
    {
        TextView title;
        TextView sig;
        ImageView pic;
    }


    private void init()
    {
        ILoadingLayout startLabels = pullToRefresh
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
    }

    private void initData()
    {
        dataSource = new LinkedList<PullData>();
        int[] pic = new int[]{R.drawable.grade,R.drawable.img2,R.drawable.img3,R.drawable.img4,R.drawable.img5,R.drawable.grade,R.drawable.img2,R.drawable.img3,R.drawable.img4};
        String[] title = new String[]{"刘德华","张学友","郭富城","周星驰","梁朝伟","张学友","郭富城","周星驰","梁朝伟"};
        String[] sig = new String[]{"真理惟一可靠的标准就是永远自相符合。",
                "时间是一切财富中最宝贵的财富",
                "真正的科学家应当是个幻想家；谁不是幻想家，谁就只能把自己称为实践家。",
                "人生并不像火车要通过每个站似的经过每一个生活阶段。人生总是直向前行走，从不留下什么。",
                "爱情只有当它是自由自在时，才会叶茂花繁。认为爱情是某种义务的思想只能置爱情于死地。只消一句话：你应当爱某个人，就足以使你对这个人恨之入骨。",
                "时间是一切财富中最宝贵的财富",
                "真正的科学家应当是个幻想家；谁不是幻想家，谁就只能把自己称为实践家。",
                "人生并不像火车要通过每个站似的经过每一个生活阶段。人生总是直向前行走，从不留下什么。",
                "爱情只有当它是自由自在时，才会叶茂花繁。认为爱情是某种义务的思想只能置爱情于死地。只消一句话：你应当爱某个人，就足以使你对这个人恨之入骨。"};

        for(int i=0;i<9;i++){
            PullData data = new PullData(title[i],sig[i],pic[i]);
            dataSource.add(data);
        }

        headView = LinearLayout.inflate(getContext(),R.layout.container_lay,null);
        container = (FrameLayout) headView.findViewById(R.id.pager_lay);
        mViewPager = new ViewPager(mContext);
        data = new LinkedList<View>();
        for(int i=0;i<5;i++){
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setImageResource(pic[i]);
            data.add(iv);
        }
        viewPagerAdapter = new myViewPagerAdapter(data);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setCurrentItem(2);
        mViewPager.setPageMargin(40);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(true, new RotateYTransformer(45));
        container.addView(mViewPager);
        ListView listView = pullToRefresh.getRefreshableView();
        listView.addHeaderView(headView);

    }

    private class myViewPagerAdapter extends PagerAdapter
    {
        List<View> data;
        public myViewPagerAdapter(List<View> data)
        {
            this.data = data;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(data.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(data.get(position), 0);
            return data.get(position);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


}
