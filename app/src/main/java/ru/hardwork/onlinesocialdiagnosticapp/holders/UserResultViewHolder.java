package ru.hardwork.onlinesocialdiagnosticapp.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.listener.ItemClickListener;

public class UserResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView diagnosticName, diagnosticDate;
    public CardView userCardView;
    // Слушатель клика
    private ItemClickListener itemClickListener;

    public UserResultViewHolder(@NonNull View itemView) {
        super(itemView);

        userCardView = itemView.findViewById(R.id.userCardView);
        diagnosticName = itemView.findViewById(R.id.diagnostic_name);
        diagnosticDate = itemView.findViewById(R.id.diagnostic_date);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
