// AddRecipeViewModel.java
package com.example.mastertref.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.mastertref.data.local.BuocNauEntity;
import com.example.mastertref.data.local.MonAnEntity;
import com.example.mastertref.data.local.NguyenLieuEntity;
import com.example.mastertref.data.repository.RecipeRepository;

import java.util.List;

public class AddRecipeVM extends AndroidViewModel {
    private final RecipeRepository repository;

    public AddRecipeVM(@NonNull Application application) {
        super(application);
        repository = new RecipeRepository(application);
    }

    public void addNewRecipe(MonAnEntity monAn, List<NguyenLieuEntity> ingredients,
                             List<BuocNauEntity> steps, AddRecipeCallback callback) {
        try {
            // Thực hiện insert dữ liệu
            repository.insertFullRecipe(monAn, ingredients, steps);

            // Nếu thành công, gọi callback.onSuccess()
            if (callback != null) {
                callback.onSuccess();
            }
        } catch (Exception e) {
            // Nếu có lỗi, gọi callback.onError()
            if (callback != null) {
                callback.onError(e.getMessage());
            }
        }
    }
    public interface AddRecipeCallback {
        void onSuccess();
        void onError(String error);
    }
}
