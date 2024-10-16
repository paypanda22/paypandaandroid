package app.pay.paypanda.helperclasses

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import app.pay.paypanda.R

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