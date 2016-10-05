package com.github.iojjj.bootstrap;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.github.iojjj.bootstrap.camera.CameraOptions;
import com.github.iojjj.bootstrap.camera.CameraService;
import com.github.iojjj.bootstrap.camera.CameraView;
import com.github.iojjj.bootstrap.camera.VideoCaptureOptions;

import java.io.File;

/**
 * Created by cvetl on 12.08.2016.
 */

public class CameraFragment extends Fragment {

    private CameraView.AutoExposureMode mAutoExposureMode = CameraView.AutoExposureMode.ON_AUTO;
    private ImageButton mBtnCaptureImage;
    private ImageButton mBtnCaptureVideo;
    private ImageButton mBtnSwitchCamera;
    private ImageButton mBtnSwitchFlash;
    private CameraService mCameraService;
    private CameraView mCameraView;
    private CameraView.Facing mFacing = CameraView.Facing.BACK;

    public static CameraFragment newInstance() {
        Bundle args = new Bundle();
        CameraFragment fragment = new CameraFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_camera, container, false);
        mCameraView = (CameraView) view.findViewById(R.id.camera_view);
        mCameraService = mCameraView.getCameraService();
        mBtnSwitchCamera = (ImageButton) view.findViewById(R.id.btn_switch_camera);
        mBtnSwitchFlash = (ImageButton) view.findViewById(R.id.btn_switch_flash);
        mBtnCaptureImage = (ImageButton) view.findViewById(R.id.btn_capture_photo);
        mBtnCaptureVideo = (ImageButton) view.findViewById(R.id.btn_capture_video);
        return view;
    }

    @Override
    public void onPause() {
        mCameraView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCameraView.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnSwitchCamera.setOnClickListener(v -> {
            final CameraView.AutoFocusMode autoFocusMode = mFacing == CameraView.Facing.FRONT ? CameraView.AutoFocusMode.CONTINUOUS_PICTURE : CameraView.AutoFocusMode.OFF;
            mFacing = mFacing == CameraView.Facing.FRONT ? CameraView.Facing.BACK : CameraView.Facing.FRONT;
            mCameraService.setCameraFacing(mFacing);
            final CameraOptions cameraOptions = mCameraService.getCameraOptions();
            if (cameraOptions.getSupportedAutoExposureModes().contains(mAutoExposureMode)) {
                mCameraService.setAutoExposureMode(mAutoExposureMode);
            }
            if (cameraOptions.getSupportedAutoFocusModes().contains(autoFocusMode)) {
                mCameraService.setAutoFocusMode(autoFocusMode);
            }
            if (mFacing == CameraView.Facing.FRONT) {
                mBtnSwitchCamera.setImageResource(R.drawable.ic_camera_rear_white_24dp);
                mBtnSwitchFlash.setVisibility(View.GONE);
            } else if (mFacing == CameraView.Facing.BACK) {
                mBtnSwitchCamera.setImageResource(R.drawable.ic_camera_front_white_24dp);
                mBtnSwitchFlash.setVisibility(View.VISIBLE);
            }
        });
        mBtnSwitchFlash.setOnClickListener(v -> {
            if (mAutoExposureMode == CameraView.AutoExposureMode.ON_AUTO) {
                mAutoExposureMode = CameraView.AutoExposureMode.ON;
                mBtnSwitchFlash.setImageResource(R.drawable.ic_flash_on_white_24dp);
            } else if (mAutoExposureMode == CameraView.AutoExposureMode.ON) {
                mAutoExposureMode = CameraView.AutoExposureMode.OFF;
                mBtnSwitchFlash.setImageResource(R.drawable.ic_flash_off_white_24dp);
            } else if (mAutoExposureMode == CameraView.AutoExposureMode.OFF) {
                mAutoExposureMode = CameraView.AutoExposureMode.ON_AUTO;
                mBtnSwitchFlash.setImageResource(R.drawable.ic_flash_auto_white_24dp);
            }
            mCameraService.setAutoExposureMode(mAutoExposureMode);
        });
        mBtnCaptureImage.setOnClickListener(v -> {
            File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
            mCameraService.captureImage(file);
        });
        mBtnCaptureVideo.setOnClickListener(v -> {
            File file = new File(Environment.getExternalStorageDirectory(), "test.mp4");
            mCameraService.captureVideo(file, new VideoCaptureOptions.Builder()
                    .setAudioChannels(2)
                    .setAudioSamplingRate(44100)
                    .setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
                    .setVideoEncoder(MediaRecorder.VideoEncoder.H264)
                    .setVideoEncodingBitrate(10_000_000)
                    .setVideoFrameRate(30)
                    .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    .build());
        });
    }
}
