package com.example.mastertref.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mastertref.data.local.BinhLuanEntity;
import com.example.mastertref.data.local.BinhLuanTaiKhoanEntity;
import com.example.mastertref.data.repository.BinhLuanRepository;
import com.example.mastertref.data.local.AppDatabase;
import com.example.mastertref.data.local.BinhLuanDAO;

import java.util.Date;
import java.util.List;

public class BinhLuanVM extends AndroidViewModel {
    private final BinhLuanDAO binhLuanDAO;
    private BinhLuanRepository repository;

    public BinhLuanVM(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        binhLuanDAO = database.binhLuanDAO();
        repository = new BinhLuanRepository(application);
    }

    // Lấy tất cả bình luận
    public LiveData<List<BinhLuanEntity>> getAllBinhLuans() {
        return repository.getAllBinhLuans();
    }

    // Lấy tất cả bình luận kèm thông tin người dùng
    public LiveData<List<BinhLuanTaiKhoanEntity>> getAllBinhLuansWithUser() {
        return repository.getAllBinhLuansWithUser();
    }

    // Lấy bình luận theo món ăn kèm thông tin người dùng
    public LiveData<List<BinhLuanTaiKhoanEntity>> getBinhLuansWithUserByMonId(int monId) {
        return binhLuanDAO.getBinhLuansWithUserByMonId(monId);
    }

    // Lấy bình luận theo món ăn
    public LiveData<List<BinhLuanEntity>> getBinhLuansByMonId(int monId) {
        return repository.getBinhLuansByMonId(monId);
    }

    // Thêm bình luận mới
    public void insertBinhLuan(int userId, int monId, String noiDung, String hinhAnh) {
        new Thread(() -> {
            try {
                BinhLuanEntity binhLuan = new BinhLuanEntity(userId, monId, noiDung, new Date(), hinhAnh);
                binhLuanDAO.insertBinhLuan(binhLuan);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Xóa bình luận
    public void deleteBinhLuan(BinhLuanEntity binhLuan) {
        repository.deleteBinhLuan(binhLuan);
    }
}
