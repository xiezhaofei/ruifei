package com.ruifei.android.fragment.tab1.child;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ruifei.android.R;
import com.ruifei.android.data.source.PullData;
import com.ruifei.framework.fragment.BaseFragment;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhaofei.xie on 2016/10/5.
 */
public class RecommendFragment extends BaseFragment {

    private PullToRefreshListView pullToRefresh;
    private myAdapter adapter;
    private List<PullData> dataSource;

    @Override
    public void initUi() {
        pullToRefresh = (PullToRefreshListView)findViewById(R.id.listview);
        dataSource = new LinkedList<PullData>();
        for(int i=0;i<5;i++){
            PullData data = new PullData("ruifeitec","android technology",R.drawable.reifeilog);
            dataSource.add(data);
        }
        adapter = new myAdapter(dataSource);

        super.initUi();
    }

    @Override
    public void loadData() {
        pullToRefresh.setAdapter(adapter);
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        init();
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

//      // 设置下拉刷新文本
//      pullToRefresh.getLoadingLayoutProxy(false, true)
//              .setPullLabel("上拉刷新...");
//      pullToRefresh.getLoadingLayoutProxy(false, true).setReleaseLabel(
//              "放开刷新...");
//      pullToRefresh.getLoadingLayoutProxy(false, true).setRefreshingLabel(
//              "正在加载...");
//      // 设置上拉刷新文本
//      pullToRefresh.getLoadingLayoutProxy(true, false)
//              .setPullLabel("下拉刷新...");
//      pullToRefresh.getLoadingLayoutProxy(true, false).setReleaseLabel(
//              "放开刷新...");
//      pullToRefresh.getLoadingLayoutProxy(true, false).setRefreshingLabel(
//              "正在加载...");
    }

}
