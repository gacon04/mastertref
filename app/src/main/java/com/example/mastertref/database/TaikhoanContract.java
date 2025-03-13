package com.example.mastertref.database;

import android.provider.BaseColumns;

public class TaikhoanContract {


    private TaikhoanContract() {} // Ngăn chặn khởi tạo

    public static class TaikhoanEntry implements BaseColumns {
        public static final String TABLE_NAME = "taikhoan";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_FULLNAME = "fullname";
        public static final String COLUMN_AVATAR = "avatar";
        public static final String COLUMN_ROLE = "role";
        public static final String COLUMN_FROM = "from_where"; // "from" là từ khóa SQL nên đổi tên
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IS_ACTIVE = "is_active";

        // Câu lệnh tạo bảng
        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                        COLUMN_PASSWORD + " TEXT NOT NULL, " +
                        COLUMN_EMAIL + " TEXT NOT NULL, " +
                        COLUMN_FULLNAME + " TEXT, " +
                        COLUMN_AVATAR + " TEXT, "+
                        COLUMN_ROLE + " INTEGER, " +
                        COLUMN_FROM + " TEXT, " +
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_IS_ACTIVE + " INTEGER DEFAULT 1)";

        // Câu lệnh xóa bảng
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
