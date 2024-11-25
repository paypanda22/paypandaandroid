package app.pay.retailers.adapters

import FullScreenImageDialogFragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import app.pay.retailers.R
import app.pay.retailers.helperclasses.MyGlide
import app.pay.retailers.retrofit.Constant

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
        imageView.setOnClickListener {
            val fragment = FullScreenImageDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("imageUrl", imageUrl) // Pass the image URL
                }
            }
            if (context is AppCompatActivity) {
                fragment.show((context as AppCompatActivity).supportFragmentManager, "FullScreenImageDialog")
            }
          //  fragment.show(supportFragmentManager, "FullScreenImageDialog")
        }
        return view
    }

}
