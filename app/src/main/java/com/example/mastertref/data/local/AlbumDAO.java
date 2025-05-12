package com.example.mastertref.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlbumDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAlbum(AlbumEntity album);

    @Update
    void updateAlbum(AlbumEntity album);

    @Delete
    void deleteAlbum(AlbumEntity album);

    @Query("SELECT * FROM album WHERE id = :albumId")
    AlbumEntity getAlbumById(int albumId);

    @Query("SELECT * FROM album WHERE taikhoan_id = :userId")
    LiveData<List<AlbumEntity>> getAlbumsByUserId(int userId);

    @Transaction
    @Query("SELECT * FROM album WHERE id = :albumId")
    LiveData<AlbumWithMonAn> getAlbumWithMonAnById(int albumId);

    @Transaction
    @Query("SELECT * FROM album WHERE taikhoan_id = :userId")
    LiveData<List<AlbumWithMonAn>> getAlbumsWithMonAnByUserId(int userId);
}

