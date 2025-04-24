package com.example.mastertref.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.example.mastertref.domain.models.TaiKhoanDTO;

@Database(entities = {TaikhoanEntity.class, MonAnEntity.class, NguyenLieuEntity.class, BuocNauEntity.class}, // Danh sách các entity
        version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract TaikhoanDAO taikhoanDAO();
    public abstract MonAnDAO monAnDAO();
    public abstract NguyenLieuDAO nguyenLieuDAO();
    public abstract BuocNauDAO buocNauDAO();

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