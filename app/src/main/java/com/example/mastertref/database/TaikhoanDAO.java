package com.example.mastertref.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mastertref.models.Taikhoan;

import java.util.ArrayList;
import java.util.List;

public class TaikhoanDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public TaikhoanDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
    // ktra tai khoan dang nhap hop le
    public boolean validLoginInfo(String email, String password) {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();

        String query = "SELECT " + TaikhoanContract.TaikhoanEntry.COLUMN_PASSWORD +
                " FROM " + TaikhoanContract.TaikhoanEntry.TABLE_NAME +
                " WHERE " + TaikhoanContract.TaikhoanEntry.COLUMN_EMAIL + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            String encryptedPassword = cursor.getString(0);
            cursor.close();
            return AESHelper.decrypt(encryptedPassword).equals(password); // So sánh mật khẩu giải mã với mật khẩu nhập vào
        }
        cursor.close();
        return false;
    }



    // Thêm tài khoản mới
    public long insertTaikhoan(Taikhoan taikhoan) {
        ContentValues values = new ContentValues();
        values.put(TaikhoanContract.TaikhoanEntry.COLUMN_USERNAME, taikhoan.getUsername());
        values.put(TaikhoanContract.TaikhoanEntry.COLUMN_PASSWORD, AESHelper.encrypt(taikhoan.getPassword())); // Mã hóa mật khẩu
        values.put(TaikhoanContract.TaikhoanEntry.COLUMN_EMAIL, taikhoan.getEmail());
        values.put(TaikhoanContract.TaikhoanEntry.COLUMN_FULLNAME, taikhoan.getFullname());
        values.put(TaikhoanContract.TaikhoanEntry.COLUMN_ROLE, taikhoan.getRole());
        values.put(TaikhoanContract.TaikhoanEntry.COLUMN_AVATAR, taikhoan.getAvatar());
        values.put(TaikhoanContract.TaikhoanEntry.COLUMN_FROM, taikhoan.getFrom());
        values.put(TaikhoanContract.TaikhoanEntry.COLUMN_DESCRIPTION, taikhoan.getDescription());
        values.put(TaikhoanContract.TaikhoanEntry.COLUMN_IS_ACTIVE, taikhoan.getIsActive());

        return db.insert(TaikhoanContract.TaikhoanEntry.TABLE_NAME, null, values);
    }

    public Taikhoan getTaikhoanByUsername(String username) {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        Taikhoan taikhoan = null;

        Cursor cursor = db.rawQuery("SELECT * FROM " + TaikhoanContract.TaikhoanEntry.TABLE_NAME +
                " WHERE " + TaikhoanContract.TaikhoanEntry.COLUMN_USERNAME + " = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            taikhoan = new Taikhoan(
                    cursor.getString(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_EMAIL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_ROLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_FULLNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_AVATAR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_FROM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_IS_ACTIVE))
            );
        }

        cursor.close();
        return taikhoan;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        boolean exists = false;

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TaikhoanContract.TaikhoanEntry.TABLE_NAME +
                " WHERE " + TaikhoanContract.TaikhoanEntry.COLUMN_EMAIL + " = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            exists = (count > 0); // Nếu count > 0, email đã tồn tại
        }

        cursor.close();
        return exists;
    }


    // Lấy tất cả tài khoản
    public List<Taikhoan> getAllTaikhoan() {
        List<Taikhoan> list = new ArrayList<>();
        Cursor cursor = db.query(
                TaikhoanContract.TaikhoanEntry.TABLE_NAME,
                null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Taikhoan taikhoan = new Taikhoan(
                        cursor.getString(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_USERNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_EMAIL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_ROLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_FULLNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_AVATAR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_FROM)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaikhoanContract.TaikhoanEntry.COLUMN_IS_ACTIVE))
                );
                list.add(taikhoan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}
