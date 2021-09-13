package com.project.aperture.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PhotoListDecoration: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val offset = 15
        with(outRect) {
            top = offset
            right = offset
            bottom = offset
            left = offset
        }
    }
}