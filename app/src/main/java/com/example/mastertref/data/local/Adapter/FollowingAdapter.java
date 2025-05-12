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

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder> {
    private List<TaikhoanEntity> followingUsers = new ArrayList<>();
    private Context context;
    private OnFollowingClickListener listener;
    private OnUnfollowButtonClickListener unfollowButtonListener;
    private int currentUserId;

    public FollowingAdapter(Context context, int currentUserId) {
        this.context = context;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public FollowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_follow, parent, false);
        return new FollowingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingViewHolder holder, int position) {
        TaikhoanEntity followingUser = followingUsers.get(position);
        
        // Set user information
        holder.txtDisplayName.setText(followingUser.getFullname());
        holder.txtUsername.setText("@" + followingUser.getUsername());
        
        // Handle profile image
        if (followingUser.getAvatar() != null && !followingUser.getAvatar().isEmpty()) {
            // Load image using your preferred image loading library (Glide, Picasso, etc.)
            // For example with Glide:
            // Glide.with(context).load(followingUser.getAvatar()).into(holder.imgUserAvatar);
            holder.imgUserAvatar.setVisibility(View.VISIBLE);
            holder.txtUserInitial.setVisibility(View.GONE);
        } else {
            // Show initial letter if no avatar
            holder.imgUserAvatar.setVisibility(View.GONE);
            holder.txtUserInitial.setVisibility(View.VISIBLE);

            String initial = followingUser.getFullname() != null && !followingUser.getFullname().isEmpty()
                    ? String.valueOf(followingUser.getFullname().charAt(0)).toUpperCase()
                    : "?";
            holder.txtUserInitial.setText(initial);
        }
        
        // Set unfollow button state - always "Bỏ theo dõi" since these are users we're following
        holder.btnFollow.setText("Bỏ theo dõi");
        holder.btnFollow.setTextColor(context.getResources().getColor(R.color.gray));
        
        // Don't show follow button for current user
        if (followingUser.getId() == currentUserId) {
            holder.btnFollow.setVisibility(View.GONE);
        } else {
            holder.btnFollow.setVisibility(View.VISIBLE);
        }
        
        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFollowingClick(followingUser);
            }
        });
        
        holder.btnFollow.setOnClickListener(v -> {
            if (unfollowButtonListener != null) {
                unfollowButtonListener.onUnfollowButtonClick(followingUser);
                // We don't remove the item immediately to avoid UI glitches
                // The fragment will handle refreshing the list
            }
        });
    }

    @Override
    public int getItemCount() {
        return followingUsers.size();
    }

    public void setFollowingUsers(List<TaikhoanEntity> followingUsers) {
        this.followingUsers = followingUsers;
        notifyDataSetChanged();
    }

    public void removeUser(int userId) {
        for (int i = 0; i < followingUsers.size(); i++) {
            if (followingUsers.get(i).getId() == userId) {
                followingUsers.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void setOnFollowingClickListener(OnFollowingClickListener listener) {
        this.listener = listener;
    }

    public void setOnUnfollowButtonClickListener(OnUnfollowButtonClickListener listener) {
        this.unfollowButtonListener = listener;
    }

    // ViewHolder class
    static class FollowingViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgUserAvatar;
        TextView txtUserInitial, txtDisplayName, txtUsername;
        Button btnFollow;

        FollowingViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUserAvatar = itemView.findViewById(R.id.imgUserAvatar);
            txtUserInitial = itemView.findViewById(R.id.txtUserInitial);
            txtDisplayName = itemView.findViewById(R.id.txtDisplayName);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }

    // Interface for item click
    public interface OnFollowingClickListener {
        void onFollowingClick(TaikhoanEntity followingUser);
    }

    // Interface for unfollow button click
    public interface OnUnfollowButtonClickListener {
        void onUnfollowButtonClick(TaikhoanEntity followingUser);
    }
}
