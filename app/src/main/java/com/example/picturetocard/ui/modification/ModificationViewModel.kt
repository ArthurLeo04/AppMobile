package com.example.picturetocard.ui.modification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ModificationViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is modification Fragment"
    }
    val text: LiveData<String> = _text
}
