package ru.hardwork.onlinesocialdiagnosticapp.holders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;
import ru.hardwork.onlinesocialdiagnosticapp.ui.activity.StartActivity;

import static ru.hardwork.onlinesocialdiagnosticapp.common.Common.categoryList;

public class DiagnosticViewHolder extends RecyclerView.ViewHolder {

    public int id;
    @BindView(R.id.item_psy_layout)
    public LinearLayout layout;
    @BindView(R.id.diagnosticName)
    public TextView diagnosticName;
    @BindView(R.id.txtTotalQuestion)
    public TextView totalQuestion;
    @BindView(R.id.txtTotalTime)
    public TextView totalTime;

    public DiagnosticViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @OnClick(R.id.item_psy_layout)
    public void onClick(View view) {
        DiagnosticTest diagnostic = Common.diagnosticTests.get(getAdapterPosition());
        Intent startDiagnostic = new Intent(view.getContext(), StartActivity.class);
        Bundle dataSend = new Bundle();
        int catId = (int) diagnostic.getCategoryId() - 1;
        String catName = categoryList.get(catId).getName();
        dataSend.putSerializable("DIAGNOSTIC", diagnostic);
        dataSend.putString("CAT_NAME", catName);
        startDiagnostic.putExtras(dataSend);
        view.getContext().startActivity(startDiagnostic);
    }
}
