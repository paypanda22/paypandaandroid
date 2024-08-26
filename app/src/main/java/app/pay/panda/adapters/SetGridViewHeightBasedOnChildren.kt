import android.view.ViewGroup
import android.widget.GridView
import android.view.View
import android.widget.ListAdapter

// Function to set the height of GridView dynamically
fun SetGridViewHeightBasedOnChildren(gridView: GridView, columns: Int) {
    val adapter = gridView.adapter ?: return

    var totalHeight = 0
    val items = adapter.count
    val rows: Int

    val listItem = adapter.getView(0, null, gridView)
    listItem.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    totalHeight = listItem.measuredHeight

    rows = if (items > columns) {
        items / columns + if (items % columns > 0) 1 else 0
    } else {
        1
    }

    totalHeight *= rows

    val params = gridView.layoutParams
    params.height = totalHeight + gridView.verticalSpacing * (rows - 1)
    gridView.layoutParams = params
    gridView.requestLayout()
}
