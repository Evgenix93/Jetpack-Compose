package com.project.aperture.utils

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun <T: Fragment> T.withArguments(action:Bundle.() -> Unit): T {
    return apply {
        val args = Bundle().apply (action)
        arguments = args
    }
}

fun Fragment.toast(@StringRes stringRes: Int) {
    Toast.makeText(requireContext(), stringRes, Toast.LENGTH_SHORT).show()
}

fun ViewGroup.inflate (@LayoutRes layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}

fun haveQ (): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}

fun SearchView.textChangedFlow(): Flow<String> {
    return callbackFlow {
        val onQueryTextListener = object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank().not())
                    sendBlocking(newText!!)
                return true
            }
        }
        setOnQueryTextListener(onQueryTextListener)
        awaitClose {
            setOnQueryTextListener(null)
        }
    }
}