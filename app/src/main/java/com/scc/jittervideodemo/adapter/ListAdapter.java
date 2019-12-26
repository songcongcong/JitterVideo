package com.scc.jittervideodemo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scc.jittervideodemo.MyVideoPlayer;
import com.scc.jittervideodemo.R;
import com.scc.jittervideodemo.base.BaseRecAdapter;
import com.scc.jittervideodemo.base.BaseRecViewHolder;

import java.util.List;

/**
 * @author
 * @data 2019/12/26
 */
public class ListAdapter extends BaseRecAdapter<String, ListAdapter.MyViewHolder> {


    public ListAdapter(List<String> list) {
        super(list);
    }

    @Override
    public void onHolder(MyViewHolder holder, String bean, int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        holder.mVideo.setUp(bean, "第" + position + "视频", MyVideoPlayer.STATE_NORMAL);
        if (position == 0) {
            holder.mVideo.startVideo();
        }
        Glide.with(context).load(bean).into(holder.mVideo.thumbImageView);
        holder.mTitle.setText("第" + position + "个视频");
    }

    @Override
    public MyViewHolder onCreateHolder() {
        return new MyViewHolder(getViewByRes(R.layout.list_item_page2_layout));
    }

    public class MyViewHolder extends BaseRecViewHolder {

        public final TextView mTitle;
        public final MyVideoPlayer mVideo;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.tv_title);
            mVideo = itemView.findViewById(R.id.mp_video);
        }
    }
}
