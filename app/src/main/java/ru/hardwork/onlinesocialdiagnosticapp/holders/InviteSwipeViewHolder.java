package ru.hardwork.onlinesocialdiagnosticapp.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.rambler.libs.swipe_layout.SwipeLayout;

public class InviteSwipeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.swipeLayout)
    public SwipeLayout swipeLayout;
    @BindView(R.id.mainLayout)
    public LinearLayout mainLayout;
    @BindView(R.id.rightLayout)
    public LinearLayout rightLayout;
    @BindView(R.id.leftLayout)
    public LinearLayout leftLayout;
    @BindView(R.id.aliasText)
    public TextView aliasText;
    @BindView(R.id.diagnosticNameText)
    public TextView diagnosticNameText;
    @BindView(R.id.diagnosticDateText)
    public TextView diagnosticDateText;

    @BindView(R.id.deleteButton)
    public ImageButton deleteButton;
    @BindView(R.id.editButton)
    public ImageButton editButton;
    @BindView(R.id.sharedButton)
    public ImageButton sharedButton;


    public InviteSwipeViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        /**
         * default click listener
         */
        if (rightLayout != null) {
            rightLayout.setClickable(true);
            rightLayout.setOnClickListener(v -> swipeLayout.animateReset());
        }

        if (leftLayout != null) {
            leftLayout.setClickable(true);
            leftLayout.setOnClickListener(v -> swipeLayout.animateReset());
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
    }

}
