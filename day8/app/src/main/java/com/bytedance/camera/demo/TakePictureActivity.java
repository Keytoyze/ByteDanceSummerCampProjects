package com.bytedance.camera.demo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.camera.demo.utils.UriUtils;
import com.bytedance.camera.demo.utils.Utils;

import java.io.File;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class TakePictureActivity extends AppCompatActivity {

    private ImageView imageView;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 101;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        imageView = findViewById(R.id.img);
        findViewById(R.id.btn_picture).setOnClickListener(v -> {
            //DONE 在这里申请相机、存储的权限 (delegate by PermissionDispatcher)
            TakePictureActivityPermissionsDispatcher.takePictureWithPermissionCheck(this);
        });

    }

    @NeedsPermission({Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void takePicture() {
        //DONE 打开相机
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = Utils.getOutputMediaFile(Utils.MEDIA_TYPE_IMAGE);
        Uri uri = UriUtils.getDocumentUri(imageFile, this);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //DONE 处理返回数据
            setPic();
        }
    }

    private void setPic() {
        //DONE 根据imageView裁剪
        int width = this.imageView.getWidth();
        int height = this.imageView.getHeight();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //DONE 根据缩放比例读取文件，生成Bitmap
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = Math.min(options.outWidth / width, options.outHeight / height);
        options.inPurgeable = true;
        //DONE 如果存在预览方向改变，进行图片旋转
        Bitmap bitmap = Utils.rotateImage(BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options),
                imageFile.getAbsolutePath());
        //DONE 显示图片
        this.imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TakePictureActivityPermissionsDispatcher.onRequestPermissionsResult(this,
                requestCode, grantResults);
    }
}