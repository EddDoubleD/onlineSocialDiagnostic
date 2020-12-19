package ru.hardwork.onlinesocialdiagnosticapp.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.rambler.libs.swipe_layout.SwipeLayout;

public class InviteSwipeViewHolder extends RecyclerView.ViewHolder {

    public final SwipeLayout swipeLayout;
    public final LinearLayout mainLayout, rightLayout, leftLayout;

    public final TextView aliasText, diagnosticNameText, diagnosticDateText;

    public final ImageButton deleteButton, editButton, sharedButton;

    public InviteSwipeViewHolder(@NonNull View itemView) {
        super(itemView);
        // swipe layout magic start here
        swipeLayout = itemView.findViewById(R.id.swipeLayout);
        rightLayout = itemView.findViewById(R.id.rightLayout);
        /**
         * default click listener
         */
        View.OnClickListener onClick = v -> swipeLayout.animateReset();
        if (rightLayout != null) {
            rightLayout.setClickable(true);
            rightLayout.setOnClickListener(onClick);
        }

        leftLayout = itemView.findViewById(R.id.leftLayout);
        if (leftLayout != null) {
            leftLayout.setClickable(true);
            leftLayout.setOnClickListener(onClick);
        }

        swipeLayout.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
            @Override
            public void onBeginSwipe(SwipeLayout swipeLayout, boolean moveToRight) {
            }

            @Override
            public void onSwipeClampReached(SwipeLayout swipeLayout, boolean moveToRight) {
            }

            @Override
            public void onLeftStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
            }

            @Override
            public void onRightStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
            }
        });


        mainLayout = itemView.findViewById(R.id.mainLayout);

        aliasText = itemView.findViewById(R.id.aliasText);
        diagnosticNameText = itemView.findViewById(R.id.diagnosticNameText);
        diagnosticDateText = itemView.findViewById(R.id.diagnosticDateText);

        sharedButton = itemView.findViewById(R.id.sharedButton);

        deleteButton = itemView.findViewById(R.id.deleteButton);
        editButton = itemView.findViewById(R.id.editButton);
    }

}
