package com.example.mastertref.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;
import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "cam_xuc",
        foreignKeys = {
                @ForeignKey(
                        entity = TaikhoanEntity.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = CASCADE
                ),
                @ForeignKey(
                        entity = MonAnEntity.class,
                        parentColumns = "id",
                        childColumns = "mon_id",
                        onDelete = CASCADE
                )
        }
)
public class CamXucEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id", index = true)
    private int userId;

    @ColumnInfo(name = "mon_id", index = true)
    private int monId;

    @ColumnInfo(name = "loai_cam_xuc")
    private int loaiCamXuc; // 1: 😋, 2: ❤️, 3: 👏

    public CamXucEntity(int userId, int monId, int loaiCamXuc) {
        this.userId = userId;
        this.monId = monId;
        this.loaiCamXuc = loaiCamXuc;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getMonId() { return monId; }
    public void setMonId(int monId) { this.monId = monId; }

    public int getLoaiCamXuc() { return loaiCamXuc; }
    public void setLoaiCamXuc(int loaiCamXuc) { this.loaiCamXuc = loaiCamXuc; }
}
