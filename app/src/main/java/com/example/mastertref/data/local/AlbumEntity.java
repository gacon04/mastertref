package com.example.mastertref.data.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "album",
        foreignKeys = @ForeignKey(
                entity = TaikhoanEntity.class,
                parentColumns = "id",
                childColumns = "taikhoan_id",
                onDelete = ForeignKey.CASCADE
        )
)
public class AlbumEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "taikhoan_id", index = true)
    private int taikhoanId;

    @ColumnInfo(name = "ten_album")
    private String tenAlbum;

    // Constructor
    public AlbumEntity(int taikhoanId, String tenAlbum) {
        this.taikhoanId = taikhoanId;
        this.tenAlbum = tenAlbum;
    }

    // Getter / Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTaikhoanId() { return taikhoanId; }
    public void setTaikhoanId(int taikhoanId) { this.taikhoanId = taikhoanId; }

    public String getTenAlbum() { return tenAlbum; }
    public void setTenAlbum(String tenAlbum) { this.tenAlbum = tenAlbum; }
}

