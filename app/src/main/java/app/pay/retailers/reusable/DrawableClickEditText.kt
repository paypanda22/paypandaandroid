package app.pay.retailers.reusable

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText

class DrawableClickEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    private var onDrawableEndClick: (() -> Unit)? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            compoundDrawables[2]?.let {
                val drawableEndX = width - paddingEnd - it.intrinsicWidth
                if (event.x >= drawableEndX) {
                    onDrawableEndClick?.invoke()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }
}
