package com.github.iojjj.bootstrap.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;

import com.github.iojjj.bootstrap.core.BSAssertions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by cvetl on 11.08.2016.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class Camera2Handler implements CameraService {

    /**
     * Max preview width that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_WIDTH = 1920;

    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_HEIGHT = 1080;

    private static final int STATE_PREVIEW = 0;
    private static final int STATE_WAITING_LOCK = 1;
    private static final int STATE_PICTURE_TAKEN = 2;
    private static final int STATE_WAITING_PRE_CAPTURE = 3;
    private static final int STATE_WAITING_NON_PRE_CAPTURE = 4;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.put(Surface.ROTATION_0, 90);
        ORIENTATIONS.put(Surface.ROTATION_90, 0);
        ORIENTATIONS.put(Surface.ROTATION_180, 270);
        ORIENTATIONS.put(Surface.ROTATION_270, 180);
    }


    @NonNull
    private final Context mContext;
    private final CameraManager mCameraManager;
    private CameraTextureView mTextureView;
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener;
    @Nullable
    private CameraCallback mCameraCallback;
    private Semaphore mOpenCloseCameraLock = new Semaphore(1);
    private String mCameraId;
    private CameraDevice.StateCallback mStateCallback;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCaptureSession;
    private Size mPreviewSize;
    private CaptureCallback mCaptureCallback;
    private int mSensorOrientation;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private ImageReader mImageReader;
    private File mImageFile;
    private int mState = STATE_PREVIEW;

    private int mAutoExposure = CameraMetadata.CONTROL_AF_MODE_OFF;
    private int mAutoFocus = CameraMetadata.CONTROL_AF_MODE_OFF;
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener =
            reader -> mBackgroundHandler.post(() -> saveImage(reader.acquireNextImage()));


    Camera2Handler(@NonNull Context context, @NonNull CameraTextureView textureView) {
        BSAssertions.assertNotNull(context, "context");
        BSAssertions.assertNotNull(textureView, "textureView");
        mContext = context.getApplicationContext();
        mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        mTextureView = textureView;
        mSurfaceTextureListener = new CameraSurfaceListener();
        mStateCallback = new CameraStateCallback();
        mCaptureCallback = new CameraCaptureCallback();
    }

    void onResume() {
        startBackgroundThread();
        acquireCamera();
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void acquireCamera() {
        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    void onPause() {
        releaseCamera();
        stopBackgroundThread();
    }

    private void releaseCamera() {
        try {
            mOpenCloseCameraLock.acquire();
            if (mCaptureSession != null) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (mImageReader != null) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            if (mCameraCallback != null) {
                mCameraCallback.onCantLockCamera();
            }
        } finally {
            mOpenCloseCameraLock.release();
        }
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join(1000);
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void openCamera(int width, int height) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (mCameraCallback != null) {
                mCameraCallback.onPermissionsNotGranted();
            }
            return;
        }
        setupCameraParams(width, height);
        applyTextureTransformation(width, height);
        mBackgroundHandler.post(this::openCameraAsync);
    }

    @SuppressWarnings("MissingPermission")
    private void openCameraAsync() {
        try {
            if (!mOpenCloseCameraLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                if (mCameraCallback != null) {
                    mCameraCallback.onCameraCantBeAcquired();
                }
                return;
            }
            mCameraManager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
        } catch (InterruptedException e) {
            e.printStackTrace();
            mOpenCloseCameraLock.release();
            if (mCameraCallback != null) {
                mCameraCallback.onCantLockCamera();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SwitchIntDef")
    @SuppressWarnings("SuspiciousNameCombination")
    private void setupCameraParams(int width, int height) {
        final CameraCharacteristics characteristics;
        try {
            characteristics = mCameraManager.getCameraCharacteristics(mCameraId);
            final StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if (map == null) {
                return;
            }
            final WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            final int displayRotation = windowManager.getDefaultDisplay().getRotation();
            //noinspection ConstantConditions
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            boolean swappedDimensions = false;
            switch (displayRotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_180:
                    if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                        swappedDimensions = true;
                    }
                    break;
                case Surface.ROTATION_90:
                case Surface.ROTATION_270:
                    if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                        swappedDimensions = true;
                    }
                    break;
            }

            final Point displaySize = new Point();
            windowManager.getDefaultDisplay().getSize(displaySize);
            int rotatedPreviewWidth = width;
            int rotatedPreviewHeight = height;
            int maxPreviewWidth = displaySize.x;
            int maxPreviewHeight = displaySize.y;
            if (swappedDimensions) {
                rotatedPreviewWidth = height;
                rotatedPreviewHeight = width;
                maxPreviewWidth = displaySize.y;
                maxPreviewHeight = displaySize.x;
            }
            if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                maxPreviewWidth = MAX_PREVIEW_WIDTH;
            }
            if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                maxPreviewHeight = MAX_PREVIEW_HEIGHT;
            }
            final Size[] imageSizes = map.getOutputSizes(ImageFormat.JPEG);
            final Size largestImageSize = Collections.max(
                    Arrays.asList(imageSizes),
                    new CompareSizesByArea());

            mImageReader = ImageReader.newInstance(largestImageSize.getWidth(), largestImageSize.getHeight(), ImageFormat.JPEG, 2);
            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);

            final Size[] videoSizes = map.getOutputSizes(MediaRecorder.class);

            // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
            // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
            // garbage capture data.
            mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                    rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                    maxPreviewHeight, largestImageSize);

            final int orientation = mContext.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void applyTextureTransformation(int width, int height) {
        if (mTextureView == null || mPreviewSize == null) {
            return;
        }
        final WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        final int deviceRotation = windowManager.getDefaultDisplay().getRotation();
        final Matrix matrix = new Matrix();
        final RectF viewRect = new RectF(0, 0, width, height);
        final RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        final float centerX = viewRect.centerX();
        final float centerY = viewRect.centerY();
        if (deviceRotation == Surface.ROTATION_90 || deviceRotation == Surface.ROTATION_270) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max((float) height / mPreviewSize.getHeight(), (float) width / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (deviceRotation - 2), centerX, centerY);
        } else if (deviceRotation == Surface.ROTATION_180) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }


    private void createPreviewSession() {
        try {
            final SurfaceTexture texture = mTextureView.getSurfaceTexture();
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            final Surface surface = new Surface(texture);
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if (mCameraDevice == null) {
                        return;
                    }
                    mState = STATE_PREVIEW;
                    mCaptureSession = cameraCaptureSession;
                    mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, mAutoExposure);
                    mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, mAutoFocus);
                    try {
                        mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if (mCameraCallback != null) {
                        mCameraCallback.onCameraConfigurationFailed();
                    }
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // camera device has been closed
            e.printStackTrace();
        }
    }

    @Override
    @NonNull
    public CameraOptions getCameraOptions(@NonNull CameraView.Facing facing) {
        final String cameraId = getCameraId(facing);
        if (cameraId != null) {
            return getCameraOptions(cameraId);
        }
        return CameraOptions.create();
    }

    @Override
    public CameraOptions getCameraOptions() {
        return getCameraOptions(mCameraId);
    }

    @NonNull
    private CameraOptions getCameraOptions(@NonNull String cameraId) {
        final CameraOptions cameraOptions = CameraOptions.create();
        try {
            final CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
            extractAutoFocusModes(cameraOptions, characteristics);
            extractAutoExposureModes(cameraOptions, characteristics);
            extractFlashAvailability(cameraOptions, characteristics);
            extractFacing(cameraOptions, characteristics);
            extractPhotoAndVideoSizes(cameraOptions, characteristics);
            extractHotPixelsModes(cameraOptions, characteristics);
            extractAberrationModes(cameraOptions, characteristics);
            extractAeAntibandingModes(cameraOptions, characteristics);
            extractEffectModes(cameraOptions, characteristics);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return cameraOptions;
    }

    private void extractEffectModes(@NonNull CameraOptions cameraOptions, @NonNull CameraCharacteristics characteristics) {
        final int[] effectModes = characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS);
        if (effectModes != null) {
            final List<CameraView.EffectMode> effectModesList = new ArrayList<>();
            for (final int effectMode : effectModes) {
                if (effectMode == CameraCharacteristics.CONTROL_EFFECT_MODE_OFF) {
                    effectModesList.add(CameraView.EffectMode.OFF);
                } else if (effectMode == CameraCharacteristics.CONTROL_EFFECT_MODE_AQUA) {
                    effectModesList.add(CameraView.EffectMode.AQUA);
                } else if (effectMode == CameraCharacteristics.CONTROL_EFFECT_MODE_BLACKBOARD) {
                    effectModesList.add(CameraView.EffectMode.BLACKBOARD);
                } else if (effectMode == CameraCharacteristics.CONTROL_EFFECT_MODE_MONO) {
                    effectModesList.add(CameraView.EffectMode.MONO);
                } else if (effectMode == CameraCharacteristics.CONTROL_EFFECT_MODE_NEGATIVE) {
                    effectModesList.add(CameraView.EffectMode.NEGATIVE);
                } else if (effectMode == CameraCharacteristics.CONTROL_EFFECT_MODE_POSTERIZE) {
                    effectModesList.add(CameraView.EffectMode.POSTERIZE);
                } else if (effectMode == CameraCharacteristics.CONTROL_EFFECT_MODE_SEPIA) {
                    effectModesList.add(CameraView.EffectMode.SEPIA);
                } else if (effectMode == CameraCharacteristics.CONTROL_EFFECT_MODE_SOLARIZE) {
                    effectModesList.add(CameraView.EffectMode.SOLARIZE);
                } if (effectMode == CameraCharacteristics.CONTROL_EFFECT_MODE_WHITEBOARD) {
                    effectModesList.add(CameraView.EffectMode.WHITEBOARD);
                }
            }
            cameraOptions.setEffectModes(effectModesList);
        }
    }

    private void extractAeAntibandingModes(@NonNull CameraOptions cameraOptions, @NonNull CameraCharacteristics characteristics) {
        final int[] antibandingModes = characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES);
        if (antibandingModes != null) {
            final List<CameraView.AeAntibandingMode> aeAntibandingModes = new ArrayList<>();
            for (final int antibandingMode : antibandingModes) {
                if (antibandingMode == CameraCharacteristics.CONTROL_AE_ANTIBANDING_MODE_OFF) {
                    aeAntibandingModes.add(CameraView.AeAntibandingMode.OFF);
                } else if (antibandingMode == CameraCharacteristics.CONTROL_AE_ANTIBANDING_MODE_AUTO) {
                    aeAntibandingModes.add(CameraView.AeAntibandingMode.AUTO);
                } else if (antibandingMode == CameraCharacteristics.CONTROL_AE_ANTIBANDING_MODE_50HZ) {
                    aeAntibandingModes.add(CameraView.AeAntibandingMode._50HZ);
                } else if (antibandingMode == CameraCharacteristics.CONTROL_AE_ANTIBANDING_MODE_60HZ) {
                    aeAntibandingModes.add(CameraView.AeAntibandingMode._60HZ);
                }
            }
            cameraOptions.setAeAntibandingModes(aeAntibandingModes);
        }
    }

    private void extractHotPixelsModes(@NonNull CameraOptions cameraOptions, @NonNull CameraCharacteristics characteristics) {
        final int[] hpModes = characteristics.get(CameraCharacteristics.HOT_PIXEL_AVAILABLE_HOT_PIXEL_MODES);
        if (hpModes != null) {
            final List<CameraView.HotPixelsMode> hotPixelsModes = new ArrayList<>();
            for (final int hpMode : hpModes) {
                if (hpMode == CameraCharacteristics.HOT_PIXEL_MODE_OFF) {
                    hotPixelsModes.add(CameraView.HotPixelsMode.OFF);
                } else if (hpMode == CameraCharacteristics.HOT_PIXEL_MODE_FAST) {
                    hotPixelsModes.add(CameraView.HotPixelsMode.FAST);
                } else if (hpMode == CameraCharacteristics.HOT_PIXEL_MODE_HIGH_QUALITY) {
                    hotPixelsModes.add(CameraView.HotPixelsMode.HIGH_QUALITY);
                }
            }
            cameraOptions.setHotPixelModes(hotPixelsModes);
        }
    }

    private void extractAberrationModes(@NonNull CameraOptions cameraOptions, @NonNull CameraCharacteristics characteristics) {
        final int[] hpModes = characteristics.get(CameraCharacteristics.COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES);
        if (hpModes != null) {
            final List<CameraView.ColorCorrectionAberrationMode> aberrationModes = new ArrayList<>();
            for (final int hpMode : hpModes) {
                if (hpMode == CameraCharacteristics.COLOR_CORRECTION_ABERRATION_MODE_OFF) {
                    aberrationModes.add(CameraView.ColorCorrectionAberrationMode.OFF);
                } else if (hpMode == CameraCharacteristics.COLOR_CORRECTION_ABERRATION_MODE_FAST) {
                    aberrationModes.add(CameraView.ColorCorrectionAberrationMode.FAST);
                } else if (hpMode == CameraCharacteristics.COLOR_CORRECTION_ABERRATION_MODE_HIGH_QUALITY) {
                    aberrationModes.add(CameraView.ColorCorrectionAberrationMode.HIGH_QUALITY);
                }
            }
            cameraOptions.setAberrationModes(aberrationModes);
        }
    }

    private void extractPhotoAndVideoSizes(@NonNull CameraOptions cameraOptions, @NonNull CameraCharacteristics characteristics) {
        final StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (map != null) {
            final Size[] photoOutputSizes = map.getOutputSizes(ImageFormat.JPEG);
            if (photoOutputSizes != null) {
                cameraOptions.setSupportedPhotoSizes(photoOutputSizes);
            }
            final Size[] videoOutputSizes = map.getOutputSizes(MediaRecorder.class);
            if (videoOutputSizes != null) {
                cameraOptions.setSupportedVideoSizes(videoOutputSizes);
            }
        }
    }

    private void extractFlashAvailability(@NonNull CameraOptions cameraOptions, @NonNull CameraCharacteristics characteristics) {
        final Boolean flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
        cameraOptions.setFlashAvailable(flashAvailable == null ? false : flashAvailable);
    }

    private void extractFacing(@NonNull CameraOptions cameraOptions, @NonNull CameraCharacteristics characteristics) {
        final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
        if (facing != null) {
            if (facing == CameraMetadata.LENS_FACING_FRONT) {
                cameraOptions.setFacing(CameraView.Facing.FRONT);
            } else if (facing == CameraMetadata.LENS_FACING_BACK) {
                cameraOptions.setFacing(CameraView.Facing.BACK);
            }
        }
    }

    private void extractAutoExposureModes(@NonNull CameraOptions cameraOptions, @NonNull CameraCharacteristics characteristics) {
        final int[] aeModes = characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES);
        if (aeModes != null) {
            final List<CameraView.AutoExposureMode> autoExposureModes = new ArrayList<>();
            for (final int aeMode : aeModes) {
                if (aeMode == CameraMetadata.CONTROL_AE_MODE_OFF) {
                    autoExposureModes.add(CameraView.AutoExposureMode.OFF);
                } else if (aeMode == CameraMetadata.CONTROL_AE_MODE_ON) {
                    autoExposureModes.add(CameraView.AutoExposureMode.ON);
                } else if (aeMode == CameraMetadata.CONTROL_AE_MODE_ON_ALWAYS_FLASH) {
                    autoExposureModes.add(CameraView.AutoExposureMode.ON_ALWAYS);
                } else if (aeMode == CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH) {
                    autoExposureModes.add(CameraView.AutoExposureMode.ON_AUTO);
                } else if (aeMode == CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE) {
                    autoExposureModes.add(CameraView.AutoExposureMode.ON_AUTO_REDEYE);
                }
            }
            cameraOptions.setSupportedFlashModes(autoExposureModes);
        }
    }

    private void extractAutoFocusModes(@NonNull CameraOptions cameraOptions, @NonNull CameraCharacteristics characteristics) {
        final int[] afModes = characteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
        if (afModes != null) {
            final List<CameraView.AutoFocusMode> autoFocusModes = new ArrayList<>();
            for (final int afMode : afModes) {
                if (afMode == CameraMetadata.CONTROL_AF_MODE_OFF) {
                    autoFocusModes.add(CameraView.AutoFocusMode.OFF);
                } else if (afMode == CameraMetadata.CONTROL_AF_MODE_AUTO) {
                    autoFocusModes.add(CameraView.AutoFocusMode.AUTO);
                } else if (afMode == CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE) {
                    autoFocusModes.add(CameraView.AutoFocusMode.CONTINUOUS_PICTURE);
                } else if (afMode == CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_VIDEO) {
                    autoFocusModes.add(CameraView.AutoFocusMode.CONTINUOUS_VIDEO);
                } else if (afMode == CameraMetadata.CONTROL_AF_MODE_EDOF) {
                    autoFocusModes.add(CameraView.AutoFocusMode.EDOF);
                } else if (afMode == CameraMetadata.CONTROL_AF_MODE_MACRO) {
                    autoFocusModes.add(CameraView.AutoFocusMode.MACRO);
                }
            }
            cameraOptions.setSupportedAutoFocusModes(autoFocusModes);
        }
    }

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, choose the smallest one that
     * is at least as large as the respective texture view size, and that is at most as large as the
     * respective max size, and whose aspect ratio matches with the specified value. If such size
     * doesn't exist, choose the largest one that is at most as large as the respective max size,
     * and whose aspect ratio matches with the specified value.
     *
     * @param choices           The list of sizes that the camera supports for the intended output
     *                          class
     * @param textureViewWidth  The width of the texture view relative to sensor coordinate
     * @param textureViewHeight The height of the texture view relative to sensor coordinate
     * @param maxWidth          The maximum width that can be chosen
     * @param maxHeight         The maximum height that can be chosen
     * @param aspectRatio       The aspect ratio
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            return choices[0];
        }
    }

    public boolean isFrontCameraSupported() {
        return getCameraId(CameraView.Facing.FRONT) != null;
    }

    public boolean isBackCameraSupported() {
        return getCameraId(CameraView.Facing.BACK) != null;
    }

    @Nullable
    public String getCameraId(@NonNull CameraView.Facing facing) {
        try {
            for (final String cameraId : mCameraManager.getCameraIdList()) {
                final CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
                Integer cameraFacing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cameraFacing != null) {
                    if (facing == CameraView.Facing.FRONT && cameraFacing == CameraMetadata.LENS_FACING_FRONT ||
                            facing == CameraView.Facing.BACK && cameraFacing == CameraMetadata.LENS_FACING_BACK) {
                        return cameraId;
                    }
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int decodeAutoFocus(@NonNull CameraView.AutoFocusMode autoFocusMode) {
        switch (autoFocusMode) {
            default:
            case AUTO:
                return CameraMetadata.CONTROL_AF_MODE_AUTO;
            case CONTINUOUS_PICTURE:
                return CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE;
            case CONTINUOUS_VIDEO:
                return CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_VIDEO;
            case EDOF:
                return CameraMetadata.CONTROL_AF_MODE_EDOF;
            case MACRO:
                return CameraMetadata.CONTROL_AF_MODE_MACRO;
            case OFF:
                return CameraMetadata.CONTROL_AF_MODE_OFF;
        }
    }

    private static int decodeAutoExposure(@NonNull CameraView.AutoExposureMode autoExposureMode) {
        switch (autoExposureMode) {
            default:
            case ON_AUTO:
                return CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH;
            case OFF:
                return CameraMetadata.CONTROL_AE_MODE_OFF;
            case ON:
                return CameraMetadata.CONTROL_AE_MODE_ON;
            case ON_ALWAYS:
                return CameraMetadata.CONTROL_AE_MODE_ON_ALWAYS_FLASH;
            case ON_AUTO_REDEYE:
                return CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE;
        }
    }

    @Override
    public void setCameraFacing(@NonNull CameraView.Facing facing) throws IllegalArgumentException {
        BSAssertions.assertNotNull(facing, "facing");
        if (facing == CameraView.Facing.FRONT) {
            if (!isFrontCameraSupported()) {
                throw new IllegalArgumentException("Front camera is not supported");
            }
        } else if (facing == CameraView.Facing.BACK) {
            if (!isBackCameraSupported()) {
                throw new IllegalArgumentException("Back camera is not supported");
            }
        }
        final String cameraId = getCameraId(facing);
        if (!TextUtils.isEmpty(mCameraId) && !TextUtils.equals(mCameraId, cameraId)) {
            onPause();
            // reset view's width and height
            mTextureView.setAspectRatio(0, 0);
            mCameraId = cameraId;
            onResume();
        } else {
            mCameraId = cameraId;
        }
    }

    @Override
    public void setAutoExposureMode(@NonNull CameraView.AutoExposureMode autoExposureMode) {
        BSAssertions.assertNotNull(autoExposureMode, "autoExposureMode");
        mAutoExposure = decodeAutoExposure(autoExposureMode);
        if (mCaptureSession != null) {
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, mAutoExposure);
            try {
                mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setAutoFocusMode(@NonNull CameraView.AutoFocusMode autoFocusMode) {
        BSAssertions.assertNotNull(autoFocusMode, "autoFocusMode");
        mAutoFocus = decodeAutoFocus(autoFocusMode);
        if (mCaptureSession != null) {
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, mAutoFocus);
            try {
                mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void captureImage(@NonNull String filePath) {
        BSAssertions.assertNotEmpty(filePath, "filePath");
        captureImage(new File(filePath));
    }

    @Override
    public void captureImage(@NonNull File file) {
        BSAssertions.assertNotNull(file, "file");
        mImageFile = file;
        lockFocus();
    }

    @Override
    public void captureVideo(@NonNull String filePath, @NonNull VideoCaptureOptions captureOptions) {
        BSAssertions.assertNotEmpty(filePath, "filePath");
        captureVideo(new File(filePath), captureOptions);
    }

    @Override
    public void captureVideo(@NonNull File file, @NonNull VideoCaptureOptions captureOptions) {
        BSAssertions.assertNotNull(file, "file");
        BSAssertions.assertNotNull(captureOptions, "captureOptions");

    }

    private void saveImage(@NonNull Image image) {
        if (image.getFormat() == ImageFormat.PRIVATE) {
            return;
        }
        final Image.Plane[] planes = image.getPlanes();
        if (planes != null && planes.length > 0) {
            final ByteBuffer buffer = planes[0].getBuffer();
            final byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            try (FileOutputStream fos = new FileOutputStream(mImageFile)) {
                fos.write(bytes);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                image.close();
            }
        }
    }

    private void captureStillPicture() {
        try {
            final CaptureRequest.Builder builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            builder.addTarget(mImageReader.getSurface());
            builder.set(CaptureRequest.CONTROL_AE_MODE, mAutoExposure);
            builder.set(CaptureRequest.CONTROL_AF_MODE, mAutoFocus);
            final WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            final int deviceRotation = windowManager.getDefaultDisplay().getRotation();
            builder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(deviceRotation));
            final CaptureCallback captureCallback = new CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    unlockFocus();
                }
            };
            mCaptureSession.stopRepeating();
            mCaptureSession.capture(builder.build(), captureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void runPreCaptureSequence() {
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                CameraMetadata.CONTROL_AE_PRECAPTURE_TRIGGER_START);
        mState = STATE_WAITING_PRE_CAPTURE;
        try {
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void lockFocus() {
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
        mState = STATE_WAITING_LOCK;
        try {
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void unlockFocus() {
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
        try {
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
            mState = STATE_PREVIEW;
            mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the JPEG orientation from the specified screen rotation.
     *
     * @param rotation The screen rotation.
     * @return The JPEG orientation (one of 0, 90, 270, and 360)
     */

    private int getOrientation(int rotation) {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;

    }

    public interface CameraCallback {

        void onPermissionsNotGranted();

        void onCameraCantBeAcquired();

        void onCantLockCamera();

        void onCameraError(int errorCode);

        void onCameraConfigurationFailed();
    }

    /**
     * Compares two {@code Size}s based on their areas.
     */
    private static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    private class CameraCaptureCallback extends CameraCaptureSession.CaptureCallback {
        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            super.onCaptureProgressed(session, request, partialResult);
            onProgress(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            onProgress(result);
        }

        private void onProgress(@NonNull CaptureResult captureResult) {
            if (mState != STATE_PREVIEW) {
                Log.w("CAMERA STATE", "" + mState);
            }
            switch (mState) {
                case STATE_WAITING_LOCK: {
                    final Integer afState = captureResult.get(CaptureResult.CONTROL_AF_STATE);
                    Log.w("AF STATE", "" + afState);
                    if (afState == null || afState == CameraMetadata.CONTROL_AF_STATE_INACTIVE &&
                            TextUtils.equals(mCameraId, getCameraId(CameraView.Facing.FRONT))) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    } else if (afState == CameraMetadata.CONTROL_AF_STATE_FOCUSED_LOCKED ||
                            afState == CameraMetadata.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED) {
                        final Integer aeState = captureResult.get(CaptureResult.CONTROL_AE_STATE);
                        Log.w("AE STATE", "" + aeState);
                        if (aeState == null || aeState == CameraMetadata.CONTROL_AE_STATE_CONVERGED) {
                            mState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPreCaptureSequence();
                        }
                    }/*else if (afState == CameraMetadata.CONTROL_AF_STATE_PASSIVE_FOCUSED) {
                        lockFocus();
                    }*/
                    break;
                }
                case STATE_WAITING_PRE_CAPTURE: {
                    final Integer aeState = captureResult.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CameraMetadata.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CameraMetadata.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRE_CAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRE_CAPTURE: {
                    final Integer aeState = captureResult.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CameraMetadata.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
            }
        }
    }

    private class CameraSurfaceListener implements TextureView.SurfaceTextureListener {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
            applyTextureTransformation(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    }

    private class CameraStateCallback extends CameraDevice.StateCallback {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mOpenCloseCameraLock.release();
            mCameraDevice = cameraDevice;
            createPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            mOpenCloseCameraLock.release();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int errorCode) {
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            mOpenCloseCameraLock.release();
            if (mCameraCallback != null) {
                mCameraCallback.onCameraError(errorCode);
            }
        }
    }
}
