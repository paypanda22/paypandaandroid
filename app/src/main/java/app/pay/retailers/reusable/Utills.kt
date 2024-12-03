package app.pay.retailers.reusable

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

object Utills {
    fun EditText.setOnTextChangedListener(onTextChanged: (CharSequence?, Int, Int, Int) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged(s, start, before, count)
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }
        })
    }
}