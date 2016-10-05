package com.github.iojjj.bootstrap.camera;

import android.support.annotation.NonNull;
import android.util.Size;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cvetl on 20.08.2016.
 */
public class CameraOptions {
    private final List<CameraView.AutoExposureMode> mSupportedAEModes = new ArrayList<>();
    private final List<CameraView.AutoFocusMode> mSupportedAFModes = new ArrayList<>();
    private final List<Size> mSupportedPhotoSizes = new ArrayList<>();
    private final List<Size> mSupportedVideoSizes = new ArrayList<>();
    private CameraView.Facing mFacing;
    private boolean mFlashAvailable;
    private List<CameraView.AutoExposureMode> mSupportedAEModesUnmod = Collections.unmodifiableList(mSupportedAEModes);
    private List<CameraView.AutoFocusMode> mSupportedAFModesUnmod = Collections.unmodifiableList(mSupportedAFModes);
    private List<Size> mSupportedPhotoSizesUnmod = Collections.unmodifiableList(mSupportedPhotoSizes);
    private List<Size> mSupportedVideoSizesUnmod = Collections.unmodifiableList(mSupportedVideoSizes);

    private CameraOptions() {
        //no instance
    }

    @NonNull
    public CameraView.Facing getFacing() {
        return mFacing;
    }

    CameraOptions setFacing(CameraView.Facing facing) {
        mFacing = facing;
        return this;
    }

    @NonNull
    public List<CameraView.AutoExposureMode> getSupportedAutoExposureModes() {
        return mSupportedAEModesUnmod;
    }

    @NonNull
    public List<CameraView.AutoFocusMode> getSupportedAutoFocusModes() {
        return mSupportedAFModesUnmod;
    }

    CameraOptions setSupportedAutoFocusModes(@NonNull List<CameraView.AutoFocusMode> autoFocusModes) {
        mSupportedAFModes.clear();
        mSupportedAFModes.addAll(autoFocusModes);
        mSupportedAFModesUnmod = Collections.unmodifiableList(mSupportedAFModes);
        return this;
    }

    @NonNull
    public List<Size> getSupportedPhotoSizes() {
        return mSupportedPhotoSizesUnmod;
    }

    CameraOptions setSupportedPhotoSizes(@NonNull Size[] sizes) {
        mSupportedPhotoSizes.clear();
        Collections.addAll(mSupportedPhotoSizes, sizes);
        mSupportedPhotoSizesUnmod = Collections.unmodifiableList(mSupportedPhotoSizes);
        return this;
    }

    @NonNull
    public List<Size> getSupportedVideoSizes() {
        return mSupportedVideoSizesUnmod;
    }

    CameraOptions setSupportedVideoSizes(@NonNull Size[] sizes) {
        mSupportedVideoSizes.clear();
        Collections.addAll(mSupportedVideoSizes, sizes);
        mSupportedVideoSizesUnmod = Collections.unmodifiableList(mSupportedVideoSizes);
        return this;
    }

    public boolean isFlashAvailable() {
        return mFlashAvailable;
    }

    CameraOptions setFlashAvailable(boolean flashAvailable) {
        mFlashAvailable = flashAvailable;
        return this;
    }

    static CameraOptions create() {
        return new CameraOptions();
    }

    CameraOptions setSupportedFlashModes(@NonNull List<CameraView.AutoExposureMode> autoExposureModes) {
        mSupportedAEModes.clear();
        mSupportedAEModes.addAll(autoExposureModes);
        mSupportedAEModesUnmod = Collections.unmodifiableList(mSupportedAEModes);
        return this;
    }
}
