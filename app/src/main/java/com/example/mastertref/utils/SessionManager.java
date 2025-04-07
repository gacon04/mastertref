package com.example.mastertref.utils;

import android.content.Context;
import android.content.SharedPreferences;
// Class để quản lý phiên đăng nhập
public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Lưu thông tin đăng nhập
    public void saveUserSession(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }


    //Kiểm tra người dùng có đăng nhập không
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    //Lấy username của người dùng đang đăng nhập
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    // Xóa session (Đăng xuất)
    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
