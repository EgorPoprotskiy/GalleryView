package com.egorpoprotskiy.galleryview.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egorpoprotskiy.galleryview.domain.model.MediaItem
import com.egorpoprotskiy.galleryview.domain.repository.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//4. Создание Главного ViewModel.
class GalleryViewModel(private val mediaRepository: MediaRepository): ViewModel() {
    // 1. Приватный, изменяемый StateFlow, хранящий List<MediaItem>
    private val _mediaList = MutableStateFlow<List<MediaItem>>(emptyList())
    // 2. Публичный, неизменяемый StateFlow для UI.
    // UI будет наблюдать за этой переменной.
    val mediaList: StateFlow<List<MediaItem>> = _mediaList.asStateFlow()

    init {
        loadMedia()
    }

    fun loadMedia() {
        // 1. Используем viewModelScope для запуска корутины,
        //    которая автоматически отменится при уничтожении ViewModel.
        viewModelScope.launch {
            // 2. Вызываем suspend-функцию репозитория и сохраняем результат
            val resultList = mediaRepository.getAllMedia()
            // 3. Обновляем MutableStateFlow новым списком
            _mediaList.value = resultList
        }
    }
}