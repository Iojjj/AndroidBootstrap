package iojjj.androidbootstrap.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;

import iojjj.androidbootstrap.ui.widgets.CameraPreview;
import iojjj.androidbootstrap.utils.storage.FileUtils;
import iojjj.androidbootstrap.utils.threading.AsyncTaskEx;

/**
 * Camera screen
 */
@SuppressWarnings("deprecation")
public abstract class CameraFragment extends AbstractFragment {

    private static final String KEY_CAMERA_TYPE = "key_camera_type";

    private Camera camera;
    private int currentRotation = -1;
    private int cameraId = -1;
    private int cameraType = -1;
    private boolean isCapturing;
    private boolean isCameraChanging;
    private Camera.Parameters previousCameraParameters;
    private Runnable orientationChangeTask;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;
    private CameraActivityExceptionHandler exceptionHandler;
    private OrientationEventListener orientationEventListener;
    private boolean isCameraAvailable = true;

    private final Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] bytes, final Camera camera) {
            AsyncTaskEx<Void, Void, Boolean> task = new AsyncTaskEx<Void, Void, Boolean>() {

                private File pic;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pic = new File(getActivity().getCacheDir(), "tmp.jpg");
                }

                @Override
                protected Boolean doInBackground(Void... voids) {
                    return !pic.isDirectory() && saveBytes(pic, bytes);
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    if (result) {
                        System.gc();
                        onPhotoCaptured(pic);
                    } else {
                        camera.startPreview();
                    }
                    isCapturing = false;
                }
            };
            task.executeEx();
        }
    };

    protected View.OnClickListener onChangeCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isCapturing || isCameraChanging) return;
            isCameraChanging = true;
            onSwitchCamera();
            switchCamera();
        }
    };

    protected View.OnClickListener onCaptureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            capture();
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    stopCapturing();
                    releaseCamera();
                }
            }
        };
        telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        exceptionHandler = new CameraActivityExceptionHandler(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // check if camera exists on device
        if (!checkCameraHardware()) {
            onNoCamerasFound();
            return;
        }

        initListeners();
        if (!initCamera(savedInstanceState == null ? Camera.CameraInfo.CAMERA_FACING_BACK :
                savedInstanceState.getInt(KEY_CAMERA_TYPE, Camera.CameraInfo.CAMERA_FACING_BACK))) {
            onCantConnectToCamera();
            return;
        }

        Camera.Parameters parameters = camera.getParameters();
        if (parameters.getSupportedFlashModes().contains(Camera.Parameters.FLASH_MODE_AUTO))
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        List<String> focusModes = parameters.getSupportedFocusModes();
        String focusMode = Camera.Parameters.FOCUS_MODE_AUTO;
        if (focusModes != null && focusModes.contains(focusMode)) {
            parameters.setFocusMode(focusMode);
        }
        Camera.Size pictureSize = getSuitablePhotographerResolution(parameters);
        parameters.setPictureSize(pictureSize.width, pictureSize.height);
        camera.setParameters(parameters);
        onGetNumberOfCameras(Camera.getNumberOfCameras());
        getCameraPreview().setDisplay(getActivity().getWindowManager().getDefaultDisplay());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isCameraAvailable) {
            if (camera == null) {
                camera = getCameraInstance(cameraType);
            }
            if (camera == null) {
                onCantConnectToCamera();
                return;
            }
            orientationEventListener.enable();
            getCameraPreview().setCamera(camera, cameraId);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_CAMERA_TYPE, cameraType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isCameraAvailable) {
            orientationEventListener.disable();
            stopCapturing();
            releaseCamera();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isCameraAvailable) {
            orientationEventListener.disable();
            stopCapturing();
            releaseCamera();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    private void stopCapturing() {
        if (!isCapturing)
            return;
        isCapturing = false;
    }

    private boolean initCamera(int initType) {
        if (initType == Camera.CameraInfo.CAMERA_FACING_BACK)
            cameraType = Camera.CameraInfo.CAMERA_FACING_BACK;
        else
            cameraType = Camera.CameraInfo.CAMERA_FACING_FRONT;
        camera = getCameraInstance(cameraType);
        if (camera == null) {
            if (initType == Camera.CameraInfo.CAMERA_FACING_BACK)
                cameraType = Camera.CameraInfo.CAMERA_FACING_FRONT;
            else
                cameraType = Camera.CameraInfo.CAMERA_FACING_BACK;
            camera = getCameraInstance(cameraType);
            if (camera == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    @Nullable
    public Camera getCameraInstance(int cameraType) {
        synchronized (CameraFragment.class) {
            Camera c = null;
            cameraId = -1;

            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

            final int cameraCount = Camera.getNumberOfCameras();
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                if (cameraInfo.facing == cameraType) {
                    try {
                        c = Camera.open(camIdx);
                        cameraId = camIdx;
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return c; // returns null if camera is unavailable
        }
    }


    /**
     * Initialize listeners
     */
    private void initListeners() {
        orientationEventListener = new OrientationEventListener(getActivity()) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (isCameraChanging) {
                    final int lastOrientation = orientation;
                    orientationChangeTask = new Runnable() {
                        @Override
                        public void run() {
                            onOrientationChanged(lastOrientation);

                            orientationChangeTask = null;
                        }
                    };
                    return;
                }

                if (orientation == ORIENTATION_UNKNOWN || cameraId == -1) return;
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(cameraId, info);
                orientation = (orientation + 45) / 90 * 90;
                int rotation;
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    rotation = (info.orientation - orientation + 360) % 360;
                } else {  // back-facing camera
                    rotation = (info.orientation + orientation) % 360;
                }
                if (camera == null) {
                    return;
                }
                if (currentRotation != rotation) {
                    currentRotation = rotation;
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setRotation(rotation);
                    camera.setParameters(parameters);
                }
            }
        };
    }

    private void capture() {
        if (camera == null)
            return;
        capturePhoto();
    }

    private void capturePhoto() {
        if (isCapturing) return;
        isCapturing = true;
        try {
            camera.takePicture(null, null, jpegCallback);
        } catch (RuntimeException e) {
            if (isAdded()) {
                onCantCaptureError(e);
            }
            isCapturing = false;
        }
    }

    private void switchCamera() {
        new AsyncTaskEx<Void, Void, Camera>() {

            private volatile Camera.Parameters parametersBackup;

            @Override
            protected void onPreExecute() {
                parametersBackup = camera.getParameters();
                releaseCamera();
            }

            @Override
            protected Camera doInBackground(Void... voids) {
                int newCameraType = cameraType == Camera.CameraInfo.CAMERA_FACING_BACK ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;
                Camera camera = getCameraInstance(newCameraType);
                if (camera != null) {
                    cameraType = newCameraType;
                    currentRotation = -1;
                    if (previousCameraParameters != null)
                        camera.setParameters(previousCameraParameters);
                    previousCameraParameters = parametersBackup;
                } else {
                    camera = getCameraInstance(cameraType);
                    if (camera != null) {
                        camera.setParameters(parametersBackup);
                    }
                }
                return camera;
            }

            @Override
            protected void onPostExecute(Camera camera) {
                if (!isAdded())
                    return;
                CameraFragment.this.camera = camera;
                if (camera != null) {
                    getCameraPreview().setCamera(camera, cameraId);
                    Camera.Parameters cameraParameters = camera.getParameters();
                    Camera.Size pictureSize = getSuitablePhotographerResolution(cameraParameters);
                    cameraParameters.setPictureSize(pictureSize.width, pictureSize.height);
                    camera.setParameters(cameraParameters);
                    orientationEventListener.onOrientationChanged(getResources().getConfiguration().orientation);
                }

                if (orientationChangeTask != null)
                    orientationChangeTask.run();
                isCameraChanging = false;
            }
        }.executeEx();
    }


    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware() {
        PackageManager pm = getActivity().getPackageManager();
        return pm != null && (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT));
    }

    /**
     * Stop preview, release camera and  free resources
     */
    private void releaseCamera() {
        if (getCameraPreview().getCamera() == camera) {
            getCameraPreview().setCamera(null, 0);
            camera = null;
        }
        if (camera == null)
            return;
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @NonNull
    private static Camera.Size getSuitablePhotographerResolution(Camera.Parameters cameraParameters) {
        Camera.Size maxSize = null;

        Collection<Camera.Size> pictureSizes = cameraParameters.getSupportedPictureSizes();
        for (Camera.Size pictureSize : pictureSizes) {
            if (maxSize == null || pictureSize.width > maxSize.width || pictureSize.height > maxSize.height) {
                maxSize = pictureSize;
            }
        }
        //noinspection ConstantConditions
        return maxSize;
    }

    /**
     * Handler for all uncaught exceptions
     */
    private static class CameraActivityExceptionHandler implements Thread.UncaughtExceptionHandler {

        final WeakReference<CameraFragment> activityRef;

        public CameraActivityExceptionHandler(CameraFragment instance) {
            activityRef = new WeakReference<CameraFragment>(instance);
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            final CameraFragment fragment = activityRef.get();

            if (fragment != null)
                fragment.releaseCamera();
        }
    }


    private static boolean saveBytes(File saveFile, byte[] bytes) {
        boolean success = false;
        OutputStream os = null;
        try {
            os = new FileOutputStream(saveFile);
            FileUtils.copyStream(new ByteArrayInputStream(bytes), os);
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    protected abstract void onPhotoCaptured(@NonNull File photo);

    protected abstract void onSwitchCamera();

    protected abstract void onGetNumberOfCameras(int numberOfCameras);

    protected abstract void onCantCaptureError(RuntimeException e);

    protected abstract void onCantConnectToCamera();

    protected abstract CameraPreview getCameraPreview();

    protected abstract void onNoCamerasFound();
}
