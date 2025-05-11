package com.example.mastertref.data.local;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class AlbumWithMonAn {
    @Embedded
    public AlbumEntity album;

    @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(
                    value = AlbumMonAnCrossRef.class,
                    parentColumn = "albumId",
                    entityColumn = "monAnId"
            )
    )
    public List<MonAnEntity> monAnList;
}
