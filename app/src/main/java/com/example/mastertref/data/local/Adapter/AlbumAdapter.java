package com.example.mastertref.data.local.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mastertref.R;
import com.example.mastertref.data.local.AlbumWithMonAn;
import com.example.mastertref.data.local.MonAnEntity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    // Remove the duplicate field
    // private Context context; <- Remove this line
    private List<AlbumWithMonAn> albums = new ArrayList<>();
    private OnAlbumClickListener onAlbumClickListener;

    // Make sure you're using a weak reference to the context or activity
    private final WeakReference<Context> contextRef;
    
    public AlbumAdapter(Context context) {
        this.contextRef = new WeakReference<>(context);
        this.albums = new ArrayList<>();
    }
    
    // When accessing the context, check if it's still valid
    private Context getContext() {
        Context context = contextRef.get();
        if (context == null) {
            Log.e("AlbumAdapter", "Context is null");
        }
        return context;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use getContext() instead of context
        Context context = getContext();
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false);
        return new AlbumViewHolder(view);
    }

    // In loadRecipeImages method, use getContext() instead of context
    private void loadRecipeImages(AlbumViewHolder holder, List<MonAnEntity> recipes) {
        // Get all ImageViews from the GridLayout
        int childCount = holder.gridImages.getChildCount();
        List<ImageView> imageViews = new ArrayList<>();
        
        for (int i = 0; i < childCount; i++) {
            View child = holder.gridImages.getChildAt(i);
            if (child instanceof ImageView) {
                imageViews.add((ImageView) child);
            }
        }
        
        // Load images into the grid (up to 4)
        Context context = getContext();
        if (context == null) return;
        
        int maxImagesToShow = Math.min(recipes.size(), 4);
        for (int i = 0; i < maxImagesToShow; i++) {
            if (i < imageViews.size()) {
                String imageUrl = recipes.get(i).getHinhAnh();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(context)
                            .load(imageUrl)
                            .centerCrop()
                            .placeholder(R.drawable.mastertref)
                            .error(R.drawable.mastertref)
                            .into(imageViews.get(i));
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        AlbumWithMonAn album = albums.get(position);
        
        // Set album name
        holder.textCollectionName.setText(album.album.getTenAlbum());
        
        // Set item count
        int itemCount = album.monAnList != null ? album.monAnList.size() : 0;
        holder.textItemCount.setText(itemCount + " recipes");
        
        // Handle display of album content
        if (album.monAnList != null && !album.monAnList.isEmpty()) {
            // If album has recipes, show grid of images
            holder.gridImages.setVisibility(View.VISIBLE);
            holder.imageIcon.setVisibility(View.GONE);
            
            // Load up to 4 images from recipes
            loadRecipeImages(holder, album.monAnList);
        } else {
            // If album is empty, show heart icon
            holder.gridImages.setVisibility(View.GONE);
            holder.imageIcon.setVisibility(View.VISIBLE);
        }
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (onAlbumClickListener != null) {
                onAlbumClickListener.onAlbumClick(album);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return albums.size();
    }
    
    public void setAlbums(List<AlbumWithMonAn> albums) {
        if (albums == null) {
            Log.e("AlbumAdapter", "Trying to set null albums list");
            this.albums = new ArrayList<>();
        } else {
            this.albums = new ArrayList<>(albums); // Create a new copy to avoid reference issues
            Log.d("AlbumAdapter", "Setting " + albums.size() + " albums in adapter");
        }
        notifyDataSetChanged();
    }
    
    public void addAlbum(AlbumWithMonAn album) {
        this.albums.add(album);
        notifyItemInserted(albums.size() - 1);
    }
    
    public void updateAlbum(AlbumWithMonAn album) {
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).album.getId() == album.album.getId()) {
                albums.set(i, album);
                notifyItemChanged(i);
                break;
            }
        }
    }
    
    public void removeAlbum(int albumId) {
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).album.getId() == albumId) {
                albums.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }
    
    // ViewHolder class
    public static class AlbumViewHolder extends RecyclerView.ViewHolder {
        TextView textCollectionName, textItemCount;
        ImageView imageIcon;
        GridLayout gridImages;
        
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            textCollectionName = itemView.findViewById(R.id.textCollectionName);
            textItemCount = itemView.findViewById(R.id.textItemCount);
            imageIcon = itemView.findViewById(R.id.imageIcon);
            gridImages = itemView.findViewById(R.id.gridImages);
        }
    }
    
    // Interface for click listener
    public interface OnAlbumClickListener {
        void onAlbumClick(AlbumWithMonAn album);
    }
    
    // Setter method for listener
    public void setOnAlbumClickListener(OnAlbumClickListener listener) {
        this.onAlbumClickListener = listener;
    }
}
