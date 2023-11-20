package com.example.picturetocard.ui.photo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.picturetocard.R
import com.example.picturetocard.ui.game.CarteFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import android.util.Log


class PhotoFragment : Fragment() {

    private lateinit var btnPrendrePhoto: Button
    private var _photoPath: String? = null
    private var carteFragment: CarteFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_photo, container, false)

        btnPrendrePhoto = root.findViewById(R.id.btn_photo)

        btnPrendrePhoto.setOnClickListener {
            prendreUnePhoto()
        }

        return root
    }

    private val prendreUnePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val options = BitmapFactory.Options()
                options.inSampleSize = 2 // Redimensionne l'image par un facteur de 2 (ajustez selon vos besoins)
                val image = BitmapFactory.decodeFile(_photoPath, options)
                Log.d("TAG", "bon on prend la photo")
                afficherCarte(image)
            }
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
            prendreUnePhotoLauncher.launch(intent)
        }
        else {
            Log.d("Print","Pas d'appareil photo détecté ...")
        }
    }

    private fun afficherCarte(image: Bitmap) {
        // Correct the orientation of the image
        val correctedImage = rotateImageIfRequired(image, _photoPath!!)

        // Réinitialiser le chemin de la photo
        _photoPath = null

        // Create an instance of CarteFragment with the corrected image
        carteFragment = getCarteFragment(correctedImage)

        // Get the FragmentManager of your CarteFragment
        val fragmentManager = parentFragmentManager

        // Start a fragment transaction
        val transaction = fragmentManager.beginTransaction()

        // Replace the current fragment with the card fragment
        transaction.replace(R.id.carte_photo, carteFragment!!)

        // Ajouter la transaction à la pile de retour arrière
        transaction.addToBackStack(null)

        // Utiliser setReorderingAllowed(true) pour assurer une restauration atomique de la pile de retour arrière
        transaction.setReorderingAllowed(true)

        // Validate the transaction
        transaction.commitAllowingStateLoss()
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


    private fun getCarteFragment(image: Bitmap): CarteFragment {
        // retourne un nouveau fragment de carte avec l'image
        return CarteFragment.newInstance(1, false, image)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Détruire le fragment CarteFragment s'il existe
        carteFragment?.let {
            val fragmentManager = parentFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.remove(it)
            transaction.commit()
        }
    }

}


