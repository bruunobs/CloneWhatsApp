package com.app.clonewhatsapp.adapter

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.io.*
import java.lang.Exception

class ArmazenarImagem {

    companion object {
        fun saveToInternalStorage(context: Context, bitmapImage: Bitmap, imageFileName: String): String {
            context.openFileOutput(imageFileName, Context.MODE_PRIVATE).use { fos ->
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 25, fos)
            }
            return context.filesDir.absolutePath
        }

        fun getImageFromInternalStorage(context: Context, imageFileName: String): Bitmap? {
            val directory = context.filesDir
            val file = File(directory, imageFileName)
            return BitmapFactory.decodeStream(FileInputStream(file))
        }

        fun deleteImageFromInternalStorage(context: Context, imageFileName: String): Boolean {
            val dir = context.filesDir
            val file = File(dir, imageFileName)
            return file.delete()
        }
    }



}