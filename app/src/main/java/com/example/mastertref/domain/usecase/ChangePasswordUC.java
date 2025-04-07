package com.example.mastertref.domain.usecase;

import com.example.mastertref.data.local.TaikhoanDAO;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.example.mastertref.domain.models.ChangePasswordDTO;
import com.example.mastertref.utils.AESHelper;

public class ChangePasswordUC {
    private final TaikhoanDAO taikhoanDAO;

    public ChangePasswordUC(TaikhoanDAO taikhoanDAO) {
        this.taikhoanDAO = taikhoanDAO;
    }

    public boolean execute(ChangePasswordDTO request) {
        // Lấy thông tin tài khoản với mật khẩu giải mã
        TaikhoanEntity user = taikhoanDAO.getUserById(request.getUserId());
        if (user == null) {
            return false; // Không tìm thấy tài khoản
        }

        // Kiểm tra mật khẩu cũ (đã giải mã)
        String decryptedPassword = user.getPassword();
        if (!decryptedPassword.equals(request.getOldPassword())) {
            return false; // Mật khẩu cũ không khớp
        }

        // Cập nhật mật khẩu mới (mã hóa trước khi lưu)
        String encryptedNewPassword = AESHelper.encrypt(request.getNewPassword());
        taikhoanDAO.updatePassword(request.getUserId(), encryptedNewPassword);

        return true;
    }
}
