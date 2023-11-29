package com.srhan.easymarkt.ui.fragments.dialog

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.srhan.easymarkt.R

fun Fragment.setupBottomSheetDialog(
    onSendClick: (String) -> (Unit)
) {

    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)

    dialog.apply {
        setContentView(view)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        show()
    }

    val etEmail = view.findViewById<EditText>(R.id.etEmail)
    val buttonSend = view.findViewById<Button>(R.id.btn_send)
    val buttonCancel = view.findViewById<Button>(R.id.btn_cancel)

    buttonSend.setOnClickListener {

        onSendClick(etEmail.text.toString().trim())
        dialog.dismiss()
    }

    buttonCancel.setOnClickListener {
        dialog.dismiss()
    }


}