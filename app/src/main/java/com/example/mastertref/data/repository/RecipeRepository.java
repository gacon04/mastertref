// RecipeRepository.java
package com.example.mastertref.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mastertref.data.local.AppDatabase;
import com.example.mastertref.data.local.BuocNauDAO;
import com.example.mastertref.data.local.BuocNauEntity;
import com.example.mastertref.data.local.MonAnDAO;
import com.example.mastertref.data.local.MonAnEntity;
import com.example.mastertref.data.local.NguyenLieuDAO;
import com.example.mastertref.data.local.NguyenLieuEntity;
import com.example.mastertref.viewmodel.AddRecipeVM;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeRepository {
    private final MonAnDAO monAnDAO;
    private final NguyenLieuDAO nguyenLieuDAO;
    private final BuocNauDAO buocNauDAO;
    private final ExecutorService executorService;

    public RecipeRepository(@NonNull Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        monAnDAO = db.monAnDAO();
        nguyenLieuDAO = db.nguyenLieuDAO();
        buocNauDAO = db.buocNauDAO();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertFullRecipe(MonAnEntity monAn, List<NguyenLieuEntity> nguyenLieus,
                                 List<BuocNauEntity> buocNaus, AddRecipeVM.AddRecipeCallback callback) {
        executorService.execute(() -> {
            try {
                Log.d("AddRecipeRepo", "Chuẩn bị insert món ăn: " + monAn.toString());
                long monAnId = monAnDAO.insertMonAn(monAn);
                Log.d("AddRecipeRepo", "ID món ăn mới: " + monAnId);
                for (NguyenLieuEntity ing : nguyenLieus) {
                    ing.setMonanId((int) monAnId);
                }
                for (BuocNauEntity step : buocNaus) {
                    step.setMonanId((int) monAnId);
                }
                nguyenLieuDAO.insertNguyenLieuList(nguyenLieus);
                buocNauDAO.insertBuocNauList(buocNaus);
               ;

                if (callback != null) {
                    callback.onSuccess();
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onError(e.getMessage());
                }
            }
        });
    }

}
