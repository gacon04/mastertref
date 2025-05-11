package com.example.mastertref.data.local.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mastertref.R;
import com.example.mastertref.data.local.LichsuTimkiemEntity;

import java.util.ArrayList;
import java.util.List;

public class LichSuTimKiemAdapter extends RecyclerView.Adapter<LichSuTimKiemAdapter.SearchViewHolder> {
    private List<LichsuTimkiemEntity> searchHistory = new ArrayList<>();
    private OnItemClickListener listener;
    private OnDeleteClickListener deleteListener;

    public interface OnItemClickListener {
        void onItemClick(String searchQuery);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    public void setData(List<LichsuTimkiemEntity> searchHistory) {
        this.searchHistory = searchHistory;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_search_item, parent, false);
        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        LichsuTimkiemEntity currentItem = searchHistory.get(position);
        holder.tvSearchQuery.setText(currentItem.getTuKhoa());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentItem.getTuKhoa());
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(currentItem.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchHistory.size();
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSearchQuery;
        private ImageView btnDelete;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSearchQuery = itemView.findViewById(R.id.tv_search_query);
            btnDelete = itemView.findViewById(R.id.btn_delete_search);
        }
    }
}
