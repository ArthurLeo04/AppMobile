package com.example.picturetocard.ui.photo

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.palette.graphics.Palette
import com.example.picturetocard.R
import com.example.picturetocard.database.CardEntity
import com.example.picturetocard.databinding.FragmentPhotoBinding
import com.example.picturetocard.game.Card
import com.example.picturetocard.game.Colors
import com.example.picturetocard.game.Effets
import com.example.picturetocard.ui.game.CarteFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.*
import com.example.picturetocard.PictureToCard
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface ColorExtractionCallback {
    fun onColorsExtracted(color1: Colors, color2: Effets)
}

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
        extractColors(image, object : ColorExtractionCallback {
            override fun onColorsExtracted(color1: Colors, color2: Effets) {
                val correctedImage = rotateImageIfRequired(image, _photoPath!!)
                Log.d("TAG", "Couleur dominante l143 : $color1")
                Log.d("TAG", "Couleur secondaire : $color2")
                _photoPath = null
                carteFragment = if (color1.ordinal == color2.ordinal){
                    getCarteFragment(correctedImage, color1, Effets.PLUS_UN)
                } else {
                    getCarteFragment(correctedImage, color1, color2)
                }
                val fragmentManager = parentFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.carte_photo, carteFragment!!)
                transaction.addToBackStack(null)
                transaction.setReorderingAllowed(true)
                transaction.commitAllowingStateLoss()

                showBottomAlertDialog(parentFragmentManager, carteFragment!!, image,null)

            }
        })
    }

    private val prendreUnePhotoGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageUri: Uri? = data?.data
                if (imageUri != null) {
                    // Obtenir le chemin du fichier à partir de l'URI
                    val imagePath = getImagePath(imageUri)

                    // Utilisez le chemin du fichier ici
                    Log.d("TAG", "Chemin de l'image : $imagePath")

                    // Utilisez l'image si nécessaire
                    val imageStream = requireContext().contentResolver.openInputStream(imageUri)
                    val image = BitmapFactory.decodeStream(imageStream)
                    afficherGalleryCarte(image, imagePath)
                }
            }
        }

    private fun getImagePath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = requireContext().contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            val columnIndex: Int = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            return it.getString(columnIndex)
        }

        return null
    }


    private fun afficherGalleryCarte(image: Bitmap, imagePath: String?){
        extractColors(image, object : ColorExtractionCallback {
            override fun onColorsExtracted(color1: Colors, color2: Effets) {
                val correctedImage = if (imagePath != null) {
                    rotateImageIfRequired(image, imagePath)
                } else {
                    image
                }
                Log.d("TAG", "Couleur dominante l143 : $color1")
                Log.d("TAG", "Couleur secondaire : $color2")
                Log.d("TAG", "imagePath : $imagePath")
                carteFragment = if (color1.ordinal == color2.ordinal){
                    getCarteFragment(correctedImage, color1, Effets.PLUS_UN)
                } else {
                    getCarteFragment(correctedImage, color1, color2)
                }
                val fragmentManager = parentFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.carte_photo, carteFragment!!)
                transaction.addToBackStack(null)
                transaction.setReorderingAllowed(true)
                transaction.commitAllowingStateLoss()

                showBottomAlertDialog(parentFragmentManager, carteFragment!!,null,imagePath)

            }
        })
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


    private fun getCarteFragment(image: Bitmap, color1: Colors, color2: Effets): CarteFragment {
        // retourne un nouveau fragment de carte avec l'image
        val card = Card(color1, color2, image)
        return CarteFragment(card)
    }

    @Deprecated("Deprecated in Java")
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

    fun getColorName(rgb: Int): Colors {
        val red = Color.red(rgb)
        val green = Color.green(rgb)
        val blue = Color.blue(rgb)

        // Associer les plages de valeurs RGB aux couleurs
        if (red > 200 && green < 100 && blue < 100) {
            return Colors.FEU
        } else if (red < 100 && green > 200 && blue > 200) {
            return Colors.EAU
        } else if (red < 100 && green > 200 && blue < 100) {
            return Colors.NATURE
        } else if (red > 200 && green > 200 && blue < 100) {
            return Colors.FOUDRE
        } else if (red > 200 && green > 200 && blue > 200) {
            return Colors.GLACE
        } else if (red > 100 && green > 50 && blue < 50) {
            return Colors.ROCHE
        } else if (red < 150 && green < 150 && blue < 150) {
            return Colors.METAL
        } else if (red > 150 && green < 100 && blue > 150) {
            return Colors.AIR
        } else {
            return Colors.EAU // Aucune des couleurs spécifiées
        }
    }

    fun getEffetName(rgb: Int): Effets {
        val red = Color.red(rgb)
        val green = Color.green(rgb)
        val blue = Color.blue(rgb)

        // Associer les plages de valeurs RGB aux couleurs
        if (red > 200 && green < 100 && blue < 100) {
            return Effets.FEU
        } else if (red < 100 && green > 200 && blue > 200) {
            return Effets.EAU
        } else if (red < 100 && green > 200 && blue < 100) {
            return Effets.NATURE
        } else if (red > 200 && green > 200 && blue < 100) {
            return Effets.FOUDRE
        } else if (red > 200 && green > 200 && blue > 200) {
            return Effets.GLACE
        } else if (red > 100 && green > 50 && blue < 50) {
            return Effets.ROCHE
        } else if (red < 150 && green < 150 && blue < 150) {
            return Effets.METAL
        } else if (red > 150 && green < 100 && blue > 150) {
            return Effets.AIR
        } else {
            return Effets.EAU // Aucune des couleurs spécifiées
        }
    }

    private fun extractColors(bitmap: Bitmap?, callback: ColorExtractionCallback) {
        var color1 = Colors.FEU
        var color2 = Effets.EAU
        Palette.from(bitmap!!).generate { palette ->
            // Extraction de la couleur dominante
            val dominantSwatch = palette?.dominantSwatch
            val secondDominantSwatch = palette?.swatches?.get(1)

            if (dominantSwatch != null && secondDominantSwatch != null) {
                val dominantColor = dominantSwatch.rgb
                val secondDominantColor = secondDominantSwatch.rgb

                color1 = getColorName(dominantColor)
                color2 = getEffetName(secondDominantColor)

                Log.d("TAG", "Couleur dominante : $color1")
                Log.d("TAG", "Couleur secondaire : $color2")

                // Appel du callback avec les couleurs extraites
                callback.onColorsExtracted(color1, color2)
            }
        }
        Log.d("TAG", "Couleur dominante l272 : $color1, Couleur secondaire : $color2")
    }

    fun showBottomAlertDialog(fragmentManager: FragmentManager, carteFragment: CarteFragment, image: Bitmap?,imagePath: String?) {
        val builder = AlertDialog.Builder(requireContext(), R.style.BottomAlertDialogTheme)

        builder.setMessage("Voulez-vous ajouter cette carte à la base de données?")
            .setPositiveButton("Oui") { _, _ ->
                GlobalScope.launch {
                    addToDatabase(carteFragment, image, imagePath)
                }
                clearCardFromFrameLayout(carteFragment)
            }
            .setNegativeButton("Non") { dialog, _ ->
                clearCardFromFrameLayout(carteFragment)
                dialog.dismiss()
            }

        val dialog = builder.create()

        // Ajustez la gravité pour afficher la boîte de dialogue en bas
        val window = dialog.window
        val layoutParams = window?.attributes
        layoutParams?.gravity = Gravity.BOTTOM
        window?.attributes = layoutParams

        dialog.setCanceledOnTouchOutside(false)

        dialog.show()
    }






    suspend fun addToDatabase(carteFragment: CarteFragment, image: Bitmap?,imagePath: String?) {
        val card = carteFragment.getCard()

        // Save the image to the device
        val savedImageUri = if (image != null) {
            saveImageToGallery(image)
        } else {
            imagePath ?: ""
        }

        // Récupérer l'instance de l'application
        val application: PictureToCard = requireActivity().application as PictureToCard

        // Maintenant, vous pouvez utiliser l'instance de l'application comme vous le souhaitez
        // Par exemple, pour accéder à une instance de CardDao si vous avez configuré votre application avec une base de données Room
        val cardDao = application.database.dao()

        // Ajout de la carte à la base de données avec le chemin de l'image sauvegardée
        cardDao.insertCard(
            CardEntity(
                color = card.color.ordinal,
                effet = card.effet.ordinal,
                imagePath = savedImageUri
            )
        )

        val cards = cardDao.getAllEntities()
        Log.d("TAG", "Cartes dans la BD : ${cards.size}")
        Log.d("TAG", "Carte ajoutée : ${card.color}, ${card.effet}, ${savedImageUri.toString()}")
    }

    private fun saveImageToGallery(image: Bitmap): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_$timeStamp.jpg"

        // Use the MediaStore to insert the image into the device's media store
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val resolver = requireContext().contentResolver
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        // Use an OutputStream to write the bitmap data to the content resolver
        val outputStream = resolver.openOutputStream(imageUri!!)
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream?.close()

        // Retrieve the file path from the URI
        val filePath = getImagePath(imageUri)

        return filePath ?: ""
    }


    private fun clearCardFromFrameLayout(carteFragment: CarteFragment) {
        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.remove(carteFragment)
        transaction.commitAllowingStateLoss()
    }


}