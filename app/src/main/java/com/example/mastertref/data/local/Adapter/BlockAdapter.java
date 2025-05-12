package com.example.mastertref.data.local.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mastertref.R;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.BlockViewHolder> {
    private Context context;
    private List<TaikhoanEntity> blockedUsers = new ArrayList<>();
    private int currentUserId;
    
    // Interfaces for click listeners
    private OnBlockedUserClickListener onBlockedUserClickListener;
    private OnUnblockButtonClickListener onUnblockButtonClickListener;
    
    public BlockAdapter(Context context, int currentUserId) {
        this.context = context;
        this.currentUserId = currentUserId;
    }
    
    @NonNull
    @Override
    public BlockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_follow, parent, false);
        return new BlockViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull BlockViewHolder holder, int position) {
        TaikhoanEntity blockedUser = blockedUsers.get(position);
        
        // Set user information
        holder.txtDisplayName.setText(blockedUser.getFullname());
        holder.txtUsername.setText("@" + blockedUser.getUsername());
        
        // Handle profile image
        if (blockedUser.getImageLink() != null && !blockedUser.getImageLink().isEmpty()) {
            // Load image using your preferred image loading library
            holder.imgUserAvatar.setVisibility(View.VISIBLE);
            holder.txtUserInitial.setVisibility(View.GONE);
            
            // Example with Glide (you would need to add Glide dependency)
            // Glide.with(context).load(blockedUser.getProfileImage()).into(holder.imgUserAvatar);
        } else {
            // Show initial letter if no profile image
            holder.imgUserAvatar.setVisibility(View.GONE);
            holder.txtUserInitial.setVisibility(View.VISIBLE);
            
            String initial = blockedUser.getFullname().substring(0, 1).toUpperCase();
            holder.txtUserInitial.setText(initial);
        }
        
        // Change follow button to unblock button
        holder.btnFollow.setText("Bỏ chặn");
        holder.btnFollow.setTextColor(context.getResources().getColor(R.color.gray));
        
        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (onBlockedUserClickListener != null) {
                onBlockedUserClickListener.onBlockedUserClick(blockedUser);
            }
        });
        
        holder.btnFollow.setOnClickListener(v -> {
            if (onUnblockButtonClickListener != null) {
                onUnblockButtonClickListener.onUnblockButtonClick(blockedUser);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return blockedUsers.size();
    }
    
    public void setBlockedUsers(List<TaikhoanEntity> blockedUsers) {
        this.blockedUsers = blockedUsers;
        notifyDataSetChanged();
    }
    
    public void removeUser(int userId) {
        for (int i = 0; i < blockedUsers.size(); i++) {
            if (blockedUsers.get(i).getId() == userId) {
                blockedUsers.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }
    
    // ViewHolder class
    public static class BlockViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgUserAvatar;
        TextView txtUserInitial, txtDisplayName, txtUsername;
        Button btnFollow;
        
        public BlockViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUserAvatar = itemView.findViewById(R.id.imgUserAvatar);
            txtUserInitial = itemView.findViewById(R.id.txtUserInitial);
            txtDisplayName = itemView.findViewById(R.id.txtDisplayName);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }
    
    // Interface definitions
    public interface OnBlockedUserClickListener {
        void onBlockedUserClick(TaikhoanEntity blockedUser);
    }
    
    public interface OnUnblockButtonClickListener {
        void onUnblockButtonClick(TaikhoanEntity blockedUser);
    }
    
    // Setter methods for listeners
    public void setOnBlockedUserClickListener(OnBlockedUserClickListener listener) {
        this.onBlockedUserClickListener = listener;
    }
    
    public void setOnUnblockButtonClickListener(OnUnblockButtonClickListener listener) {
        this.onUnblockButtonClickListener = listener;
    }
}
