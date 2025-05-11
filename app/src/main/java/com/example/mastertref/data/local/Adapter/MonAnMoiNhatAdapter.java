package com.example.mastertref.data.local.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mastertref.R;
import com.example.mastertref.data.local.MonAnWithChiTiet;
import com.example.mastertref.utils.ImageHelper;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class MonAnMoiNhatAdapter extends RecyclerView.Adapter<MonAnMoiNhatAdapter.MonAnViewHolder> {
    private List<MonAnWithChiTiet> monAnList = new ArrayList<>();
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int monAnId);
    }

    public MonAnMoiNhatAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<MonAnWithChiTiet> monAnList) {
        this.monAnList = monAnList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MonAnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_horizontal, parent, false);
        return new MonAnViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MonAnViewHolder holder, int position) {
        MonAnWithChiTiet currentItem = monAnList.get(position);
        
        // Set recipe title
        holder.tvRecipeTitle.setText(currentItem.getMonAn().getTenMonAn());
        
        // Set recipe description
        holder.tvRecipeDescription.setText(currentItem.getMonAn().getMoTa());
        
        // Set recipe image
        if (currentItem.getMonAn().getHinhAnh() != null && !currentItem.getMonAn().getHinhAnh().isEmpty()) {
            ImageHelper.loadImage(holder.imgRecipe, currentItem.getMonAn().getHinhAnh());
        } else {
            holder.imgRecipe.setImageResource(R.drawable.mastertref);
        }
        
        // Set author name
        if (currentItem.getNguoiDang() != null) {
            holder.tvAuthorName.setText(currentItem.getNguoiDang().getFullname());
            
            // Set author avatar
            if (currentItem.getNguoiDang().getImageLink() != null && !currentItem.getNguoiDang().getImageLink().isEmpty()) {
                ImageHelper.loadImage(holder.imgAuthor, currentItem.getNguoiDang().getImageLink());
            } else {
                holder.imgAuthor.setImageResource(R.drawable.mastertref);
            }
        }
        if (currentItem.getMonAn().getThoiGian()!=null && !currentItem.getMonAn().getThoiGian().isEmpty()) {
            holder.imgClock.setVisibility(View.VISIBLE);
            holder.tvCookTime.setVisibility(View.VISIBLE);
        } else {
            holder.imgClock.setVisibility(View.GONE);
            holder.tvCookTime.setVisibility(View.GONE);
        }
        // Set cooking time
        holder.tvCookTime.setText(currentItem.getMonAn().getThoiGian());
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentItem.getMonAn().getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return monAnList.size();
    }

    static class MonAnViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRecipeTitle, tvRecipeDescription, tvAuthorName, tvCookTime;
        private ImageView imgRecipe, imgClock;
        private ShapeableImageView imgAuthor;

        public MonAnViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipeTitle = itemView.findViewById(R.id.tvRecipeTitle);
            tvRecipeDescription = itemView.findViewById(R.id.tvRecipeDescription);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            tvCookTime = itemView.findViewById(R.id.tvCookTime);
            imgRecipe = itemView.findViewById(R.id.imgRecipe);
            imgAuthor = itemView.findViewById(R.id.imgAuthor);
            imgClock = itemView.findViewById(R.id.imgClock);
        }
    }
}
