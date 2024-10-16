package app.pay.paypanda.reusable

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatEditText
import app.pay.paypanda.R


class AnimatedHintEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    private var isHintAnimatedUp = false

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused && !isHintAnimatedUp) {
            animateHintUp()
        } else if (!focused && isHintAnimatedUp && text.isNullOrEmpty()) {
            animateHintDown()
        }
    }

    private fun animateHintUp() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.hint_up)
        startAnimation(anim)
        isHintAnimatedUp = true
    }

    private fun animateHintDown() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.hint_down)
        startAnimation(anim)
        isHintAnimatedUp = false
    }
}
