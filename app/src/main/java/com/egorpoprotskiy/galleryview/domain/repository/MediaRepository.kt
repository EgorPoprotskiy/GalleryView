package com.egorpoprotskiy.galleryview.domain.repository

import com.egorpoprotskiy.galleryview.domain.model.MediaItem

//2. Интерфейс репозитория.
interface MediaRepository { //Это контракт о том, что нужно получить.
    suspend fun getAllMedia(): List<MediaItem>
}