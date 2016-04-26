package com.mrg.drawing;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

import com.mrg.drawing.GPUImage.GPUImage;


/**
 * Created by Mrugi on 2016-03-23.
 */
public class CameraLoader {
    private Context mContext;
    private int numberOfCameras = 1;
    private GPUImage mGPUImage;
    CameraLoader(Context context, GPUImage gpuimage){
        mContext = context;
        if(hasCameraSupport()) numberOfCameras+=1;
        mGPUImage = gpuimage;
    }
    private int mCurrentCameraId = 0;
    private Camera mCameraInstance;

    public void onResume() {
        setUpCamera(mCurrentCameraId);
    }

    public void onPause() {
        releaseCamera();
    }

    public void switchCamera() {
        releaseCamera();
        mCurrentCameraId = (mCurrentCameraId + 1) % getNumberOfCameras();
        setUpCamera(mCurrentCameraId);
    }
    public int getCurrentCameraId(){
        return mCurrentCameraId;
    }

    private int getNumberOfCameras() {
        return numberOfCameras;
    }

    private boolean hasCameraSupport() {
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
    private void setUpCamera(final int id) {
        mCameraInstance = getCameraInstance(id);
        Camera.Parameters parameters = mCameraInstance.getParameters();
        // TODO adjust by getting supportedPreviewSizes and then choosing
        // the best one for screen size (best fill screen)
        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        mCameraInstance.setParameters(parameters);

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(mCurrentCameraId, cameraInfo);
        boolean flipHorizontal = cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
        mGPUImage.setUpCamera(mCameraInstance, cameraInfo.orientation,flipHorizontal, false);
    }

    /** A safe way to get an instance of the Camera object. */
    private Camera getCameraInstance(final int id) {
        Camera c = null;
        try {
            c = Camera.open(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
    public Camera getCameraInstance(){
        return mCameraInstance;
    }
    private void releaseCamera() {
        mCameraInstance.setPreviewCallback(null);
        mCameraInstance.release();
        mCameraInstance = null;
    }

}
