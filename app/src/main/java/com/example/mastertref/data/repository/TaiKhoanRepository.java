package com.example.mastertref.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.mastertref.data.local.AppDatabase;
import com.example.mastertref.data.local.ChanTaiKhoanEntity;
import com.example.mastertref.data.local.TaikhoanDAO;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.example.mastertref.utils.AESHelper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaiKhoanRepository {
    private final TaikhoanDAO taikhoanDAO;
    private final ExecutorService executorService;

    public TaiKhoanRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        taikhoanDAO = database.taikhoanDAO();
        executorService = Executors.newFixedThreadPool(4);
    }

    // Lấy tài khoản theo username
    public LiveData<TaikhoanEntity> getUserByUsername(String username) {
        return taikhoanDAO.getTaikhoanByUsername(username);
    }

    // Lấy tài khoản theo ID
    public void getUserById(int userId, OnUserResultListener listener) {
        executorService.execute(() -> {
            TaikhoanEntity user = taikhoanDAO.getUserById(userId);
            listener.onResult(user);
        });
    }

    // Thêm tài khoản mới
    public void insertUser(TaikhoanEntity user) {
        executorService.execute(() -> taikhoanDAO.insertUser(user));
    }

    // Cập nhật thông tin tài khoản
    public void updateUser(TaikhoanEntity user) {
        executorService.execute(() -> taikhoanDAO.updateUser(user));
    }

    // Xóa tài khoản
    public void deleteUser(TaikhoanEntity user) {
        executorService.execute(() -> taikhoanDAO.deleteUser(user));
    }

    // Kiểm tra đăng nhập
    public void validLoginInfo(String email, String password, OnUserResultListener listener) {
        executorService.execute(() -> {
            String encryptedPassword = AESHelper.encrypt(password);
            TaikhoanEntity user = taikhoanDAO.validLoginInfo(email, encryptedPassword);
            listener.onResult(user);
        });
    }

    // Kiểm tra email đã tồn tại chưa
    public void isEmailExists(String email, OnBooleanResultListener listener) {
        executorService.execute(() -> {
            boolean exists = taikhoanDAO.isEmailExists(email) > 0;
            listener.onResult(exists);
        });
    }

    // Lấy ID của tài khoản theo username
    public void getUserIdByUsername(String username, OnIntResultListener listener) {
        executorService.execute(() -> {
            int userId = taikhoanDAO.getUserIdByUsername(username);
            listener.onResult(userId);
        });
    }

    // Xác thực mật khẩu cũ
    public void verifyPassword(String username, String password, OnUserResultListener listener) {
        executorService.execute(() -> {
            String encryptedPassword = AESHelper.encrypt(password);
            TaikhoanEntity user = taikhoanDAO.verifyPassword(username, encryptedPassword);
            listener.onResult(user);
        });
    }

    // Cập nhật mật khẩu mới
    public void updatePassword(String username, String newPassword) {
        executorService.execute(() -> {
            String encryptedPassword = AESHelper.encrypt(newPassword);
            taikhoanDAO.updatePassword(username, encryptedPassword);
        });
    }

    // Lấy tất cả tài khoản
    public LiveData<List<TaikhoanEntity>> getAllUsers() {
        return taikhoanDAO.getAllTaikhoan();
    }

    // Tìm kiếm tài khoản theo tên hoặc username, loại trừ các tài khoản đã chặn hoặc bị chặn
    public LiveData<List<TaikhoanEntity>> searchUsersByUsernameOrName(String query, int currentUserId) {
        return taikhoanDAO.searchUsersByUsernameOrName(query, currentUserId);
    }

    // Tìm kiếm tài khoản theo tên hoặc username, sắp xếp theo A-Z
    public LiveData<List<TaikhoanEntity>> searchUsersByUsernameOrNameAZ(String query, int currentUserId) {
        return taikhoanDAO.searchUsersByUsernameOrNameAZ(query, currentUserId);
    }

    // Tìm kiếm tài khoản theo tên hoặc username, sắp xếp theo Z-A
    public LiveData<List<TaikhoanEntity>> searchUsersByUsernameOrNameZA(String query, int currentUserId) {
        return taikhoanDAO.searchUsersByUsernameOrNameZA(query, currentUserId);
    }

    // Kiểm tra xem người dùng có bị chặn không
    public void isUserBlockedOrBlocking(int currentUserId, int targetUserId, OnBooleanResultListener listener) {
        executorService.execute(() -> {
            boolean isBlocked = taikhoanDAO.isUserBlockedOrBlocking(currentUserId, targetUserId) > 0;
            listener.onResult(isBlocked);
        });
    }

    // Chặn một người dùng
    public void blockUser(int currentUserId, int targetUserId) {
        executorService.execute(() -> {


        });
    }

    // Bỏ chặn một người dùng
    public void unblockUser(int currentUserId, int targetUserId) {
        executorService.execute(() -> {
            // Assuming you have a DAO method to delete block records
            // database.chanTaiKhoanDAO().deleteBlock(currentUserId, targetUserId);
        });
    }

    // Interface để xử lý kết quả bất đồng bộ
    public interface OnUserResultListener {
        void onResult(TaikhoanEntity user);
    }

    public interface OnBooleanResultListener {
        void onResult(boolean result);
    }

    public interface OnIntResultListener {
        void onResult(int result);
    }
}
