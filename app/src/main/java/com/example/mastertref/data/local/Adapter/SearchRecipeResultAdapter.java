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
import com.example.mastertref.data.local.MonAnWithChiTiet;

import java.util.ArrayList;
import java.util.List;

public class SearchRecipeResultAdapter extends RecyclerView.Adapter<SearchRecipeResultAdapter.SearchViewHolder> {
    private Context context;
    private List<MonAnWithChiTiet> monAnList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MonAnWithChiTiet monAn);
    }

    public SearchRecipeResultAdapter(Context context) {
        this.context = context;
        this.monAnList = new ArrayList<>();
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
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Change the layout resource to match your project's actual layout file
        View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        MonAnWithChiTiet monAn = monAnList.get(position);
        holder.bind(monAn);
    }

    @Override
    public int getItemCount() {
        return monAnList.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgRecipe;
        private TextView tvRecipeName;
        private TextView tvRecipeTime, tvAuthor;


        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRecipe = itemView.findViewById(R.id.iv_recipe);
            tvRecipeName = itemView.findViewById(R.id.tv_recipe_title);
            tvRecipeTime = itemView.findViewById(R.id.tv_cooking_time);
            tvAuthor = itemView.findViewById(R.id.tv_source_name);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(monAnList.get(position));
                }
            });
        }

        public void bind(MonAnWithChiTiet monAn) {
            tvRecipeName.setText(monAn.getMonAn().getTenMonAn());
            tvRecipeTime.setText(monAn.getMonAn().getThoiGian());
            tvAuthor.setText(monAn.getNguoiDang().getFullname());

            // Load image using Glide
            if (monAn.getMonAn().getHinhAnh() != null && !monAn.getMonAn().getHinhAnh().isEmpty()) {
                Glide.with(context)
                        .load(monAn.getMonAn().getHinhAnh())
                        .placeholder(R.drawable.mastertref)
                        .error(R.drawable.mastertref)
                        .centerCrop()
                        .into(imgRecipe);
            } else {
                imgRecipe.setImageResource(R.drawable.mastertref);
            }
        }
    }
}
