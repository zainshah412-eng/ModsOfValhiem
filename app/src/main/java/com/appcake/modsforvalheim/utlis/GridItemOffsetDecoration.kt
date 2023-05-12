package com.appcake.modsforvalheim.utlis

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridItemOffsetDecoration(private val spanCount: Int, private var mItemOffset: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        if (position < spanCount) {
            if (position % 2 == 0) { // left grid
                outRect.set(0, mItemOffset, mItemOffset / 2, mItemOffset / 2)
            } else { // right grid
                outRect.set(mItemOffset / 2, mItemOffset, 0, mItemOffset / 2)
            }

        } else if (position % 2 == 0) { // left grid
            outRect.set(0, mItemOffset / 2, mItemOffset, mItemOffset / 2)

        } else if (position % 2 == 1) { // right grid
            outRect.set(mItemOffset / 2, mItemOffset / 2, 0, mItemOffset / 2)

        } else {
            if (position % 2 == 0) { // left grid
                outRect.set(0, mItemOffset / 2, mItemOffset, mItemOffset)
            } else { // right grid
                outRect.set(mItemOffset / 2, mItemOffset / 2, 0, mItemOffset)
            }
        }
    }
}