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
import com.example.mastertref.data.local.BinhLuanTaiKhoanEntity;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BinhLuanAdapter extends RecyclerView.Adapter<BinhLuanAdapter.BinhLuanViewHolder> {
    private List<BinhLuanTaiKhoanEntity> binhLuanList = new ArrayList<>();
    private Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public BinhLuanAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BinhLuanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_binhluan, parent, false);
        return new BinhLuanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BinhLuanViewHolder holder, int position) {
        BinhLuanTaiKhoanEntity currentItem = binhLuanList.get(position);

        // Set user information
        holder.tvName.setText(currentItem.getFullname());
        holder.tvTime.setText(dateFormat.format(currentItem.getThoiGian()));
        holder.tvFoodDescription.setText(currentItem.getNoiDung());

        // Load avatar if available
        if (currentItem.getAvatar() != null && !currentItem.getAvatar().isEmpty()) {
            Glide.with(context)
                    .load(currentItem.getAvatar())
                    .centerCrop()
                    .into(holder.userProfileImage);
        }

        // Display food image if available
        if (currentItem.getHinhAnh() != null && !currentItem.getHinhAnh().isEmpty()) {
            holder.foodImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(currentItem.getHinhAnh())
                    .centerCrop()
                    .into(holder.foodImage);
        } else {
            holder.foodImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return binhLuanList.size();
    }

    public void setBinhLuans(List<BinhLuanTaiKhoanEntity> binhLuans) {
        this.binhLuanList = binhLuans;
        notifyDataSetChanged();
    }

    class BinhLuanViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvTime, tvFoodDescription;
        private ImageView foodImage;
        private ShapeableImageView userProfileImage;

        public BinhLuanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_username);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvFoodDescription = itemView.findViewById(R.id.tv_food_description);
            foodImage = itemView.findViewById(R.id.food_image);
            userProfileImage = itemView.findViewById(R.id.user_profile_image);
        }
    }
}