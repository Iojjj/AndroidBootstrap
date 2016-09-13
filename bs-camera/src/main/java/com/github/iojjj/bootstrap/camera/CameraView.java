package com.github.iojjj.bootstrap.camera;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by cvetl on 11.08.2016.
 */

public class CameraView extends FrameLayout {

    private Camera2Handler mCameraHandler;

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CameraView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        final CameraTextureView textureView = new CameraTextureView(getContext());
        final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        textureView.setLayoutParams(params);
        addView(textureView);
        mCameraHandler = new Camera2Handler(getContext(), textureView);
        // TODO: 20.08.2016 get from attributes
        Facing facing = Facing.BACK;
        if (!mCameraHandler.isBackCameraSupported()) {
            if (!mCameraHandler.isFrontCameraSupported()) {
                throw new IllegalArgumentException("There are no cameras on device.");
            }
            facing = Facing.FRONT;
        }
        final CameraOptions cameraOptions = mCameraHandler.getCameraOptions(facing);
        mCameraHandler.setCameraFacing(facing);
        if (cameraOptions.getSupportedAutoFocusModes().contains(AutoFocusMode.CONTINUOUS_PICTURE)) {
            mCameraHandler.setAutoFocusMode(AutoFocusMode.CONTINUOUS_PICTURE);
        } else if (!cameraOptions.getSupportedAutoFocusModes().isEmpty()) {
            mCameraHandler.setAutoFocusMode(cameraOptions.getSupportedAutoFocusModes().get(0));
        }
        if (cameraOptions.getSupportedAutoExposureModes().contains(AutoExposureMode.ON_AUTO)) {
            mCameraHandler.setAutoExposureMode(AutoExposureMode.ON_AUTO);
        } else if (!cameraOptions.getSupportedAutoExposureModes().isEmpty()) {
            mCameraHandler.setAutoExposureMode(cameraOptions.getSupportedAutoExposureModes().get(0));
        }
    }

    public void onResume() {
        mCameraHandler.onResume();
    }

    public void onPause() {
        mCameraHandler.onPause();
    }
    
    @NonNull
    public CameraService getCameraService() {
        return mCameraHandler;
    }

    public enum Facing {
        BACK,
        FRONT
    }

    public enum AutoFocusMode {
        AUTO,
        CONTINUOUS_PICTURE,
        CONTINUOUS_VIDEO,
        EDOF,
        MACRO,
        OFF
    }

    public enum AutoExposureMode {
        ON,
        OFF,
        ON_AUTO,
        ON_ALWAYS,
        ON_AUTO_REDEYE
    }

    public enum HotPixelsMode {
        OFF,
        FAST,
        HIGH_QUALITY
    }

    public enum ColorCorrectionAberrationMode {
        OFF,
        FAST,
        HIGH_QUALITY
    }

    public enum AeAntibandingMode {
        OFF,
        _50HZ,
        _60HZ,
        AUTO
    }

    public enum EffectMode {
        OFF,
        MONO,
        NEGATIVE,
        SOLARIZE,
        SEPIA,
        POSTERIZE,
        WHITEBOARD,
        BLACKBOARD,
        AQUA
    }
}
