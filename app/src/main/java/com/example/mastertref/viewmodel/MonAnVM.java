package com.example.mastertref.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mastertref.data.local.MonAnEntity;
import com.example.mastertref.data.local.MonAnWithChiTiet;
import com.example.mastertref.data.repository.MonAnRepository;

import java.util.List;

public class MonAnVM extends AndroidViewModel {
    private MonAnRepository repository;
    private LiveData<List<MonAnWithChiTiet>> monAnWithChiTietList;
    public MonAnVM(@NonNull Application application) {
        super(application);
        repository = new MonAnRepository(application);
    }

    public LiveData<List<MonAnEntity>> getMonAnByUsername(String username) {
        return repository.getMonAnByUsername(username);
    }
    public LiveData<List<MonAnWithChiTiet>> getMonAnWithChiTietByUsername(String username) {
        return repository.getMonAnWithChiTietByUsername(username);
    }
    public LiveData<MonAnWithChiTiet> getMonAnWithChiTietById(int id) {
        return repository.getMonAnWithChiTietById(id);
    }


}
