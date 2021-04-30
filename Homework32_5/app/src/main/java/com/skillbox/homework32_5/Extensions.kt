package com.skillbox.homework32_5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate (@LayoutRes layout: Int): View {
    return LayoutInflater.from(this.context).inflate(layout, this, false)
}