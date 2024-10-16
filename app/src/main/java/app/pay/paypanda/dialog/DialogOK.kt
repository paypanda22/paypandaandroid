package app.pay.paypanda.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import app.pay.paypanda.R

import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

class DialogOK(private val context: Context) {
    fun showErrorDialog(
        context: Context,
        title: String,
        message: String,
        lottieResId: Int? = null,
        lottieWidth: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
        lottieHeight: Int = 200 // Default height
    ): Dialog {
        val builder = AlertDialog.Builder(context)

        // Create a LinearLayout to hold the dialog content
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
            gravity = Gravity.CENTER_HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            background = ContextCompat.getDrawable(context, R.drawable.rectanle_backgound)
        }

        // Add LottieAnimationView if provided
        lottieResId?.let {
            val lottieAnimationView = LottieAnimationView(context).apply {
                setAnimation(it)
                repeatCount = LottieDrawable.INFINITE
                playAnimation()
                layoutParams = LinearLayout.LayoutParams(
                    lottieWidth,
                    lottieHeight
                ).apply {
                    bottomMargin = 16
                }
            }
            layout.addView(lottieAnimationView)
        }

        // Add title
        val titleView = TextView(context).apply {
            text = title
            textSize = 20f
            setTypeface(null, android.graphics.Typeface.BOLD)
            gravity = Gravity.CENTER
            setTextColor(ContextCompat.getColor(context, R.color.green))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16
            }
        }
        layout.addView(titleView)

        // Add message
        val messageView = TextView(context).apply {
            text = message
            textSize = 15f
            gravity = Gravity.CENTER
            setTextColor(ContextCompat.getColor(context, R.color.black))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16
            }
        }
        layout.addView(messageView)

        builder.setView(layout)
        val dialog = builder.create()

        // Create and set custom buttons
        val button = Button(context).apply {
            text = "OK"
            setBackgroundColor(ContextCompat.getColor(context, R.color.blue_700)) // Set OK button background color
            setTextColor(ContextCompat.getColor(context, R.color.white)) // Set OK button text color to white
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
            setOnClickListener { dialog.dismiss() }
        }
        layout.addView(button)

        // Show the dialog
        dialog.show()

        return dialog // Return the dialog instance
    }


    fun showForceDialog(
        context: Context,
        title: String,
        message: String,
        lottieResId: Int? = null,
        lottieWidth: Int = ViewGroup.LayoutParams.MATCH_PARENT,
        lottieHeight: Int = ViewGroup.LayoutParams.MATCH_PARENT
    ): Dialog {
        val builder = AlertDialog.Builder(context)

        // Create a FrameLayout to hold the dialog content
        val layout = FrameLayout(context).apply {
            setPadding(0, 0, 0, 0)
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            background = ContextCompat.getDrawable(context, R.color.white)
        }

        // Add LottieAnimationView to cover the entire background
        lottieResId?.let {
            val lottieAnimationView = LottieAnimationView(context).apply {
                setAnimation(it)
                repeatCount = LottieDrawable.INFINITE
                playAnimation()
                layoutParams = FrameLayout.LayoutParams(
                    lottieWidth,
                    lottieHeight
                ).apply {
                    gravity = Gravity.CENTER
                }
            }
            layout.addView(lottieAnimationView)
        }

        // Create a LinearLayout to hold the content
        val contentLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
            gravity = Gravity.CENTER_HORIZONTAL
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
        }

        // Add title
        val titleView = TextView(context).apply {
            text = title
            textSize = 30f
            setTypeface(null, android.graphics.Typeface.BOLD)
            gravity = Gravity.CENTER
            setTextColor(ContextCompat.getColor(context, R.color.green))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16
            }
        }
        contentLayout.addView(titleView)

        // Add message
        val messageView = TextView(context).apply {
            text = message
            textSize = 15f
            gravity = Gravity.CENTER
            setTextColor(ContextCompat.getColor(context, R.color.black))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16
            }
        }
        contentLayout.addView(messageView)

        layout.addView(contentLayout)
        builder.setView(layout)
        val dialog = builder.create()

        // Prevent user from dismissing the dialog
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)

        // Show the dialog
        dialog.show()

        return dialog // Return the dialog instance
    }


}