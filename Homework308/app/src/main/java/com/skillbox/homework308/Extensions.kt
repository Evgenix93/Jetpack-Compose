package com.skillbox.homework308

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.LayoutRes
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun EditText.textChangedFlow(): Flow<String> {
    return callbackFlow{
        val textWatcher = object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendBlocking(s?.toString().orEmpty())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        addTextChangedListener(textWatcher)
        awaitClose{
            removeTextChangedListener(textWatcher)
        }
    }
}

fun RadioGroup.checkChangeFlow(): Flow<MovieType>{
    return callbackFlow {
        setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.movieRadioButton -> sendBlocking(MovieType.MOVIE)
                R.id.seriesRadioButton -> sendBlocking(MovieType.SERIES)
                R.id.episodeRadioButton -> sendBlocking(MovieType.EPISODE)
            }
        }
        awaitClose {
          setOnCheckedChangeListener(null)
        }
    }
}

fun ViewGroup.inflate (@LayoutRes layout: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layout, this, attachToRoot)
}