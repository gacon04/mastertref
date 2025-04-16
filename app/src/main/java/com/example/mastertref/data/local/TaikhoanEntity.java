package com.example.mastertref.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import com.example.mastertref.utils.AESHelper;

@Entity(tableName = "taikhoan")
public class TaikhoanEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "fullname")
    private String fullname;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "from")
    private String from;

    @ColumnInfo(name = "avatar")
    private String avatar;

    @ColumnInfo(name = "isActive")
    private boolean isActive;

    // Constructor
    public TaikhoanEntity(String username, String email, String password, String fullname, String description, String from, String avatar, boolean isActive) {
        this.username = username;
        this.email = email;
        this.password = password; // Không mã hóa ở đây nữa
        this.fullname = fullname;
        this.description = description;
        this.from = from;
        this.avatar = avatar;
        this.isActive = isActive;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; } // Không giải mã ở đây nữa
    public void setPassword(String password) { this.password = password; } // Không mã hóa ở đây nữa

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setImageLink(String avatar) {
        this.avatar = avatar;
    }
    public String getImageLink() {
        return avatar;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
