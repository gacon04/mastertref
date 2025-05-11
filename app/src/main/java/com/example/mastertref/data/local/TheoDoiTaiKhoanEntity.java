package com.example.mastertref.data.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "theodoitaikhoan",
        foreignKeys = {
                @ForeignKey(
                        entity = TaikhoanEntity.class,
                        parentColumns = "id",
                        childColumns = "follower_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = TaikhoanEntity.class,
                        parentColumns = "id",
                        childColumns = "following_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = "follower_id"),
                @Index(value = "following_id")
        }
)
public class TheoDoiTaiKhoanEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "follower_id")
    private int followerId;

    @ColumnInfo(name = "following_id")
    private int followingId;

    @ColumnInfo(name = "thoi_gian_theo_doi")
    private long thoiGianTheoDoi; // Thời gian theo dõi

    // Constructor
    public TheoDoiTaiKhoanEntity(int followerId, int followingId, long thoiGianTheoDoi) {
        this.followerId = followerId;
        this.followingId = followingId;
        this.thoiGianTheoDoi = thoiGianTheoDoi;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFollowerId() {
        return followerId;
    }

    public void setFollowerId(int followerId) {
        this.followerId = followerId;
    }

    public int getFollowingId() {
        return followingId;
    }

    public void setFollowingId(int followingId) {
        this.followingId = followingId;
    }

    public long getThoiGianTheoDoi() {
        return thoiGianTheoDoi;
    }

    public void setThoiGianTheoDoi(long thoiGianTheoDoi) {
        this.thoiGianTheoDoi = thoiGianTheoDoi;
    }
}
