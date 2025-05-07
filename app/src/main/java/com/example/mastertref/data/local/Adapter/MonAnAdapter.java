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
import com.example.mastertref.data.local.NguyenLieuEntity;
import com.example.mastertref.ui.ChiTietMonAnActivity;

import java.util.ArrayList;
import java.util.List;

public class MonAnAdapter extends RecyclerView.Adapter<MonAnAdapter.MonAnViewHolder> {
    private final List<MonAnWithChiTiet> list;
    private final List<MonAnWithChiTiet> fullList; // Store the full list
    private Context context;
    private OnItemClickListener listener;
    private int itemLimit = -1; // -1 means no limit
    public interface OnItemClickListener {
        void onItemClick(MonAnWithChiTiet monAn);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public MonAnAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        this.fullList = new ArrayList<>();
    }

    public void setData(List<MonAnWithChiTiet> data) {
        fullList.clear();
        fullList.addAll(data);
        
        list.clear();
        if (itemLimit > 0 && data.size() > itemLimit) {
            list.addAll(data.subList(0, itemLimit));
        } else {
            list.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setItemLimit(int limit) {
        this.itemLimit = limit;
    }

    public boolean hasMoreItems() {
        return fullList.size() > list.size();
    }

    public void showAllItems() {
        list.clear();
        list.addAll(fullList);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MonAnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mon_an_card, parent, false);
        return new MonAnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonAnViewHolder holder, int position) {
        MonAnWithChiTiet monAnChiTiet = list.get(position);
        MonAnEntity monAn = monAnChiTiet.getMonAn();

        holder.tvTen.setText(monAn.getTenMonAn());

        // Tạo mô tả từ danh sách nguyên liệu
        List<NguyenLieuEntity> nguyenLieus = monAnChiTiet.getNguyenLieuList();
        StringBuilder moTa = new StringBuilder();
        for (int i = 0; i < nguyenLieus.size(); i++) {
            moTa.append(nguyenLieus.get(i).getTenNguyenLieu());
            if (i < nguyenLieus.size() - 1) moTa.append(", ");
        }
        holder.tvMoTa.setText(moTa.toString());

        Glide.with(context).load(monAn.getHinhAnh()).into(holder.ivAnh);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(monAnChiTiet);
            }

            Intent intent = new Intent(context, ChiTietMonAnActivity.class);
            intent.putExtra("mon_an_id", monAn.getId());
            context.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MonAnViewHolder extends RecyclerView.ViewHolder {
        TextView tvTen, tvMoTa;
        ImageView ivAnh;

        public MonAnViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tvTenMonAn);
            tvMoTa = itemView.findViewById(R.id.tvMoTaMonAn);
            ivAnh = itemView.findViewById(R.id.imgMonAn);
        }
    }
}
