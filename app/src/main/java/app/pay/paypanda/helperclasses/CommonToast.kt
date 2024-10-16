package com.example.yourapp

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import app.pay.paypanda.R


class CommonToast {

    companion object {
        // Function to display a custom Toast with a message and an optional icon
        fun show(context: Context, message: String, iconResId: Int? = null) {
            // Inflate the custom layout
            val inflater = LayoutInflater.from(context)
            val layout = inflater.inflate(R.layout.toast_layout, null)

            // Set the text for the Toast message
            val toastMessage = layout.findViewById<TextView>(R.id.toast_message)
            toastMessage.text = message

            // Set the icon if provided, otherwise hide the ImageView
            val toastIcon = layout.findViewById<ImageView>(R.id.toast_icon)
            if (iconResId != null) {
                toastIcon.setImageResource(iconResId)
                toastIcon.visibility = ImageView.VISIBLE
            } else {
                toastIcon.visibility = ImageView.GONE
            }

            // Create and show the custom Toast
            val toast = Toast(context)
            toast.duration = Toast.LENGTH_SHORT
            toast.view = layout
            toast.show()
        }
    }
}
