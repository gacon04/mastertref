package com.example.mastertref.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface AlbumMonAnDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(AlbumMonAnCrossRef crossRef);

    @Delete
    void delete(AlbumMonAnCrossRef crossRef);
}
