package com.davidbojkovski.recipes.cache_aware_image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CacheAwareImageViewModel @Inject constructor(private val repository: ImageRepository) :
    ViewModel() {
    private val _imageState = MutableStateFlow<Bitmap?>(null)
    val imageState: StateFlow<Bitmap?> = _imageState

    fun loadImage(url: String) {
        viewModelScope.launch {
            try {
                val imageByteArray = repository.getImage(url)
                imageByteArray?.let {
                    val bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
                    _imageState.value = bmp
                }

            } catch (e: Exception) {
                Log.d("CacheAwareImageViewModel", "Exception: ${e.localizedMessage}")
            }
        }
    }
}