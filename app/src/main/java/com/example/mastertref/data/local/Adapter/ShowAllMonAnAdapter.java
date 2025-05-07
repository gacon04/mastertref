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
import com.example.mastertref.ui.ChiTietMonAnActivity;

import java.util.ArrayList;
import java.util.List;

public class ShowAllMonAnAdapter extends RecyclerView.Adapter<ShowAllMonAnAdapter.MonAnViewHolder> {
    private final List<MonAnWithChiTiet> list;
    private final List<MonAnWithChiTiet> fullList;
    private Context context;

    public ShowAllMonAnAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        this.fullList = new ArrayList<>();
    }

    public void setData(List<MonAnWithChiTiet> data) {
        fullList.clear();
        fullList.addAll(data);
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    public void filterData(String query) {
        list.clear();
        if (query.isEmpty()) {
            list.addAll(fullList);
        } else {
            for (MonAnWithChiTiet item : fullList) {
                if (item.getMonAn().getTenMonAn().toLowerCase().contains(query.toLowerCase())) {
                    list.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MonAnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_showallmon, parent, false);
        return new MonAnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonAnViewHolder holder, int position) {
        MonAnWithChiTiet monAnChiTiet = list.get(position);
        MonAnEntity monAn = monAnChiTiet.getMonAn();

        holder.tvRecipeTitle.setText(monAn.getTenMonAn());
        holder.tvTime.setText(monAn.getThoiGian());

        Glide.with(context)
            .load(monAn.getHinhAnh())
            .placeholder(R.drawable.mastertref)
            .error(R.drawable.mastertref)
            .into(holder.imgRecipe);

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

    static class MonAnViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeTitle, tvTime;
        ImageView imgRecipe;

        public MonAnViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipeTitle = itemView.findViewById(R.id.tvRecipeTitle);
            tvTime = itemView.findViewById(R.id.tvAuthorName);
            imgRecipe = itemView.findViewById(R.id.imgRecipe);
        }
    }
}
