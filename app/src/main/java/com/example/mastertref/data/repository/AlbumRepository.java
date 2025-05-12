package com.example.mastertref.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.mastertref.data.local.AlbumDAO;
import com.example.mastertref.data.local.AlbumEntity;
import com.example.mastertref.data.local.AlbumMonAnCrossRef;
import com.example.mastertref.data.local.AlbumMonAnDAO;
import com.example.mastertref.data.local.AlbumWithMonAn;
import com.example.mastertref.data.local.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlbumRepository {
    private AlbumDAO albumDAO;
    private AlbumMonAnDAO albumMonAnDAO;
    private ExecutorService executorService;

    public AlbumRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        albumDAO = database.albumDao();
        albumMonAnDAO = database.albumMonAnDao();
        executorService = Executors.newSingleThreadExecutor();
    }
    
    // Add a method to clean up resources
    public void cleanup() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    // Album operations
    public LiveData<List<AlbumEntity>> getAlbumsByUserId(int userId) {
        return albumDAO.getAlbumsByUserId(userId);
    }

    public LiveData<AlbumWithMonAn> getAlbumWithMonAnById(int albumId) {
        return albumDAO.getAlbumWithMonAnById(albumId);
    }

    public LiveData<List<AlbumWithMonAn>> getAlbumsWithMonAnByUserId(int userId) {
        return albumDAO.getAlbumsWithMonAnByUserId(userId);
    }

    public void insertAlbum(AlbumEntity album, InsertAlbumCallback callback) {
        executorService.execute(() -> {
            long albumId = albumDAO.insertAlbum(album);
            if (callback != null) {
                callback.onAlbumInserted((int) albumId);
            }
        });
    }

    public void updateAlbum(AlbumEntity album) {
        executorService.execute(() -> albumDAO.updateAlbum(album));
    }

    public void deleteAlbum(AlbumEntity album) {
        executorService.execute(() -> albumDAO.deleteAlbum(album));
    }

    // Album-MonAn relationship operations
    public void addMonAnToAlbum(int albumId, int monAnId) {
        AlbumMonAnCrossRef crossRef = new AlbumMonAnCrossRef();
        crossRef.albumId = albumId;
        crossRef.monAnId = monAnId;
        executorService.execute(() -> albumMonAnDAO.insert(crossRef));
    }

    public void removeMonAnFromAlbum(int albumId, int monAnId) {
        AlbumMonAnCrossRef crossRef = new AlbumMonAnCrossRef();
        crossRef.albumId = albumId;
        crossRef.monAnId = monAnId;
        executorService.execute(() -> albumMonAnDAO.delete(crossRef));
    }

    // Callback interface for album insertion
    public interface InsertAlbumCallback {
        void onAlbumInserted(int albumId);
    }
}
