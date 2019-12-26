package com.scc.jittervideodemo.fragment;

import android.Manifest;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.scc.jittervideodemo.R;
import com.scc.jittervideodemo.utils.AppUtils;

import java.io.File;
import java.sql.NClob;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author
 * @data 2019/11/26
 */
public class RecodeFragment extends Fragment implements SurfaceHolder.Callback {
    @BindView(R.id.recode_surview)
    SurfaceView recodeSurview;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_switch)
    Button btnSwitch;
    @BindView(R.id.btn_end)
    Button btnEnd;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    private SurfaceHolder surfaceHolder;
    private MediaRecorder mediaRecorder;
    private File tempFile;
    //当前打开的摄像头标记 1--后，2--前
    private int currentCameraType = -1;
    private Camera mCamera;
    private static final long TIME_MAX = 15 * 1000;
    private Camera.Size mSize;
    private static final long TIME_INTERVAL = 500;
    private MyTimer myTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_recode_layout, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestPermission();
//        startPreview();
    }



    private void initView() {
        surfaceHolder = recodeSurview.getHolder();
        surfaceHolder.addCallback(this);
        // 设置一些参数，方便后面绘图
        recodeSurview.setFocusable(true);
        recodeSurview.setKeepScreenOn(true); // 保持屏幕常亮
        recodeSurview.setFocusableInTouchMode(true); // 能否通过触摸获得焦点

        // 设置seekBar的最大数
        seekBar.setMax(100);
        seekBar.setProgress(0);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        requestPermission(); // 申请权限
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int i, int i1, int i2) {
        surfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // 停止预览并释放摄像头资源
        stopPreview();
        // 停止录制
        stopRecord(false);
    }

    @OnClick({R.id.btn_start, R.id.btn_switch, R.id.btn_end})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start: // 开始录制
                startRecord();
                break;
            case R.id.btn_switch: // 切换前后摄像头
                stopPreview();
                if (currentCameraType == 1) { // 前摄像头
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    currentCameraType = 2;
                    btnSwitch.setText("前");
                } else {
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    currentCameraType = 1;
                    btnSwitch.setText("后");
                }
                startPreview();
                break;
            case R.id.btn_end: // 停止录制
                stopRecord(false);
                break;
        }
    }

    /**
     * 开始录制
     */
    private void startRecord() {
        if(mediaRecorder == null) {
            mediaRecorder = new MediaRecorder(); // 用于录音录像
        }
        // 创建本地路径
        tempFile = creatFile();

        try {
            mCamera.unlock(); // 解锁camera，让mediaRecorder访问camera的类
            mediaRecorder.setCamera(mCamera); // 将摄像头赋给录音录像控件
            // 从相机采集视频
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            // 从麦克采集音频信息
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置在录制过程中产生的输出文件格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            // 设置相机编码格式
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            // 设置音频编码格式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // 设置录制视频的宽高
            mediaRecorder.setVideoSize(mSize.width, mSize.height);
            // 设置每秒的帧数
            mediaRecorder.setVideoFrameRate(24);
            // 设置帧频率， 然后就清晰了
            mediaRecorder.setVideoEncodingBitRate(1 * 1024 * 1024 * 100);

            mediaRecorder.setOutputFile(tempFile.getAbsolutePath());
            mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
            // 解决录制视频，播放器横向问题
            if (currentCameraType == 1) {
                // 后置
                mediaRecorder.setOrientationHint(90);
            } else {
                // 前置
                mediaRecorder.setOrientationHint(270);
            }
            mediaRecorder.prepare(); // 录音录像开始准备

            try {
                mediaRecorder.start(); // 开始录制
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("song", "开始录制：" + e.toString());
            }
            // 设置进度条--定时器改变进度
            myTimer = new MyTimer(TIME_MAX, TIME_INTERVAL);
            myTimer.start();
            showtoast("开始录制");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("song", "开始录制异常：" + e.toString());

        }
    }

    /**
     * 停止录制
     * @param delete  delete
     */
    private void stopRecord(boolean delete) {
        if (mediaRecorder == null) {
            return;
        }
        if (myTimer != null) {
            myTimer.cancel();
        }
        try {
            try {
                mediaRecorder.stop();

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("song", "2222结束录制异常：" + e.toString());
            }
            mediaRecorder.reset(); // 先重置 mediaRecorder
            mediaRecorder.release(); //释放资源
            mediaRecorder = null; // 设置为null
            if (mCamera != null) {
                mCamera.lock(); // 锁定相机以备后续使用
            }

            if(delete) { // 用于判断是否删除本地文件（录制视频的）
                if (tempFile != null &&tempFile.exists()) {
                    tempFile.delete();
                }
            } else {
                stopPreview(); // 停止预览
                // 跳转页面播放已录制好的视频
                Intent intent = new Intent(getActivity(), RecordVideoActivity.class);
                intent.putExtra(RecordVideoActivity.VIDEO_PATH, tempFile.getAbsolutePath());
                startActivity(intent);
            }
            showtoast("停止录制");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("song", "结束录制异常：" + e.toString());

        }
    }

    /**
     * 开始预览
     */
    private void startPreview(){
        if (recodeSurview == null || surfaceHolder == null) {
            return;
        }

        if (mCamera == null) { // 第一次打开摄像头（后置）
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            currentCameraType = 1;
            btnSwitch.setText("后");
        }
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            Camera.Parameters parameters = mCamera.getParameters();
            // 设置预览旋转90度
            mCamera.setDisplayOrientation(90);
            // 实现camera自动对焦
            List<String> focusModes = parameters.getSupportedFocusModes();
            if(focusModes != null) {
                for (String mode : focusModes) {
                    mode.contains("continuous-video");
                    parameters.setFocusMode("continuous-video");                }
            }
            List<Camera.Size> videoSizes = parameters.getSupportedVideoSizes();
            if (videoSizes.size() > 0) {
                mSize = videoSizes.get(videoSizes.size() - 1);
            }

            try {
                // 将参数设置给相机
              mCamera.setParameters(parameters);
              mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("song","报出异常："+e.toString());
            }
        } catch (Exception e) {
          e.printStackTrace();
            Log.d("song","开始报出异常："+e.toString());

        }
    }

    /**
     * 停止预览并释放摄像头资源
     */
    private void stopPreview() {
        if(mCamera == null) {
            return;
        }
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }


    /**
     * 创建本地路径
     * @return File
     */
    private File creatFile() {
        // 获取sdCard路径
        String path = Environment.getExternalStorageDirectory() + "/jitterdemo/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir(); // 判断文件是否存在，不存在就创建文件夹
        }
        File tempPath = new File(path + System.currentTimeMillis() + ".mp4");
        return tempPath;
    }


    /**
     * 创建定时器，用来改变进度
     */
    public class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            // 开始录制视频改变进度条
            int  progress = (int) ((TIME_MAX - l ) / (double) TIME_MAX * 100);
            Log.e("change", "millisUntilFinished=" + progress);
            seekBar.setProgress(progress);
        }

        @Override
        public void onFinish() {
            stopRecord(false);
        }
    }

    /**弹出Toast
     * @param s s
     */
    public void showtoast(@NonNull String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopPreview();
        stopRecord(true);
    }

    /**
     * 检查权限
     */
    private void requestPermission() {
         if (AppUtils.checkAndApplyfPermissionActivity(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
         Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 1)) {
             startPreview();
         }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (AppUtils.checkPermission(grantResults)) {
                startPreview();
            } else {
                getActivity().finish();
            }
        }
    }
}
