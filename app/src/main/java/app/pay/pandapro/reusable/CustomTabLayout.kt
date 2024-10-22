package app.pay.pandapro.reusable

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import app.pay.pandapro.R
import com.google.android.material.tabs.TabLayout

class CustomTabLayout : TabLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun addTab(tab: Tab, position: Int, setSelected: Boolean) {
        super.addTab(tab, position, setSelected)
        val customView = LayoutInflater.from(context).inflate(R.layout.custom_tab_layout, null)
        val tabText = customView.findViewById<TextView>(R.id.tab_text)
        tabText.text = tab.text
        tabText.setTextColor(resources.getColor(if (setSelected) R.color.colorPrimary else R.color.white))
        tabText.textSize = resources.getDimension(if (setSelected) R.dimen._12ssp else R.dimen._12ssp) // Set text size based on the selected state
        tab.customView = customView
    }
}