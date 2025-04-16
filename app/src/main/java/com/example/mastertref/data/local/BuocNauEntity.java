package com.example.mastertref.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;
import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "buocnau",
        foreignKeys = @ForeignKey(
                entity = MonAnEntity.class,
                parentColumns = "id",
                childColumns = "monan_id",
                onDelete = CASCADE
        )
)
public class BuocNauEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "monan_id", index = true)
    private int monanId; // Liên kết với món ăn

    @ColumnInfo(name = "so_buoc")
    private int soBuoc; // Thứ tự bước nấu

    @ColumnInfo(name = "mo_ta")
    private String moTa; // Mô tả cách thực hiện



    public BuocNauEntity(int monanId, int soBuoc, String moTa) {
        this.monanId = monanId;
        this.soBuoc = soBuoc;
        this.moTa = moTa;

    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMonanId() { return monanId; }
    public void setMonanId(int monanId) { this.monanId = monanId; }

    public int getSoBuoc() { return soBuoc; }
    public void setSoBuoc(int soBuoc) { this.soBuoc = soBuoc; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }


}
