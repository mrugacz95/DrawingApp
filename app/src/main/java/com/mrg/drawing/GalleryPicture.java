package com.mrg.drawing;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.mrg.drawing.shaders.GPUImageCanny;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.co.cyberagent.android.gpuimage.GPUImage;

/**
 * Created by Mrugi on 2016-03-26.
 */
public class GalleryPicture extends Fragment{
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
    public GalleryPicture() {
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGPUImage.setGLSurfaceView(glSurfaceView);
        mGPUImage.setFilter(new GPUImageCanny());
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
                    mGPUImage.setImage(imageUri);
                }
                break;
        }
    }
}
