package com.bytedance.camera.demo;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bytedance.camera.demo.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.bytedance.camera.demo.utils.Utils.MEDIA_TYPE_IMAGE;
import static com.bytedance.camera.demo.utils.Utils.MEDIA_TYPE_VIDEO;
import static com.bytedance.camera.demo.utils.Utils.getOutputMediaFile;

public class CustomCameraActivity extends AppCompatActivity {

    private static final String TAG = CustomCameraActivity.class.getSimpleName();

    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Camera mCamera;

    private int CAMERA_TYPE = Camera.CameraInfo.CAMERA_FACING_BACK;

    private boolean isRecording = false;
    private boolean isPause = false;

    private int rotationDegree = 0;

    private int mWidth = 0, mHeight = 0;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom_camera);

        mSurfaceView = findViewById(R.id.img);
        //DONE 给SurfaceHolder添加Callback
        mHolder = mSurfaceView.getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i(TAG, "surface created");
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.i(TAG, "surface changed: mWidth = " + width + ", mHeight = " + height);
                mWidth = width;
                mHeight = height;
                releaseCameraAndPreview();
                startPreview(holder);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.i(TAG, "surface destroyed");
                releaseCameraAndPreview();
            }
        });

        findViewById(R.id.btn_picture).setOnClickListener(v -> {
            //DONE 拍一张照片
            mCamera.takePicture(null, null, mPicture);
        });

        Button recordBtn = findViewById(R.id.btn_record);
        Button pauseBtn = findViewById(R.id.btn_record_pause);
        recordBtn.setOnClickListener(v -> {
            //DONE 录制，第一次点击是start，第二次点击是stop
            isPause = false;
            pauseBtn.setText(R.string.pause);
            if (isRecording) {
                //DONE 停止录制
                recordBtn.setText(R.string.record);
                pauseBtn.setEnabled(false);
                releaseMediaRecorder();

            } else {
                //DONE 录制
                recordBtn.setText(R.string.stop);
                if (Build.VERSION.SDK_INT >= 24) {
                    pauseBtn.setEnabled(true);
                }
                prepareVideoRecorder();
            }
        });

        pauseBtn.setOnClickListener(v -> {
            if (isPause) {
                mMediaRecorder.resume();
                isPause = false;
                pauseBtn.setText(R.string.pause);
            } else {
                mMediaRecorder.pause();
                isPause = true;
                pauseBtn.setText(R.string.resume);
            }
        });

        findViewById(R.id.btn_facing).setOnClickListener(v -> {
            //DONE 切换前后摄像头
            if (CAMERA_TYPE == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCamera = getCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                startPreview(mHolder);
            } else {
                mCamera = getCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
                startPreview(mHolder);
            }
        });

        findViewById(R.id.btn_zoom_in).setOnClickListener(v -> {
            //DONE 调焦，需要判断手机是否支持
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.isZoomSupported()) {
                int zoom = parameters.getZoom() + 5;
                if (zoom < parameters.getMaxZoom()) {
                    parameters.setZoom(zoom);
                    mCamera.setParameters(parameters);
                }
            }
        });

        findViewById(R.id.btn_zoom_out).setOnClickListener(v -> {
            //DONE 调焦，需要判断手机是否支持
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.isZoomSupported()) {
                int zoom = parameters.getZoom() - 5;
                if (zoom >= 0) {
                    parameters.setZoom(zoom);
                    mCamera.setParameters(parameters);
                }
            }
        });

        findViewById(R.id.btn_flash).setOnClickListener(v -> {
            //DONE 监听闪光灯
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.getFlashMode().equals(android.hardware.Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
            } else {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            }
        });
    }

    public Camera getCamera(int position) {
        CAMERA_TYPE = position;
        if (mCamera != null) {
            releaseCameraAndPreview();
        }
        Camera cam = Camera.open(position);

        //DONE 摄像头添加属性，例是否自动对焦，设置旋转方向等
        rotationDegree = getCameraDisplayOrientation(position);
        cam.setDisplayOrientation(rotationDegree);

        Camera.Parameters parameters = cam.getParameters();
        size = getOptimalPreviewSize(cam.getParameters().getSupportedPreviewSizes(),
                mWidth, mHeight);
        parameters.setPreviewSize(size.width, size.height);

        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        cam.setParameters(parameters);
        return cam;
    }


    private static final int DEGREE_90 = 90;
    private static final int DEGREE_180 = 180;
    private static final int DEGREE_270 = 270;
    private static final int DEGREE_360 = 360;

    private int getCameraDisplayOrientation(int cameraId) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = DEGREE_90;
                break;
            case Surface.ROTATION_180:
                degrees = DEGREE_180;
                break;
            case Surface.ROTATION_270:
                degrees = DEGREE_270;
                break;
            default:
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % DEGREE_360;
            result = (DEGREE_360 - result) % DEGREE_360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + DEGREE_360) % DEGREE_360;
        }
        Log.i(TAG, "orientation: " + result);

        return result;
    }


    private void releaseCameraAndPreview() {
        //DONE 释放camera资源
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    Camera.Size size;

    private void startPreview(SurfaceHolder holder) {
        //DONE 开始预览
        Log.i(TAG, "start preview");
        if (mCamera == null) {
            mCamera = getCamera(CAMERA_TYPE);
        }
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mCamera.getParameters().getSupportedFocusModes()
                .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            mCamera.autoFocus((success, camera) -> {
                if (success) {
                    mCamera.cancelAutoFocus();
                    Log.i(TAG, "auto focus success");
                } else {
                    Log.e(TAG, "auto focus failed");
                }
            });
        }
    }

    private MediaRecorder mMediaRecorder;
    private File videoFile;

    private boolean prepareVideoRecorder() {
        //DONE 准备MediaRecorder
        Log.i(TAG, "prepare video recorder");
        isRecording = true;
        mMediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        videoFile = getOutputMediaFile(MEDIA_TYPE_VIDEO);
        mMediaRecorder.setOutputFile(videoFile.getAbsolutePath());
        mMediaRecorder.setPreviewDisplay(mHolder.getSurface());
        mMediaRecorder.setOrientationHint(rotationDegree);

        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IOException e) {
            releaseMediaRecorder();
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private void releaseMediaRecorder() {
        //DONE 释放MediaRecorder
        Log.i(TAG, "release media recorder");
        isRecording = false;
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
        Utils.insertIntoGallery(videoFile, this);
        mCamera.lock();
    }


    private Camera.PictureCallback mPicture = (data, camera) -> {
        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        setPictureDegreeZero(pictureFile.getAbsolutePath());

        Utils.insertIntoGallery(pictureFile, this);

        mCamera.startPreview();
    };


    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = Math.min(w, h);

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    private static final int[] ORIENTATION_MAP = new int[]{
            ExifInterface.ORIENTATION_NORMAL, ExifInterface.ORIENTATION_ROTATE_90,
            ExifInterface.ORIENTATION_ROTATE_180, ExifInterface.ORIENTATION_ROTATE_270
    };

    public void setPictureDegreeZero(String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = ORIENTATION_MAP[getCameraDisplayOrientation(CAMERA_TYPE) / 90];
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,
                    String.valueOf(orientation));
            exifInterface.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
