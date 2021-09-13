package com.project.aperture.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.aperture.R

class LogoutDialog: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.logout_dialog, container, false)
        view.findViewById<Button>(R.id.dialogYesButton).setOnClickListener {
          val profileFragment = parentFragment as ProfileFragment
          profileFragment.logout()
        }
        view.findViewById<Button>(R.id.dialogNoButton).setOnClickListener {
            dismiss()
        }
        return view
    }
}