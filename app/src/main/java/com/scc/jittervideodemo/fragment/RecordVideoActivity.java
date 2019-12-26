package com.scc.jittervideodemo.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.scc.jittervideodemo.MyVideoPlayer;
import com.scc.jittervideodemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordVideoActivity extends AppCompatActivity {

    @BindView(R.id.jd_view)
    MyVideoPlayer jdView;
    public static final String VIDEO_PATH = "VIDEO_PATH";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        String path = getIntent().getStringExtra(VIDEO_PATH);
        if (!TextUtils.isEmpty(path)) {
            try {
                Log.d("song","视频路径："+path);
                jdView.setUp(path, path, MyVideoPlayer.STATE_NORMAL);
                jdView.startVideo();

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("song","视频路径异常："+e.toString());
            }
        }
    }
}
