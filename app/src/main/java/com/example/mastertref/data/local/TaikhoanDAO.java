package com.example.mastertref.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface TaikhoanDAO {
    // 🟢 Thêm tài khoản mới (Mã hóa mật khẩu trước khi lưu)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(TaikhoanEntity user);

    // 🟢 Xóa tài khoản theo username
    @Query("DELETE FROM taikhoan WHERE username = :username")
    void deleteByUsername(String username);

    // 🟢 Kiểm tra đăng nhập (So sánh mật khẩu đã mã hóa)
    @Query("SELECT * FROM taikhoan WHERE email = :email AND password = :password LIMIT 1")
    TaikhoanEntity validLoginInfo(String email, String password);

    // 🟢 Lấy tài khoản theo username
    @Query("SELECT * FROM taikhoan WHERE username = :username LIMIT 1")
    LiveData<TaikhoanEntity> getTaikhoanByUsername(String username);

    @Query("SELECT * FROM taikhoan WHERE  id = :userId LIMIT 1")
    LiveData<TaikhoanEntity> getTaikhoanByIdProFile(int userId);
    // 🟢 Lấy tài khoản theo email
    @Query("SELECT * FROM taikhoan WHERE email = :email LIMIT 1")
    TaikhoanEntity getUserByEmail(String email);



    // 🟢 Lấy tất cả tài khoản
    @Query("SELECT * FROM taikhoan")
    LiveData<List<TaikhoanEntity>> getAllTaikhoan();

    // 🟢 Lấy tài khoản theo ID


    // 🟢 Xác thực mật khẩu cũ trước khi cập nhật (Dùng trong chức năng đổi mật khẩu)
    @Query("SELECT * FROM taikhoan WHERE username = :username AND password = :password LIMIT 1")
    TaikhoanEntity verifyPassword(String username, String password);

    // 🟢 Cập nhật mật khẩu mới (Lưu ý: Mật khẩu mới cần được mã hóa trước khi gọi hàm này)
    @Query("UPDATE taikhoan SET password = :newPassword WHERE username = :username")
    void updatePassword(String username, String newPassword);

    // 🔍 Tìm kiếm tài khoản theo tên hoặc username, loại trừ các tài khoản đã chặn hoặc bị chặn


    

    // 🟢 Cập nhật thông tin tài khoản (Không cập nhật mật khẩu ở đây)
    @Update
    void updateUser(TaikhoanEntity user);

    // 🟢 Xóa tài khoản
    @Delete
    void deleteUser(TaikhoanEntity user);

    // 🔍 Lấy tài khoản theo ID
    @Query("SELECT * FROM taikhoan WHERE id = :id")
    TaikhoanEntity getUserById(int id);

    // 🔍 Lấy tài khoản theo username
    @Query("SELECT * FROM taikhoan WHERE username = :username")
    LiveData<TaikhoanEntity> getUserByUsername(String username);

    // 🔍 Lấy ID tài khoản theo username
    @Query("SELECT id FROM taikhoan WHERE username = :username")
    int getUserIdByUsername(String username);

    // 🔍 Kiểm tra username đã tồn tại chưa
    @Query("SELECT COUNT(*) FROM taikhoan WHERE username = :username")
    int isUsernameExists(String username);

    // 🔍 Kiểm tra email đã tồn tại chưa
    @Query("SELECT COUNT(*) FROM taikhoan WHERE email = :email")
    int isEmailExists(String email);

    // 🔍 Lấy tất cả tài khoản
    @Query("SELECT * FROM taikhoan")
    LiveData<List<TaikhoanEntity>> getAllUsers();

    // 🔍 Lấy danh sách tài khoản theo danh sách ID
    @Query("SELECT * FROM taikhoan WHERE id IN (:userIds)")
    LiveData<List<TaikhoanEntity>> getUsersByIds(List<Integer> userIds);

    // 🔍 Tìm kiếm tài khoản theo tên hoặc username
    @Query("SELECT * FROM taikhoan WHERE " +
            "(fullname LIKE '%' || :query || '%' OR " +
            "username LIKE '%' || :query || '%') AND " +
            "id != :currentUserId")
    LiveData<List<TaikhoanEntity>> searchUsersByUsernameOrName(String query, int currentUserId);

    // 🔍 Tìm kiếm tài khoản theo tên hoặc username, sắp xếp theo A-Z
    @Query("SELECT * FROM taikhoan WHERE " +
            "(fullname LIKE '%' || :query || '%' OR " +
            "username LIKE '%' || :query || '%') AND " +
            "id != :currentUserId " +
            "ORDER BY fullname ASC")
    LiveData<List<TaikhoanEntity>> searchUsersByUsernameOrNameAZ(String query, int currentUserId);

    // 🔍 Tìm kiếm tài khoản theo tên hoặc username, sắp xếp theo Z-A
    @Query("SELECT * FROM taikhoan WHERE " +
            "(fullname LIKE '%' || :query || '%' OR " +
            "username LIKE '%' || :query || '%') AND " +
            "id != :currentUserId " +
            "ORDER BY fullname DESC")
    LiveData<List<TaikhoanEntity>> searchUsersByUsernameOrNameZA(String query, int currentUserId);

    // 🔍 Kiểm tra đăng nhập
    @Query("SELECT * FROM taikhoan WHERE username = :username LIMIT 1")
    TaikhoanEntity getUserForLogin(String username);

    // 🔍 Kiểm tra xem người dùng có bị chặn không
    @Query("SELECT COUNT(*) FROM chantaikhoan WHERE " +
            "(blocker_id = :currentUserId AND blocked_id = :targetUserId) OR " +
            "(blocker_id = :targetUserId AND blocked_id = :currentUserId)")
    int isUserBlockedOrBlocking(int currentUserId, int targetUserId);
}