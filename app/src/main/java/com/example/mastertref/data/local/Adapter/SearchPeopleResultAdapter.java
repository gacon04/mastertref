package com.example.mastertref.data.local.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mastertref.R;
import com.example.mastertref.data.local.TaikhoanEntity;

import java.util.ArrayList;
import java.util.List;

public class SearchPeopleResultAdapter extends RecyclerView.Adapter<SearchPeopleResultAdapter.UserViewHolder> {
    private final Context context;
    private List<TaikhoanEntity> userList;
    private OnUserClickListener onUserClickListener;
    private OnFollowClickListener onFollowClickListener;

    public SearchPeopleResultAdapter(Context context) {
        this.context = context;
        this.userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_people, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        TaikhoanEntity user = userList.get(position);
        
        // Set user data to views
        holder.tvFullName.setText(user.getFullname());
        holder.tvUsername.setText("@" + user.getUsername());
        
        // Load user avatar with Glide
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            Glide.with(context)
                .load(user.getAvatar())
                .placeholder(R.drawable.mastertref)
                .error(R.drawable.mastertref)
                .circleCrop()
                .into(holder.ivUserAvatar);
        } else {
            holder.ivUserAvatar.setImageResource(R.drawable.mastertref);
        }
        
        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (onUserClickListener != null) {
                onUserClickListener.onUserClick(user);
            }
        });
        
        holder.btnFollow.setOnClickListener(v -> {
            if (onFollowClickListener != null) {
                onFollowClickListener.onFollowClick(user, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    
    public void setData(List<TaikhoanEntity> users) {
        this.userList = users;
        notifyDataSetChanged();
    }
    
    public void setOnUserClickListener(OnUserClickListener listener) {
        this.onUserClickListener = listener;
    }
    
    public void setOnFollowClickListener(OnFollowClickListener listener) {
        this.onFollowClickListener = listener;
    }
    
    // Interface for item click
    public interface OnUserClickListener {
        void onUserClick(TaikhoanEntity user);
    }
    
    // Interface for follow button click
    public interface OnFollowClickListener {
        void onFollowClick(TaikhoanEntity user, int position);
    }
    
    // ViewHolder class
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserAvatar;
        TextView tvFullName, tvUsername, btnFollow;
        
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvFullName = itemView.findViewById(R.id.tv_full_name);
            tvUsername = itemView.findViewById(R.id.tv_username);
            btnFollow = itemView.findViewById(R.id.btn_follow);
        }
    }
}
