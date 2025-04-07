package com.example.mastertref.domain.models;

public class ChangePasswordDTO {
    private int userId;
    private String oldPassword;
    private String newPassword;

    // Constructor
    public ChangePasswordDTO(int userId, String oldPassword, String newPassword) {
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    // Getter & Setter
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
