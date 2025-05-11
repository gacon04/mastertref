package com.example.mastertref.data.local;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "album_monan",
        primaryKeys = {"albumId", "monAnId"},
        foreignKeys = {
                @ForeignKey(
                        entity = AlbumEntity.class,
                        parentColumns = "id",
                        childColumns = "albumId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = MonAnEntity.class,
                        parentColumns = "id",
                        childColumns = "monAnId",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class AlbumMonAnCrossRef {
    public int albumId;
    public int monAnId;
}
