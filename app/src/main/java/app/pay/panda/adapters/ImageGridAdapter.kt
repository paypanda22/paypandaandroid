package app.pay.panda.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import app.pay.panda.R
import app.pay.panda.helperclasses.MyGlide
import app.pay.panda.retrofit.Constant

class ImageGridAdapter(
    private val context: Context,
    private val imageUrls: List<String>
) : BaseAdapter() {

    override fun getCount(): Int = imageUrls.size

    override fun getItem(position: Int): Any = imageUrls[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_item_image, parent, false)
        val imageView: ImageView = view.findViewById(R.id.imageView)

        // Load image into ImageView
        val imageUrl = imageUrls[position]
        MyGlide.with(context, Uri.parse(Constant.PIMAGE_URL + imageUrl), imageView)

        return view
    }
}
