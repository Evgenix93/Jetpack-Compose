package com.skillbox.homework308

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        Log.d("MyTag", "onCreate")
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().replace(R.id.container, MovieListFragment()).commit()
    }

    fun openDbMovieListFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.container, DbMovieListFragment()).addToBackStack(null).commit()
    }
}