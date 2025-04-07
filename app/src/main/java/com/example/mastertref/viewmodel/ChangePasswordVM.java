package com.example.mastertref.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.mastertref.data.local.TaikhoanEntity;
import com.example.mastertref.domain.usecase.ChangePasswordUC;
import com.example.mastertref.domain.models.ChangePasswordDTO;
import com.example.mastertref.utils.AESHelper;

public class ChangePasswordVM extends ViewModel {
    private final ChangePasswordUC changePasswordUseCase;

    public ChangePasswordVM(ChangePasswordUC changePasswordUseCase) {
        this.changePasswordUseCase = changePasswordUseCase;
    }

    public boolean changePassword(int userId, String oldPassword, String newPassword) {
//        TaikhoanEntity user = TaikhoanDAO.verifyPassword(userId, AESHelper.encrypt(oldPassword));
//
//        if (user == null) {
//            return false; // Mật khẩu cũ không đúng
//        }
//
//        // Mã hóa mật khẩu mới trước khi cập nhật
//        String encryptedNewPassword = AESHelper.encrypt(newPassword);
//        taikhoanDAO.updatePassword(userId, encryptedNewPassword);
        return true;
    }

}
