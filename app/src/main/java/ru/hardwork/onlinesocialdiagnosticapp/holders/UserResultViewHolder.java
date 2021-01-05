package ru.hardwork.onlinesocialdiagnosticapp.holders;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hardwork.onlinesocialdiagnosticapp.R;

public class UserResultViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.resultLine)
    public LinearLayout resultLine;
    @BindView(R.id.diagnostic_name)
    public TextView diagnosticName;
    @BindView(R.id.diagnostic_date)
    public TextView diagnosticDate;
    @BindView(R.id.userCardView)
    public CardView userCardView;

    @SuppressLint("ResourceAsColor")
    public UserResultViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
