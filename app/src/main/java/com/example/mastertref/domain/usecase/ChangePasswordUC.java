package com.example.mastertref.domain.usecase;

import android.util.Log;

import com.example.mastertref.data.local.TaikhoanDAO;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.example.mastertref.domain.models.ChangePasswordDTO;
import com.example.mastertref.utils.AESHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChangePasswordUC {
    private static final String TAG = "ChangePasswordUC";
    private final TaikhoanDAO taikhoanDAO;
    private final ExecutorService executorService;

    public ChangePasswordUC(TaikhoanDAO taikhoanDAO) {
        this.taikhoanDAO = taikhoanDAO;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public interface OnChangePasswordListener {
        void onSuccess();
        void onError(String message);
    }

    public void execute(ChangePasswordDTO dto, OnChangePasswordListener listener) {
        executorService.execute(() -> {
            try {
                // Kiểm tra mật khẩu mới có khớp với xác nhận không
                if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
                    listener.onError("Mật khẩu mới không khớp với xác nhận!");
                    return;
                }

                // Kiểm tra mật khẩu mới có đúng định dạng không
                if (!isValidPassword(dto.getNewPassword())) {
                    listener.onError("Mật khẩu mới không đúng định dạng!");
                    return;
                }

                // Kiểm tra mật khẩu cũ
                Log.d(TAG, "Username: " + dto.getUsername());
                Log.d(TAG, "Old password: " + dto.getOldPassword());

                String encryptedOldPassword = AESHelper.encrypt(dto.getOldPassword());
                if (encryptedOldPassword == null) {
                    listener.onError("Lỗi mã hóa mật khẩu cũ!");
                    return;
                }

                TaikhoanEntity user = taikhoanDAO.verifyPassword(dto.getUsername(), encryptedOldPassword);
                if (user == null) {
                    listener.onError("Mật khẩu cũ không đúng!");
                    return;
                }

                // Cập nhật mật khẩu mới
                String encryptedNewPassword = AESHelper.encrypt(dto.getNewPassword());
                if (encryptedNewPassword == null) {
                    listener.onError("Lỗi mã hóa mật khẩu mới!");
                    return;
                }

                taikhoanDAO.updatePassword(dto.getUsername(), encryptedNewPassword);
                listener.onSuccess();
            } catch (Exception e) {
                Log.e(TAG, "Error changing password", e);
                listener.onError("Lỗi: " + e.getMessage());
            }
        });
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
