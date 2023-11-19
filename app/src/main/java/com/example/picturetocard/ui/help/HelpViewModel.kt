package com.example.picturetocard.ui.help

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HelpViewModel() : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Veuillez trouver ici la grille récapitulative des forces et faiblesses de chaque éléments"
    }
    val text: LiveData<String> = _text

}