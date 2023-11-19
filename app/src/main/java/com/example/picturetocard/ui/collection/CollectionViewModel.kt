package com.example.picturetocard.ui.collection

// CollectionViewModel.kt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CollectionViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "La collection est toujours vide."
    }

    val text: LiveData<String> = _text

    // Ajoutez des méthodes ou des champs pour gérer les données de votre collection
}

