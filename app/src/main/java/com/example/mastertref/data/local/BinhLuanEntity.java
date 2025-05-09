package com.example.mastertref.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;
import java.util.Date;
import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "binh_luan",
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
public class BinhLuanEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id", index = true)
    private int userId;

    @ColumnInfo(name = "mon_id", index = true)
    private int monId;

    @ColumnInfo(name = "noi_dung")
    private String noiDung;

    @ColumnInfo(name = "thoi_gian")
    private Date thoiGian;

    @ColumnInfo(name = "hinh_anh") // thêm cột lưu đường dẫn hình ảnh
    private String hinhAnh;

    public BinhLuanEntity(int userId, int monId, String noiDung, Date thoiGian, String hinhAnh) {
        this.userId = userId;
        this.monId = monId;
        this.noiDung = noiDung;
        this.thoiGian = thoiGian;
        this.hinhAnh = hinhAnh;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getMonId() { return monId; }
    public void setMonId(int monId) { this.monId = monId; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public Date getThoiGian() { return thoiGian; }
    public void setThoiGian(Date thoiGian) { this.thoiGian = thoiGian; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }
}

