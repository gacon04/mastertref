package com.example.mastertref.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;

public class MediaPermissionHelper {
    private static final int PERMISSION_REQUEST_CODE = 100;

    public interface MediaCallback {
        void onImageSelected(Uri imageUri);
        void onPermissionDenied();
    }

    /**
     * Kiểm tra xem đã có tất cả quyền cần thiết chưa
     */
    public static boolean hasAllPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(activity,
                    android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(activity,
                    android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(activity,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(activity,
                    android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        }
    }

    /**
     * Hiển thị dialog chọn ảnh nếu đã có quyền, ngược lại sẽ xin cấp quyền
     */
    public static void showImagePicker(Activity activity, MediaCallback callback) {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA
            };
        } else {
            permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            };
        }

        // Luôn xin quyền trước, không cần kiểm tra
        ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE);
    }

    private static void showImagePickerDialog(Activity activity, MediaCallback callback) {
        String[] options = {"Chụp ảnh mới", "Chọn từ thư viện"};
        new AlertDialog.Builder(activity)
                .setTitle("Chọn ảnh từ")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Mở camera
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        activity.startActivityForResult(intent, 1);
                    } else {
                        // Mở gallery
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activity.startActivityForResult(intent, 2);
                    }
                })
                .show();
    }

    public static void handlePermissionResult(Activity activity, int requestCode,
                                           String[] permissions, int[] grantResults,
                                           MediaCallback callback) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                showImagePickerDialog(activity, callback);
            } else {
                // Nếu bị từ chối, xin cấp quyền lại ngay lập tức
                ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE);
            }
        }
    }

    /**
     * Xử lý kết quả chọn ảnh từ camera hoặc gallery
     */
    public static void handleActivityResult(Activity activity, int requestCode, int resultCode,
                                         Intent data, MediaCallback callback) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 1) { // Camera
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    Uri imageUri = getImageUri(activity, imageBitmap);
                    callback.onImageSelected(imageUri);
                }
            } else if (requestCode == 2) { // Gallery
                Uri imageUri = data.getData();
                callback.onImageSelected(imageUri);
            }
        }
    }

    private static Uri getImageUri(Activity activity, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                bitmap, "Title", null);
        return Uri.parse(path);
    }
}