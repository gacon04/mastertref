package com.example.mastertref.data.local.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mastertref.R;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder> {
    private List<TaikhoanEntity> followers = new ArrayList<>();
    private Context context;
    private OnFollowerClickListener listener;
    private OnFollowButtonClickListener followButtonListener;
    private int currentUserId;
    private List<Integer> followingIds = new ArrayList<>(); // IDs of users that current user is following

    public FollowerAdapter(Context context, int currentUserId) {
        this.context = context;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public FollowerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_follow, parent, false);
        return new FollowerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerViewHolder holder, int position) {
        TaikhoanEntity follower = followers.get(position);
        
        // Set user information
        holder.txtDisplayName.setText(follower.getFullname());
        holder.txtUsername.setText("@" + follower.getUsername());
        
        // Handle profile image
        if (follower.getAvatar() != null && !follower.getAvatar().isEmpty()) {
            // Load image using your preferred image loading library (Glide, Picasso, etc.)
            // For example with Glide:
            // Glide.with(context).load(follower.getAvatar()).into(holder.imgUserAvatar);
            holder.imgUserAvatar.setVisibility(View.VISIBLE);
            holder.txtUserInitial.setVisibility(View.GONE);
            Glide.with(context)
                    .load(follower.getImageLink())
                    .placeholder(R.drawable.mastertref)
                    .error(R.drawable.mastertref)
                    .into(holder.imgUserAvatar);
        } else {
            // Show initial letter if no avatar
            holder.imgUserAvatar.setVisibility(View.GONE);
            holder.txtUserInitial.setVisibility(View.VISIBLE);
            
            String initial = follower.getFullname() != null && !follower.getFullname().isEmpty()
                    ? String.valueOf(follower.getFullname().charAt(0)).toUpperCase()
                    : "?";
            holder.txtUserInitial.setText(initial);
        }
        
        // Set follow button state
        boolean isFollowing = followingIds.contains(follower.getId());
        updateFollowButton(holder.btnFollow, isFollowing);
        
        // Don't show follow button for current user
        if (follower.getId() == currentUserId) {
            holder.btnFollow.setVisibility(View.GONE);
        } else {
            holder.btnFollow.setVisibility(View.VISIBLE);
        }
        
        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFollowerClick(follower);
            }
        });
        
        holder.btnFollow.setOnClickListener(v -> {
            if (followButtonListener != null) {
                followButtonListener.onFollowButtonClick(follower, !isFollowing);
                // Update UI immediately for better UX
                updateFollowButton(holder.btnFollow, !isFollowing);
                // Update local data
                if (!isFollowing) {
                    followingIds.add(follower.getId());
                } else {
                    followingIds.remove(Integer.valueOf(follower.getId()));
                }
            }
        });
    }

    private void updateFollowButton(Button button, boolean isFollowing) {
        if (isFollowing) {
            button.setText("Đang theo dõi");
            button.setTextColor(context.getResources().getColor(R.color.gray));
        } else {
            button.setText("Theo dõi");
            button.setTextColor(context.getResources().getColor(R.color.orange));
        }
    }

    @Override
    public int getItemCount() {
        return followers.size();
    }

    public void setFollowers(List<TaikhoanEntity> followers) {
        this.followers = followers;
        notifyDataSetChanged();
    }

    public void setFollowingIds(List<Integer> followingIds) {
        this.followingIds = followingIds;
        notifyDataSetChanged();
    }

    public void setOnFollowerClickListener(OnFollowerClickListener listener) {
        this.listener = listener;
    }

    public void setOnFollowButtonClickListener(OnFollowButtonClickListener listener) {
        this.followButtonListener = listener;
    }

    // ViewHolder class
    static class FollowerViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgUserAvatar;
        TextView txtUserInitial, txtDisplayName, txtUsername;
        Button btnFollow;

        FollowerViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUserAvatar = itemView.findViewById(R.id.imgUserAvatar);
            txtUserInitial = itemView.findViewById(R.id.txtUserInitial);
            txtDisplayName = itemView.findViewById(R.id.txtDisplayName);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }

    // Interface for item click
    public interface OnFollowerClickListener {
        void onFollowerClick(TaikhoanEntity follower);
    }

    // Interface for follow button click
    public interface OnFollowButtonClickListener {
        void onFollowButtonClick(TaikhoanEntity follower, boolean isFollowing);
    }
}
