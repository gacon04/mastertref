package com.example.mastertref.domain.models;

import com.example.mastertref.data.local.TaikhoanEntity;

public class TaiKhoanDTO {
    private int id;
    private String username;
    private String email;
    private String fullname;
    private String description;
    private String from;
    private String avatar;
    private boolean isActive;

    // Constructor
    public TaiKhoanDTO(int id, String username, String email, String fullname, String description, String from, String avatar, boolean isActive) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.description = description;
        this.from = from;
        this.avatar = avatar;
        this.isActive = isActive;
    }

    // Phương thức chuyển đổi từ Entity sang DTO
    public static TaiKhoanDTO fromEntity(TaikhoanEntity entity) {
        return new TaiKhoanDTO(
            entity.getId(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getFullname(),
            entity.getDescription(),
            entity.getFrom(),
            entity.getAvatar(),
            entity.isActive()
        );
    }

    // Phương thức chuyển đổi từ DTO sang Entity
    public TaikhoanEntity toEntity() {
        TaikhoanEntity entity = new TaikhoanEntity(
            username,
            email,
            "", // password sẽ được set riêng
            fullname,
            description,
            from,
            avatar,
            isActive
        );
        entity.setId(id);
        return entity;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
