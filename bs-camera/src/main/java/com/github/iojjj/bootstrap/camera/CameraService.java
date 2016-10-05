package com.github.iojjj.bootstrap.camera;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by cvetl on 11.08.2016.
 */

public interface CameraService {

    void captureImage(@NonNull String filePath);

    void captureImage(@NonNull File file);

    void captureVideo(@NonNull String filePath, @NonNull VideoCaptureOptions captureOptions);

    void captureVideo(@NonNull File file, @NonNull VideoCaptureOptions captureOptions);

    CameraOptions getCameraOptions();

    CameraOptions getCameraOptions(@NonNull CameraView.Facing facing);

    void setAutoExposureMode(@NonNull CameraView.AutoExposureMode autoExposureMode);

    void setAutoFocusMode(@NonNull CameraView.AutoFocusMode autoFocusMode);

    void setCameraFacing(@NonNull CameraView.Facing facing);
}
