package com.scc.jittervideodemo.adapter;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.scc.jittervideodemo.MyVideoPlayer;
import com.scc.jittervideodemo.R;
import com.scc.jittervideodemo.base.BaseRecAdapter;
import com.scc.jittervideodemo.base.BaseRecViewHolder;

import java.util.List;

/**
 * @author
 * @data 2019/12/25
 */
public class Page2Adapter extends BaseRecAdapter<String, Page2Adapter.MyViewHolder> {


    public Page2Adapter(List<String> list) {
        super(list);
    }


    @Override
    public void onHolder(MyViewHolder holder, String bean, int position) {
        try {
            holder.mVideo.setUp(bean, "第" + position + "个视频", MyVideoPlayer.STATE_NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("song","崩潰："+e.toString());
        }
        if (position == 0) {
            holder.mVideo.startVideo(); // 如果是第一个就开始播放视频
        }
        Glide.with(context).load(bean).into(holder.mVideo.thumbImageView);
        //添加标题
        holder.mTitle.setText("第" + position + "个视频");
    }

    @Override
    public MyViewHolder onCreateHolder() {
        return new MyViewHolder(getViewByRes(R.layout.page2_item_layout));
    }
    public class MyViewHolder extends BaseRecViewHolder {

        private final TextView mTitle;
        private final MyVideoPlayer mVideo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.page2_title);
            mVideo = itemView.findViewById(R.id.page2_video);
        }
    }

}
