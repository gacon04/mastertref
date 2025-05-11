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
import com.example.mastertref.data.local.MonAnWithChiTiet;
import com.example.mastertref.ui.ChiTietMonAnActivity;

import java.util.ArrayList;
import java.util.List;

public class DaXemGanDayAdapter extends RecyclerView.Adapter<DaXemGanDayAdapter.ViewHolder> {
    private Context context;
    private List<MonAnWithChiTiet> recentRecipes = new ArrayList<>();
    private OnItemClickListener listener;

    public DaXemGanDayAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mon_an_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MonAnWithChiTiet recipe = recentRecipes.get(position);
        
        holder.tvTenMonAn.setText(recipe.getMonAn().getTenMonAn());
        holder.tvMoTaMonAn.setText(recipe.getMonAn().getMoTa());
        
        // Load image using Glide
        if (recipe.getMonAn().getHinhAnh() != null && !recipe.getMonAn().getHinhAnh().isEmpty()) {
            Glide.with(context)
                    .load(recipe.getMonAn().getHinhAnh())
                    .placeholder(R.drawable.mastertref)
                    .error(R.drawable.mastertref)
                    .into(holder.imgMonAn);
        }
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(recipe.getMonAn().getId());
            } else {
                // Default behavior if no listener is set
                Intent intent = new Intent(context, ChiTietMonAnActivity.class);
                intent.putExtra("mon_an_id", recipe.getMonAn().getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recentRecipes.size();
    }

    public void setData(List<MonAnWithChiTiet> recipes) {
        this.recentRecipes = recipes;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int monAnId);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenMonAn, tvMoTaMonAn;
        ImageView imgMonAn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenMonAn = itemView.findViewById(R.id.tvTenMonAn);
            tvMoTaMonAn = itemView.findViewById(R.id.tvMoTaMonAn);
            imgMonAn = itemView.findViewById(R.id.imgMonAn);
        }
    }
}
