package app.pay.panda

import android.os.Bundle

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import app.pay.panda.helperclasses.ActivityExtensions

abstract class BaseActivity2<VB : ViewBinding> : AppCompatActivity(), View.OnClickListener {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    // This method is for subclasses to provide their view binding
    abstract fun getViewBinding(): VB

    // This method can be used if a subclass prefers to use a layout resource ID
    protected open fun setLayout(): Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Apply fullscreen To All Activities
        ActivityExtensions.fullscreen(this)
        _binding = getViewBinding()

        val layoutResId = setLayout()

        if (_binding != null) {
            setContentView(_binding!!.root)
        } else if (layoutResId != null) {
            setContentView(layoutResId)
        } else {
            throw IllegalStateException("Must provide a layout resource ID or a view binding")
        }

        try {
            init(savedInstanceState)
            addListeners()
            setData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    protected abstract fun init(savedInstanceState: Bundle?)

    protected abstract fun addListeners()

    protected abstract fun setData()

    @Deprecated("Deprecated app Java")
    override fun onBackPressed() {
        if (!handleBackPress()) {
            super.onBackPressed()
        }
    }

    abstract fun handleBackPress(): Boolean

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // To avoid memory leaks
    }


}



