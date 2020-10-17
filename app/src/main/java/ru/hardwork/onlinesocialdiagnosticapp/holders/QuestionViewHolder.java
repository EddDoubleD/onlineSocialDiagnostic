package ru.hardwork.onlinesocialdiagnosticapp.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.listener.ItemClickListener;

public class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView questionText;

    // Слушатель клика
    private ItemClickListener itemClickListener;

    public QuestionViewHolder(@NonNull View itemView) {
        super(itemView);
        questionText = itemView.findViewById(R.id.question_text);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
