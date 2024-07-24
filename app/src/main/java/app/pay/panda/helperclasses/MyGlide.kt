package app.pay.panda.helperclasses

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import app.pay.panda.R

import com.bumptech.glide.Glide

class MyGlide {
    companion object{
        fun with(context: Context?, url: Uri?, imageView: ImageView?) {
            if (imageView != null) {
                Glide.with(context!!)
                    .load(url)
                    .placeholder(R.drawable.no_photos)
                    .error(R.drawable.no_photos)
                    .into(imageView)
            }
        }
    }

}