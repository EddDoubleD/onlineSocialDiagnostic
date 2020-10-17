package ru.hardwork.onlinesocialdiagnosticapp.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.listener.ItemClickListener;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView categoryName;
    public ImageView categoryImage; // optional
    // Слушатель клика
    private ItemClickListener itemClickListener;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        categoryName = itemView.findViewById(R.id.category_name);
        categoryImage = itemView.findViewById(R.id.category_image);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
