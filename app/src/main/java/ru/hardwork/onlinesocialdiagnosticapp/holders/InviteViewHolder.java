package ru.hardwork.onlinesocialdiagnosticapp.holders;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.hardwork.onlinesocialdiagnosticapp.R;

public class InviteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public LinearLayout groupLine, clickableLayout;
    public ImageButton share;
    public TextView aliasText, inviteDiagnostic;

    @SuppressLint("ResourceAsColor")
    public InviteViewHolder(@NonNull View itemView) {
        super(itemView);

        share = itemView.findViewById(R.id.shareBtn);
        aliasText = itemView.findViewById(R.id.aliasText);
        clickableLayout = itemView.findViewById(R.id.clickableLayout);
        inviteDiagnostic = itemView.findViewById(R.id.inviteDiagnostic);
        groupLine = itemView.findViewById(R.id.groupLine);
        groupLine.setBackgroundColor(R.color.background);
    }


    @Override
    public void onClick(View view) {

    }
}
