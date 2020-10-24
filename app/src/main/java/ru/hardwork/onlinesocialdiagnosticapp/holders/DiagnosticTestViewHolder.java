package ru.hardwork.onlinesocialdiagnosticapp.holders;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.listener.ItemClickListener;

public class DiagnosticTestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public int id;
    public LinearLayout layout;
    public TextView diagnosticName, totalQuestion, totalTime;
    // Слушатель клика
    private ItemClickListener itemClickListener;

    @SuppressLint("ResourceAsColor")
    public DiagnosticTestViewHolder(@NonNull View itemView) {
        super(itemView);

        layout = itemView.findViewById(R.id.item_psy_layout);
        layout.setBackgroundColor(R.color.background);

        diagnosticName = itemView.findViewById(R.id.diagnosticName);
        totalQuestion = itemView.findViewById(R.id.txtTotalQuestion);
        totalTime = itemView.findViewById(R.id.txtTotalTime);

        itemView.setOnClickListener(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public LinearLayout getLayout() {
        return layout;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
