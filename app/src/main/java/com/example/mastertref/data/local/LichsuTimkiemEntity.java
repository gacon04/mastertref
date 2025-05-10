
package com.example.mastertref.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;
import java.util.Date;
import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "lichsu_timkiem",
        foreignKeys = @ForeignKey(
                entity = TaikhoanEntity.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = CASCADE
        )
)
public class LichsuTimkiemEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id", index = true)
    private int userId;

    @ColumnInfo(name = "keyword")
    private String keyword;

    @ColumnInfo(name = "search_date")
    private long searchDate;

    public LichsuTimkiemEntity(int userId, String keyword, long searchDate) {
        this.userId = userId;
        this.keyword = keyword;
        this.searchDate = searchDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public long getSearchDate() { return searchDate; }
    public void setSearchDate(long searchDate) { this.searchDate = searchDate; }
}

