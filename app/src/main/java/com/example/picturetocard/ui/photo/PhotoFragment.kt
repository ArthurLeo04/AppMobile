package com.example.picturetocard.ui.photo

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.picturetocard.R
import com.example.picturetocard.databinding.FragmentPhotoBinding
import com.example.picturetocard.ui.game.CarteFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class PhotoFragment : Fragment() {

    private lateinit var binding: FragmentPhotoBinding
    private val CAMERA_REQUEST_CODE = 1
    private var _photoPath: String? = null
    private var carteFragment: CarteFragment? = null

    private val prendreUnePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val options = BitmapFactory.Options()
                options.inSampleSize = 2
                val image = BitmapFactory.decodeFile(_photoPath, options)
                Log.d("TAG", "bon on prend la photo")
                afficherCarte(image)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCamera.setOnClickListener {
            cameraCheckPermission()
        }

        binding.btnGallery.setOnClickListener {
            galleryCheckPermission()
        }
    }

    private fun cameraCheckPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            camera()
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                CAMERA_REQUEST_CODE
            )
        }
    }

    private fun galleryCheckPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            gallery()
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                CAMERA_REQUEST_CODE
            )
        }
    }

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        prendreUnePhotoGalleryLauncher.launch(intent)
    }

    private fun camera() {
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
        } else {
            Log.d("Print", "Pas d'appareil photo détecté ...")
        }
    }

    private fun afficherCarte(image: Bitmap) {
        val correctedImage = rotateImageIfRequired(image, _photoPath!!)
        _photoPath = null
        carteFragment = getCarteFragment(correctedImage)

        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.carte_photo, carteFragment!!)
        transaction.addToBackStack(null)
        transaction.setReorderingAllowed(true)
        transaction.commitAllowingStateLoss()
    }

    private val prendreUnePhotoGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageUri: Uri? = data?.data
                if (imageUri != null) {
                    val imageStream = requireContext().contentResolver.openInputStream(imageUri)
                    val image = BitmapFactory.decodeStream(imageStream)
                    Log.d("TAG", "bon on prend la photo")
                    afficherGalleryCarte(image, null)
                }
            }
        }

    private fun afficherGalleryCarte(image: Bitmap, imagePath: String?) {
        val correctedImage = if (imagePath != null) {
            rotateImageIfRequired(image, imagePath)
        } else {
            image
        }

        carteFragment = getCarteFragment(correctedImage)

        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.carte_photo, carteFragment!!)
        transaction.addToBackStack(null)
        transaction.setReorderingAllowed(true)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                camera()
            } else {
                showRationaleDialogForPermission()
            }
        }
    }

    private fun showRationaleDialogForPermission() {
        AlertDialog.Builder(requireContext())
            .setMessage("It looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton("GO TO SETTINGS") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", requireActivity().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}
