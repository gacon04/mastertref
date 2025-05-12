package com.example.mastertref.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mastertref.data.local.AppDatabase;
import com.example.mastertref.data.local.TaikhoanDAO;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.example.mastertref.data.repository.TaiKhoanRepository;
import com.example.mastertref.domain.models.TaiKhoanDTO;
import com.example.mastertref.utils.AESHelper;

import org.jspecify.annotations.NonNull;

import java.util.List;

public class TaikhoanVM extends AndroidViewModel {
    private static final String TAG = "TaikhoanVM";
    private final TaikhoanDAO taikhoanDAO;
    private final AppDatabase database;
    private final TaiKhoanRepository taikhoanRepository;

    public TaikhoanVM(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
        taikhoanDAO = database.taikhoanDAO();
        taikhoanRepository = new TaiKhoanRepository(application);
    }

    public TaikhoanDAO getTaikhoanDAO() {
        return taikhoanDAO;
    }

    // tạo tài khoản đăng nhập vào đầu tiên
    public void createDefaultUserIfNotExists() {
        new Thread(() -> {
            TaikhoanEntity existingUser = taikhoanDAO.getUserByEmail("crushzone610@gmail.com");
            if (existingUser == null) {
                TaikhoanEntity newUser = new TaikhoanEntity(
                        "ngocminh04",
                        "crushzone610@gmail.com",
                        "Ngocminh2k4@",
                        "Tran Ngoc Minh",
                        "Tui yêu VN",
                        "Vietnam",
                        "https://res.cloudinary.com/dmrexfwzv/image/upload/ic_launcher-playstore_pmtrat.png",
                        true
                );
                taikhoanDAO.insertUser(newUser);
                Log.d("DATABASE", "Thêm tài khoản mặc định thành công!");
            } else {
                Log.d("DATABASE", "Tài khoản mặc định đã tồn tại.");
            }
        }).start();
    }

    public LiveData<TaikhoanEntity> getUserByUsername(String username) {
        return taikhoanDAO.getTaikhoanByUsername(username);
    }
    public LiveData<TaikhoanEntity> getUserById(int id) {
        return taikhoanDAO.getTaikhoanByIdProFile(id);
    }

    public boolean insertAccount(TaikhoanEntity user) {
        try {
            taikhoanDAO.insertUser(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void validLoginInfo(String email, String password, OnLoginResultListener listener) {
        new Thread(() -> {
            String encryptedPassword = AESHelper.encrypt(password);
            Log.d("LOGIN", "Mật khẩu gốc: " + password);
            Log.d("LOGIN", "Mật khẩu mã hóa: " + encryptedPassword);
            
            TaikhoanEntity user = taikhoanDAO.validLoginInfo(email, encryptedPassword);
            if (user != null) {
                Log.d("LOGIN", "Mật khẩu trong DB: " + user.getPassword());
            }
            listener.onResult(user);
        }).start();
    }

    // Interface để xử lý kết quả bất đồng bộ
    public interface OnLoginResultListener {
        void onResult(TaikhoanEntity user);
    }

    public void insertUser(TaikhoanEntity user) {
        new Thread(() -> taikhoanDAO.insertUser(user)).start();
    }

    public void updateUser(TaikhoanEntity user) {
        new Thread(() -> taikhoanDAO.updateUser(user)).start();
    }

    public boolean isEmailExists(String email) {
        return taikhoanDAO.isEmailExists(email) > 0;
    }
    public void getUserIdByUsername(String username, OnUserIdResultListener listener) {
        new Thread(() -> {
            int userId = taikhoanDAO.getUserIdByUsername(username);
            listener.onResult(userId);
        }).start();
    }

    // Interface để trả kết quả bất đồng bộ
    public interface OnUserIdResultListener {
        void onResult(int userId);
    }



    /**
     * Tìm kiếm tài khoản theo tên hoặc username, loại trừ các tài khoản đã chặn hoặc bị chặn
     */
    public LiveData<List<TaikhoanEntity>> searchUsersByUsernameOrName(String query, int currentUserId) {
        return taikhoanRepository.searchUsersByUsernameOrName(query, currentUserId);
    }

    /**
     * Tìm kiếm tài khoản theo tên hoặc username, sắp xếp theo A-Z
     */
    public LiveData<List<TaikhoanEntity>> searchUsersByUsernameOrNameAZ(String query, int currentUserId) {
        return taikhoanRepository.searchUsersByUsernameOrNameAZ(query, currentUserId);
    }

    /**
     * Tìm kiếm tài khoản theo tên hoặc username, sắp xếp theo Z-A
     */
    public LiveData<List<TaikhoanEntity>> searchUsersByUsernameOrNameZA(String query, int currentUserId) {
        return taikhoanRepository.searchUsersByUsernameOrNameZA(query, currentUserId);
    }
    
    /**
     * Lấy danh sách tài khoản theo danh sách ID
     */
    public LiveData<List<TaikhoanEntity>> getUsersByIds(List<Integer> userIds) {
        return taikhoanRepository.getUsersByIds(userIds);
    }
}
