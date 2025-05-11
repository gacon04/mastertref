package com.example.mastertref.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mastertref.R;

import java.io.File;

public class SplashActitvity extends AppCompatActivity {
    public void deleteDatabase(Context context) {
        File dbFile = context.getDatabasePath("mastertref.db");

        if (dbFile.exists()) {
            boolean deleted = dbFile.delete();
            File shmFile = new File(dbFile.getPath() + "-shm");
            File walFile = new File(dbFile.getPath() + "-wal");
            shmFile.delete();
            walFile.delete();

            if (deleted) {
                Log.d("Database", "Database file deleted.");
            } else {
                Log.d("Database", "Failed to delete database file.");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
//        deleteDatabase(this);

        // Sử dụng Handler để delay màn hình Splash
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActitvity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
