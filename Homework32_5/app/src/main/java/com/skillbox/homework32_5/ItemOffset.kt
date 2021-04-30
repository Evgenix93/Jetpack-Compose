package com.skillbox.homework32_5

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemOffset: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val offset = 15
        with(outRect){
            left = offset
            right = offset
            top = offset
            bottom = offset
        }
    }
}