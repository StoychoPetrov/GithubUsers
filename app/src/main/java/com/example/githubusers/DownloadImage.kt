package com.example.githubusers

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class DownloadImage(private var context: Context, private var defaultImageRes: Int, private var errorImageRes: Int, private var isCircle: Boolean, private var imageView: ImageView) {

    fun downloadImageFromUrl(url: String) {
        // Download image from url and set in imageview
        Picasso.get()
            .load(url)                                   // set url where the image will be downloaded
            .placeholder(defaultImageRes)               // set default image while the current image is downloading
            .error(errorImageRes)                       // set image when download is failed
            .fit()                                     // the image will fit the target (imageView)
            .into(imageView, object: Callback {
                override fun onSuccess() {
                    if(isCircle)
                        setRoundDrawable()
                }

                override fun onError(e: Exception?) {
                }
            })
    }

    private fun setRoundDrawable(){
        // Create drawable from downloaded image
        val bitmap                  = (imageView.drawable as BitmapDrawable).bitmap;
        val circleBitmapDrawable    = RoundedBitmapDrawableFactory.create(context.resources, bitmap);

        circleBitmapDrawable.isCircular = true;         // set shape of drawable to be circle

        imageView.setImageDrawable(circleBitmapDrawable)
    }
}