package ru.hardwork.onlinesocialdiagnosticapp.holders;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.listener.ItemClickListener;

public class DescriptionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public LinearLayout descriptionLayout;
    public TextView descriptionName, descriptionCount;
    public ProgressBar descriptionProgress;

    // Слушатель клика
    private ItemClickListener itemClickListener;

    @SuppressLint("ResourceAsColor")
    public DescriptionViewHolder(@NonNull View itemView) {
        super(itemView);

        descriptionLayout = itemView.findViewById(R.id.description_layout);
        descriptionName = itemView.findViewById(R.id.descriptionName);
        descriptionCount = itemView.findViewById(R.id.descriptionCount);
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
