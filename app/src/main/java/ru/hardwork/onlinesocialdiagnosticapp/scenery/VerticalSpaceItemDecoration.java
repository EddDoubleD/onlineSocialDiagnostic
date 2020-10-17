package ru.hardwork.onlinesocialdiagnosticapp.scenery;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {


    private static final int def_verticalSpaceHeight = 10;
    private static final int def_horizontalSpaceHeight = 70;

    private final int verticalSpaceHeight;
    private final int horizontalSpaceHeight;

    public VerticalSpaceItemDecoration() {
        this.verticalSpaceHeight = def_verticalSpaceHeight;
        this.horizontalSpaceHeight = def_horizontalSpaceHeight;
    }

    public VerticalSpaceItemDecoration(int verticalSpaceHeight, int horizontalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
        this.horizontalSpaceHeight = horizontalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.bottom = verticalSpaceHeight;
        outRect.left = horizontalSpaceHeight / 2;
        outRect.right = horizontalSpaceHeight / 2;
    }
}
