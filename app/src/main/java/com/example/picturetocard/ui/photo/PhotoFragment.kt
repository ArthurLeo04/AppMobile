package com.example.picturetocard.ui.photo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.picturetocard.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class PhotoFragment : Fragment() {

    private var _photoPath: String? = null
    private lateinit var imgPhoto: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_photo, container, false)

        imgPhoto = root.findViewById(R.id.img_photo)

        val btnPhoto: Button = root.findViewById(R.id.btn_photo)
        btnPhoto.setOnClickListener {
            prendreUnePhoto()
        }

        return root
    }

    private fun prendreUnePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            val time = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val photoDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val photoFile = File.createTempFile("photo$time", ".jpg", photoDir)
            _photoPath = photoFile.absolutePath

            val photoUri = FileProvider.getUriForFile(
                requireActivity(),
                requireActivity().applicationContext.packageName + ".provider",
                photoFile
            )

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(intent, RETOUR_PRENDRE_PHOTO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RETOUR_PRENDRE_PHOTO && resultCode == Activity.RESULT_OK) {
            val image = BitmapFactory.decodeFile(_photoPath)
            imgPhoto.setImageBitmap(image)
        }
    }

    companion object {
        private const val RETOUR_PRENDRE_PHOTO = 1
    }
}
