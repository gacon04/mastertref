package com.example.mastertref.data.local;

import androidx.room.*;

import java.util.List;

@Dao
public interface MonAnDaLuuDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MonAnDaLuuEntity entity);

    @Query("SELECT * FROM monan_daluu WHERE user_id = :userId ORDER BY thoi_gian_luu DESC")
    List<MonAnDaLuuEntity> getSavedRecipes(int userId);

    @Query("DELETE FROM monan_daluu WHERE user_id = :userId AND mon_an_id = :monAnId")
    void deleteSavedRecipe(int userId, int monAnId);

    @Query("SELECT COUNT(*) FROM monan_daluu WHERE user_id = :userId AND mon_an_id = :monAnId")
    int isRecipeSaved(int userId, int monAnId);
}

