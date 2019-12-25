package com.scc.jittervideodemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.scc.jittervideodemo.R;
import com.scc.jittervideodemo.VerticalViewPager2;
import com.scc.jittervideodemo.adapter.VerticalViewPagerAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author
 * @data 2019/11/26
 */
public class PageFragment extends Fragment {
    @BindView(R.id.page_viewpager)
    VerticalViewPager2 pageViewpager;
    @BindView(R.id.page_smart)
    SmartRefreshLayout pageSmart;
    private ArrayList<String> urlList;
    private VerticalViewPagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_page_layout, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        addSmartListener();
    }

    private void addSmartListener() {
        pageSmart.setEnableAutoLoadMore(false);
        pageSmart.setEnableLoadMore(false);

        //刷新监听
        pageSmart.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageSmart.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 加载更多，跟适配器添加数据
                        urlList.addAll(urlList);
                        pagerAdapter.setmList(urlList);
                        pagerAdapter.notifyDataSetChanged();

                        pageSmart.finishLoadMore(); // 停止加载更多
                    }
                }, 2000);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }
        });
    }



    private void initView(){
        makeData();
        pagerAdapter = new VerticalViewPagerAdapter(getFragmentManager());
        pageViewpager.setOffscreenPageLimit(10); //缓存fragment的页面
        pagerAdapter.setmList(urlList); // 给适配器添加数据
        pageViewpager.setAdapter(pagerAdapter); // 设置adapter

        // viewpager的点击事件
        pageViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == urlList.size() - 1) {
                    pageSmart.setEnableAutoLoadMore(true); //设置是否监听列表在滚动到底部时触发加载事件
                    pageSmart.setEnableLoadMore(true);
                } else {
                    pageSmart.setEnableAutoLoadMore(false); //设置是否监听列表在滚动到底部时触发加载事件
                    pageSmart.setEnableLoadMore(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    // 适配数据
    private void makeData() {
        urlList = new ArrayList<>();
        urlList.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        urlList.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        urlList.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        urlList.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        urlList.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        urlList.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        urlList.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        urlList.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
    }
}
