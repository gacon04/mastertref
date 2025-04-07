package com.example.mastertref.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mastertref.data.local.AppDatabase;
import com.example.mastertref.data.local.TaikhoanDAO;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.example.mastertref.domain.models.TaiKhoanDTO;
import com.example.mastertref.utils.AESHelper;

import org.jspecify.annotations.NonNull;

public class TaikhoanVM extends AndroidViewModel {
    private TaikhoanDAO taikhoanDAO;
    private AppDatabase database;

    public TaikhoanVM(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
        taikhoanDAO = database.taikhoanDAO();
    }


    // tạo tài khoản đăng nhập vào đầu tiên
    public void createDefaultUserIfNotExists() {
        final TaikhoanDAO dao = this.taikhoanDAO;
        new Thread(new Runnable() {
            @Override
            public void run() {
                TaikhoanEntity existingUser = dao.getUserByEmail("crushzone610@gmail.com");
                if (existingUser == null) {
                    TaikhoanEntity newUser = new TaikhoanEntity(
                            "ngocminh04",
                            "crushzone610@gmail.com",
                            "Ngocminh2k4@",
                            "Tran Ngoc Minh",
                            "Tui yêu VN",
                            "Vietnam",
                            "default_avatar.png",
                            true
                    );
                    dao.insertUser(newUser);
                    Log.d("DATABASE", "Thêm tài khoản mặc định thành công!");
                } else {
                    Log.d("DATABASE", "Tài khoản mặc định đã tồn tại.");
                }
            }
        }).start();
    }


    public LiveData<TaikhoanEntity> getUserByUsername(String username) {
        return taikhoanDAO.getTaikhoanByUsername(username);
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

}
