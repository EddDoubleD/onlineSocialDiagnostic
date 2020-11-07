package ru.hardwork.onlinesocialdiagnosticapp.holders;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.listener.ItemClickListener;

public class UserResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public LinearLayout resultLine;
    public TextView diagnosticName, diagnosticDate;
    public CardView userCardView;
    // Слушатель клика
    private ItemClickListener itemClickListener;

    @SuppressLint("ResourceAsColor")
    public UserResultViewHolder(@NonNull View itemView) {
        super(itemView);

        resultLine = itemView.findViewById(R.id.resultLine);
        resultLine.setBackgroundColor(R.color.background);
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
