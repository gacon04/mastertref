package com.example.mastertref.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;
import java.util.Date;
import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "mon_da_xem",
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
public class MonDaXemEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id", index = true)
    private int userId;

    @ColumnInfo(name = "mon_id", index = true)
    private int monId;

    @ColumnInfo(name = "view_date")
    private Date viewDate;

    public MonDaXemEntity(int userId, int monId, Date viewDate) {
        this.userId = userId;
        this.monId = monId;
        this.viewDate = viewDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getMonId() { return monId; }
    public void setMonId(int monId) { this.monId = monId; }

    public Date getViewDate() { return viewDate; }
    public void setViewDate(Date viewDate) { this.viewDate = viewDate; }
}
