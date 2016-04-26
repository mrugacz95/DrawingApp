package com.mrg.drawing;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.mrg.drawing.GPUImage.GPUImage;
import com.mrg.drawing.shaders.GPUImageCanny;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSketchFilter;
import jp.co.cyberagent.android.gpuimage.Rotation;


public class CameraCapture extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SELECT_PHOTO = 2;
    GPUImage mGPUImage;
    @Bind(R.id.bt_capture_photo)
            ImageButton btCapurePhoto;
    @Bind(R.id.next)
            ImageButton btNext;
    @Bind(R.id.gl_surface_view)
            GLSurfaceView glSurfaceView;
    @Bind(R.id.switch_camera)
            ImageButton btSwitchCamera;
    @Bind(R.id.delete)
    ImageButton btDelete;
    @Bind(R.id.change_filter)
    ImageButton btChangeFillter;
    int currentFillter=0;
    CameraLoader cameraLoader;
    public CameraCapture() {
        // Required empty public constructor
    }
    public static CameraCapture newInstance() {
        return new CameraCapture();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_camera_capture, container, false);
        mGPUImage = new GPUImage(getActivity());
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraLoader.onResume();
    }

    @Override
    public void onPause() {
        cameraLoader.onPause();
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGPUImage.setGLSurfaceView(glSurfaceView);
        mGPUImage.setFilter(new GPUImageCanny());
        cameraLoader = new CameraLoader(getContext(),mGPUImage);

    }

    @OnClick(R.id.bt_capture_photo)
    public void caputrPhoto() {

    }
    @OnClick(R.id.bt_from_gallery)
    public void chooseFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case SELECT_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Uri imageUri = data.getData();
                    setImage(imageUri);
                }
                break;
        }
    }
    private void setImage(Uri imageUri){
//        cameraLoader.getCameraInstance().release();
        mGPUImage.setRotation(Rotation.NORMAL);
        mGPUImage.setScaleType(jp.co.cyberagent.android.gpuimage.GPUImage.ScaleType.CENTER_CROP);
        mGPUImage.setImage(imageUri);
        btNext.setVisibility(View.VISIBLE);
        btCapurePhoto.setVisibility(View.GONE);
        btDelete.setVisibility(View.VISIBLE);
        btChangeFillter.setVisibility(View.GONE);
    }
    @OnClick(R.id.change_filter)
    public void changeFilter(){
        currentFillter++;
        GPUImageFilter filter;
        switch (currentFillter){
            case 1:
                filter = new GPUImageSketchFilter();
                break;
            default:
                currentFillter=0;
                filter = new GPUImageCanny();
                break;
        }
        mGPUImage.setFilter(filter);
    }
    @OnClick(R.id.switch_camera)
    public void switchCamera(){
        cameraLoader.switchCamera();
        if(cameraLoader.getCurrentCameraId()==0)
            Picasso.with(getContext()).load(R.drawable.ic_camera_front_white_48dp).into(btSwitchCamera);
        else
            Picasso.with(getContext()).load(R.drawable.ic_camera_rear_white_48dp).into(btSwitchCamera);
    }
    @OnClick(R.id.delete)
    public void discardPhoto(){
        btNext.setVisibility(View.GONE);
        btCapurePhoto.setVisibility(View.VISIBLE);
        btDelete.setVisibility(View.GONE);
        btChangeFillter.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.bt_capture_photo)
    public void photoCapture() {
//        File pictureFile = getOutputMediaFile();
//        if (pictureFile == null) {
//            Log.d("Bitmap", "Error creating media file, check storage permissions");
//            return;
//        }
//        FileOutputStream out = null;
//        Bitmap bmp = mGPUImage.getBitmapWithFilterApplied();
//        try {
//            out = new FileOutputStream(pictureFile);
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
//            // PNG is a lossless format, the compression factor (100) is ignored
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        mGPUImage.saveToPictures("GPUImage", "ImageWithFilter.jpg", new GPUImage.OnPictureSavedListener() {
            @Override
            public void onPictureSaved(Uri uri) {
                Log.d("picture", "saved");
            }
        });
        return;

//        cameraLoader.getCameraInstance().takePicture(null, null, new Camera.PictureCallback() {
//            final static String TAG = "picture caputre";
//
//            @Override
//            public void onPictureTaken(byte[] data, Camera camera) {
//                File pictureFile = getOutputMediaFile();
//                if (pictureFile == null) {
//                    Log.d(TAG, "Error creating media file, check storage permissions");
//                    return;
//                }
//
//                try {
//                    FileOutputStream fos = new FileOutputStream(pictureFile);
//                    fos.write(data);
//                    fos.close();
//                } catch (FileNotFoundException e) {
//                    Log.d(TAG, "File not found: " + e.getMessage());
//                    return;
//                } catch (IOException e) {
//                    Log.d(TAG, "Error accessing file: " + e.getMessage());
//                    return;
//                }
//                finally {
//                    Log.d(TAG,"success");
//                    Log.d("URI",Uri.fromFile(pictureFile).toString());
//                }
//                setImage(Uri.fromFile(pictureFile));
//            }
//        });

    }
    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "DrawingApp");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");


        return mediaFile;
    }
}
