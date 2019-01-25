package com.example.githubusers.globalClasses

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

class DownloadImage(private var context:            Context,
                    private var defaultImageRes:    Int,
                    private var errorImageRes:      Int,
                    private var isCircle:           Boolean,
                    private var imageView:          ImageView) {

    fun downloadImageFromUrl(url: String) {

        val fileName = url.substring(url.lastIndexOf("/") + 1)  // image file name

        // check if image is stored in internal storage
        // and if the image is in internal storage, will get it firectly from there and set to imageView
        val bitmap = getImageFromCache(fileName)

        if(bitmap != null){
            setRoundDrawable(bitmap)
            return
        }

        // Download image from url and set in imageview
        Picasso.get()
            .load(url)                                   // set url where the image will be downloaded
            .placeholder(defaultImageRes)               // set default image while the current image is downloading
            .error(errorImageRes)                       // set image when download is failed
            .fit()                                      // the image will fit the target (imageView)
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    val bitmap = (imageView.drawable as BitmapDrawable).bitmap
                    saveImageToCache(bitmap, fileName)
                    if (isCircle)
                        setRoundDrawable(bitmap)
                }

                override fun onError(e: Exception?) {
                }
            })
    }

    // Create circle drawable and set to ImageView
    private fun setRoundDrawable(bitmap: Bitmap){
        // Create drawable from downloaded image
        val circleBitmapDrawable        = RoundedBitmapDrawableFactory.create(context.resources, bitmap);

        circleBitmapDrawable.isCircular = true;         // set shape of drawable to be circle

        imageView.setImageDrawable(circleBitmapDrawable)
    }

    // Store image in internal storage
    private fun saveImageToCache(bitmap: Bitmap, fileName: String) {
        try {
            val file = File.createTempFile(fileName, ".png", context.cacheDir)      // create new file with image's name
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Get image from internal storage by file's name
    private fun getImageFromCache(fileName: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val cacheDir = context.cacheDir
            val files = cacheDir.listFiles()
            var image: File? = null

            for (file in files) {
                if (file.name.startsWith(fileName)) {
                    image = file
                    break
                }
            }

            if (image != null) {
                bitmap = BitmapFactory.decodeFile(image.absolutePath)
            }
        } catch (e: Exception) {
        }

        return bitmap
    }
}