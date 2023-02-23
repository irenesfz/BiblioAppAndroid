package com.proyecto.biblioapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64
import com.proyecto.biblioapp.R
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL


object ImagenUtilidad {

    fun convertirURLBitmap(url: String?, context: Context): Bitmap? {
        var bitmap: Bitmap?
        val inputStream: InputStream
        try {
            inputStream = URL(url).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            bitmap = convertirRecursoBitmap(R.drawable.no_image, context)
        }
        return bitmap
    }

    fun convertirStringBitmap(imagen: String?): Bitmap {
        val decodedString: ByteArray = Base64.decode(imagen, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    fun convertirRecursoBitmap(recurso: Int, context: Context): Bitmap {
        return BitmapFactory.decodeResource(context.resources, recurso)
    }

    fun ConvertirImagenString(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val byte_arr: ByteArray = stream.toByteArray()
        return Base64.encodeToString(byte_arr, Base64.DEFAULT)
    }
}
