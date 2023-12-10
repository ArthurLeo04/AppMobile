package com.example.picturetocard.game

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Parcelable
import android.os.Parcel
import android.util.Log
import java.io.File

class Card(
    val color: Colors,
    val effet: Effets,
    var imageBitmap: Bitmap? = null,
    val id : Int = 0
) {
    fun setImageBitmap(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            val bitmap = BitmapFactory.decodeFile(file.absolutePath, options)

            // Rotate the image if required
            //bitmap = rotateImageIfRequired(bitmap, file.absolutePath)

            imageBitmap = resizeBitmap(bitmap, 500f)
        }
    }

    fun resizeBitmap(bitmap: Bitmap, targetResolution: Float): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height

        val originalResolution = originalHeight + originalWidth
        val facteurReduction = targetResolution / originalResolution

        Log.d("Print", "Original : $originalResolution et $facteurReduction")

        val matrix = Matrix()
        matrix.postScale(facteurReduction, facteurReduction)

        return Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, false)
    }

    // Function to rotate the image if required based on its orientation
    private fun rotateImageIfRequired(img: Bitmap, path: String): Bitmap {
        val ei = ExifInterface(path)
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
            else -> img
        }
    }

    // Function to rotate the image by a specified angle
    private fun rotateImage(img: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        return Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    }
}
