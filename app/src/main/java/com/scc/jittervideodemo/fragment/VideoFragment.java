package com.scc.jittervideodemo.fragment;

import android.view.View;
import android.widget.RelativeLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.scc.jittervideodemo.MyVideoPlayer;
import com.scc.jittervideodemo.R;

import butterknife.BindView;

/**
 * @author
 * @data 2019/11/26
 */
public class VideoFragment extends BaseFragment {
    @BindView(R.id.video_player)
    MyVideoPlayer videoPlayer;
    @BindView(R.id.rl_back_right)
    RelativeLayout rlBackRight;
    @BindView(R.id.video_drawer)
    DrawerLayout videoDrawer;
    public static final String URL = "URL";
    private String url;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vieo_layout;
    }

    @Override
    protected void initView() {
        // 获取URL
        url = getArguments().getString(URL);
        // 加载URL
        Glide.with(getContext())
                .load(url)
                .into(videoPlayer.thumbImageView);
        videoPlayer.rl_touch_help.setVisibility(View.GONE);
        videoPlayer.setUp(url, url);  // 使用jiaozivideoplayer 播放器
    }

    @Override
    protected void loadData() {
        videoPlayer.startVideo(); // 开始播放
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (videoPlayer == null) {
            return;
        }
        if (isVisibleToUser) {  // 如果当前fragment显示就播放
            videoPlayer.goOnPlayOnResume();
        } else { // 不显示就停止播放
            videoPlayer.goOnPlayOnPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoPlayer != null) {
            videoPlayer.goOnPlayOnResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoPlayer != null) {
            videoPlayer.goOnPlayOnPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoPlayer != null) {
            videoPlayer.releaseAllVideos();
        }
    }
}
