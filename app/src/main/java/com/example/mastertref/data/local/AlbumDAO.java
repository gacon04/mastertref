package com.example.mastertref.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface AlbumDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAlbum(AlbumEntity album);

    @Query("SELECT * FROM album WHERE taikhoan_id = :userId")
    List<AlbumEntity> getAlbumsByUserId(int userId);

    @Transaction
    @Query("SELECT * FROM album WHERE id = :albumId")
    AlbumWithMonAn getAlbumWithMonAn(int albumId);
}

