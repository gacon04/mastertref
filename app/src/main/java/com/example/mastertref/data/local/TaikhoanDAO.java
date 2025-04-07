package com.example.mastertref.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface TaikhoanDAO {
    // 🟢 Thêm tài khoản mới (Mã hóa mật khẩu trước khi lưu)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(TaikhoanEntity user);

    // 🟢 Cập nhật thông tin tài khoản (Không cập nhật mật khẩu ở đây)
    @Update
    void updateUser(TaikhoanEntity user);

    // 🟢 Xóa tài khoản
    @Delete
    void deleteUser(TaikhoanEntity user);

    // 🟢 Xóa tài khoản theo username
    @Query("DELETE FROM taikhoan WHERE username = :username")
    void deleteByUsername(String username);

    // 🟢 Kiểm tra đăng nhập (So sánh mật khẩu đã mã hóa)
    @Query("SELECT * FROM taikhoan WHERE email = :email AND password = :password LIMIT 1")
    TaikhoanEntity validLoginInfo(String email, String password);

    // 🟢 Lấy tài khoản theo username
    @Query("SELECT * FROM taikhoan WHERE username = :username LIMIT 1")
    LiveData<TaikhoanEntity> getTaikhoanByUsername(String username);

    // 🟢 Lấy tài khoản theo email
    @Query("SELECT * FROM taikhoan WHERE email = :email LIMIT 1")
    TaikhoanEntity getUserByEmail(String email);

    // 🟢 Kiểm tra email đã tồn tại chưa
    @Query("SELECT COUNT(*) FROM taikhoan WHERE email = :email")
    int isEmailExists(String email);

    // 🟢 Lấy tất cả tài khoản
    @Query("SELECT * FROM taikhoan")
    LiveData<List<TaikhoanEntity>> getAllTaikhoan();

    // 🟢 Lấy tài khoản theo ID
    @Query("SELECT * FROM taikhoan WHERE id = :userId LIMIT 1")
    TaikhoanEntity getUserById(int userId);

    // 🟢 Xác thực mật khẩu cũ trước khi cập nhật (Dùng trong chức năng đổi mật khẩu)
    @Query("SELECT * FROM taikhoan WHERE username = :username AND password = :password LIMIT 1")
    TaikhoanEntity verifyPassword(String username, String password);

    // 🟢 Cập nhật mật khẩu mới (Lưu ý: Mật khẩu mới cần được mã hóa trước khi gọi hàm này)
    @Query("UPDATE taikhoan SET password = :newPassword WHERE username = :username")
    void updatePassword(String username, String newPassword);
}
