package com.comleoneo.customcalendarviewwithevents;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = outRect.bottom = outRect.left = space;

        if (parent.getChildLayoutPosition(view) == -1) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }
}
