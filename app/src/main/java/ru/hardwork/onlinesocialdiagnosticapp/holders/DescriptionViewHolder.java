package ru.hardwork.onlinesocialdiagnosticapp.holders;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.listener.ItemClickListener;

public class DescriptionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView descriptionName;
    public ProgressBar descriptionProgress;

    // Слушатель клика
    private ItemClickListener itemClickListener;

    public DescriptionViewHolder(@NonNull View itemView) {
        super(itemView);

        descriptionName = itemView.findViewById(R.id.descriptionName);
        descriptionProgress = itemView.findViewById(R.id.descriptionProgress);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
