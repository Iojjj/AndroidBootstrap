package com.github.iojjj.bootstrap.camera;

import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by cvetl on 11.08.2016.
 */

@SuppressWarnings("deprecation")
class Camera1Handler {

    @Nullable
    public String getCameraId(@NonNull CameraView.Facing facing) {
        final int numberOfCameras = Camera.getNumberOfCameras();
        final Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (facing == CameraView.Facing.FRONT && cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT ||
                    facing == CameraView.Facing.BACK && cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return String.valueOf(i);
            }
        }
        return null;
    }

    public boolean isBackCameraSupported() {
        return getCameraId(CameraView.Facing.BACK) != null;
    }

    public boolean isFrontCameraSupported() {
        return getCameraId(CameraView.Facing.FRONT) != null;
    }
}
