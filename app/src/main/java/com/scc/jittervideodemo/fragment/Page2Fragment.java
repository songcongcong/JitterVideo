package com.scc.jittervideodemo.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.scc.jittervideodemo.MyVideoPlayer;
import com.scc.jittervideodemo.R;
import com.scc.jittervideodemo.adapter.Page2Adapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author
 * @data 2019/11/26
 */
public class Page2Fragment extends Fragment {
    @BindView(R.id.page2_recycleview)
    RecyclerView page2Recycleview;
    private List<String> urlList;
    private int lastVisibleItemPosition;
    private int firstVisibleItemPosition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_page2_layout, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    // 适配数据
    private void initView() {
        makeData();
        Page2Adapter page2Adapter = new Page2Adapter(urlList);
        page2Recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        page2Recycleview.setAdapter(page2Adapter);
        addListener();
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

    /**
     * 点击监听
     */
    private void addListener() {
        // recycleview滚动监听
        page2Recycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                // 判断recycleview的滑动时机
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE: // 停止滚动
                        // 视频的自动播放与停止
                        autoPlayVideo(page2Recycleview);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING: // 拖动
                        autoPlayVideo(page2Recycleview);
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING: // 惯性滑动
                        MyVideoPlayer.releaseAllVideos();
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition(); // 获取第一个position
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition(); // 获取最后一个position            }

            }
        });
    }

    /**
     * 自动播放
     *
     * @param recyclerView recyclerView
     */
    private void autoPlayVideo(RecyclerView recyclerView) {
        if (firstVisibleItemPosition == 0 && lastVisibleItemPosition == 0 && recyclerView.getChildAt(0) != null) {
            MyVideoPlayer videoView = null;
            if (recyclerView != null && recyclerView.getChildAt(0) != null) {
                videoView = recyclerView.getChildAt(0).findViewById(R.id.page2_video);
            }
            if (videoView != null) {
                if (videoView.state == MyVideoPlayer.STATE_NORMAL
                        || videoView.state == MyVideoPlayer.STATE_PAUSE) {
                    videoView.startVideo(); // 自动播放第一个
                }
            }
        }

        for (int i = 0; i < lastVisibleItemPosition; i++) {
            if (recyclerView == null || recyclerView.getChildAt(i) == null) {
                return;
            }
            MyVideoPlayer videoView = recyclerView.getChildAt(0).findViewById(R.id.page2_video);
            if (videoView != null) {
                Rect rect = new Rect();
                // 获取试图本身的可见坐标，把值传入到rect对象中
                videoView.getLocalVisibleRect(rect); // 以目标view左上角为参考系，等于0全部可见；大于或不等于0时，要么部分可见，要么全部不可见

                int height = videoView.getHeight();// 获取视频的高度
                if (rect.top <= 100 && rect.bottom >= height) {
                    if (videoView.state == MyVideoPlayer.STATE_NORMAL
                            || videoView.state == MyVideoPlayer.STATE_PAUSE) {
                        videoView.startVideo();
                    }
                    return;
                }

                MyVideoPlayer.releaseAllVideos(); // 释放掉资源
            } else {
                MyVideoPlayer.releaseAllVideos(); // 释放掉资源
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MyVideoPlayer.releaseAllVideos();
    }


}
