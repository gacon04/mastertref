package com.example.mastertref.data.local.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mastertref.R;
import com.example.mastertref.data.local.MonAnEntity;
import com.example.mastertref.data.local.MonAnWithChiTiet;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.example.mastertref.ui.ChiTietMonAnActivity;

import java.util.ArrayList;
import java.util.List;

public class MonAnGoiYAdapter extends RecyclerView.Adapter<MonAnGoiYAdapter.MonAnGoiYViewHolder> {
    private final List<MonAnWithChiTiet> list;
    private Context context;

    public MonAnGoiYAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    public void setData(List<MonAnWithChiTiet> data) {
        list.clear();
        if (data != null) {
            list.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MonAnGoiYViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mon_an_goiy, parent, false);
        return new MonAnGoiYViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonAnGoiYViewHolder holder, int position) {
        MonAnWithChiTiet monAnChiTiet = list.get(position);
        MonAnEntity monAn = monAnChiTiet.getMonAn();
        TaikhoanEntity tacGia = monAnChiTiet.getNguoiDang();

        // Set recipe title
        holder.tvRecipeTitle.setText(monAn.getTenMonAn());

        // Set author name
        if (tacGia != null) {
            holder.tvAuthorName.setText(tacGia.getFullname());

            // Load author avatar
            if (tacGia.getAvatar() != null && !tacGia.getAvatar().isEmpty()) {
                Glide.with(context)
                        .load(tacGia.getAvatar())
                        .placeholder(R.drawable.mastertref)
                        .error(R.drawable.mastertref)
                        .into(holder.imgAuthor);
            } else {
                holder.imgAuthor.setImageResource(R.drawable.mastertref);
            }
        } else {
            holder.tvAuthorName.setText("Unknown");
            holder.imgAuthor.setImageResource(R.drawable.mastertref);
        }

        // Load recipe image
        if (monAn.getHinhAnh() != null && !monAn.getHinhAnh().isEmpty()) {
            Glide.with(context)
                    .load(monAn.getHinhAnh())
                    .placeholder(R.drawable.mastertref)
                    .error(R.drawable.mastertref)
                    .into(holder.imgRecipe);
        } else {
            holder.imgRecipe.setImageResource(R.drawable.mastertref);
        }

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChiTietMonAnActivity.class);
            intent.putExtra("mon_an_id", monAn.getId());
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MonAnGoiYViewHolder extends RecyclerView.ViewHolder {
        ImageView imgRecipe, imgAuthor;
        TextView tvRecipeTitle, tvAuthorName;

        public MonAnGoiYViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRecipe = itemView.findViewById(R.id.imgRecipe);
            imgAuthor = itemView.findViewById(R.id.imgAuthor);
            tvRecipeTitle = itemView.findViewById(R.id.tvRecipeTitle);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
        }
    }
}
