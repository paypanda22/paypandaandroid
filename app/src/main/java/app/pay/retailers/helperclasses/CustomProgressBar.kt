package app.pay.retailers.helperclasses

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import app.pay.retailers.R


class CustomProgressBar {
    private var dialog: Dialog? = null
    fun showProgress(context: Context?) {
        dialog = Dialog(context!!)
        dialog!!.setContentView(R.layout.custom_dialog_progress)
        if (dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()
    }

    fun hideProgress() {
        if (dialog != null) {
            if (dialog!!.isShowing) dialog!!.dismiss()
        }
    }
}