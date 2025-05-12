package com.example.mastertref.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AlbumMonAnDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(AlbumMonAnCrossRef crossRef);

    @Delete
    void delete(AlbumMonAnCrossRef crossRef);
    
    @Query("DELETE FROM album_monan WHERE albumId = :albumId")
    void deleteAllMonAnFromAlbum(int albumId);
    
    @Query("SELECT * FROM album_monan WHERE albumId = :albumId")
    List<AlbumMonAnCrossRef> getMonAnRefsForAlbum(int albumId);
    
    @Query("SELECT COUNT(*) FROM album_monan WHERE albumId = :albumId AND monAnId = :monAnId")
    int isMonAnInAlbum(int albumId, int monAnId);
}
