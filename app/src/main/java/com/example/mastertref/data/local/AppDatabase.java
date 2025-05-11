package com.example.mastertref.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mastertref.data.local.BinhLuanDAO;
import com.example.mastertref.domain.models.ChangePasswordDTO;
import com.example.mastertref.domain.models.TaiKhoanDTO;

@Database(entities = {
    TaikhoanEntity.class, 
    MonAnEntity.class, 
    NguyenLieuEntity.class, 
    BuocNauEntity.class,
    BinhLuanEntity.class,
    ChanTaiKhoanEntity.class,
    TheoDoiTaiKhoanEntity.class,
    LichsuTimkiemEntity.class,
        MonAnDaLuuEntity.class,
}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract TaikhoanDAO taikhoanDAO();
    public abstract MonAnDAO monAnDAO();
    public abstract NguyenLieuDAO nguyenLieuDAO();
    public abstract BuocNauDAO buocNauDAO();
    public abstract BinhLuanDAO binhLuanDAO();
    public abstract LichsuTimkiemDAO lichSuTimKiemDAO();
    public abstract TheoDoiTaiKhoanDAO theoDoiTaiKhoanDAO();
    public abstract ChanTaiKhoanDAO chanTaiKhoanDAO();
    public abstract MonAnDaLuuDAO monAnDaLuuDAO();




    public static AppDatabase getInstance(Context context) { // single ton
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "mastertref.db")
                            .fallbackToDestructiveMigration() // Xóa database nếu có thay đổi version
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}