package com.scc.jittervideodemo.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.scc.jittervideodemo.MyVideoPlayer;
import com.scc.jittervideodemo.R;
import com.scc.jittervideodemo.adapter.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author
 * @data 2019/11/26
 */
public class ListFragment extends Fragment {
    /**
     *  recycleview + PagerSnapHelper(viewpager一样效果，一次只能滑动一页，而且居中显示)
     *  SnamHelper:辅助recycleview在滚动结束时将item对齐到某个位置
     */
    @BindView(R.id.page2_list_recyccleview)
    RecyclerView page2ListRecyccleview;
    private List<String> urlList;
    private LinearLayoutManager linearLayoutManager;
    private PagerSnapHelper pagerSnapHelper;
    private int currentPosition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_list_layout, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @SuppressLint("WrongConstant")
    private void  initView() {
        makeData();
        // 创建pagesnapHelper
        pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(page2ListRecyccleview); // 将pager依附于recycleview上

        ListAdapter listAdapter = new ListAdapter(urlList);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        page2ListRecyccleview.setLayoutManager(linearLayoutManager);
        page2ListRecyccleview.setAdapter(listAdapter);
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


    private void addListener() {
        page2ListRecyccleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE: // 停止滚动
                        // 该方法会找到linearLayoutManager上最接近对齐位置的那个view，叫SnapView,对应的position叫SnapPosition
                        // 如果返回null，就不会做对齐调整
                        View snapView = pagerSnapHelper.findSnapView(linearLayoutManager);
                        int position = recyclerView.getChildAdapterPosition(snapView); // 返回指定item的position
                        if (currentPosition != position) {
                            // 如果当前position和上一次固定的position相同， 说明就是同一个， 只不过滑动了一点点，又释放了
                            MyVideoPlayer.releaseAllVideos();
                            RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(snapView); // 得到子视图的viewholder
                            if (childViewHolder != null && childViewHolder instanceof ListAdapter.MyViewHolder) { // 判断holder是否是适配器中的
                                ((ListAdapter.MyViewHolder) childViewHolder).mVideo.startVideo(); // 通过viewHolder获取子视图的控件id，开启视频
                            }
                        }
                        currentPosition = position;
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING : // 拖动
                        break;
                    case  RecyclerView.SCROLL_STATE_SETTLING: // 惯性滑动
                        break;
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        MyVideoPlayer.releaseAllVideos();
    }
}
