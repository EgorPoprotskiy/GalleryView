package com.egorpoprotskiy.galleryview.domain.repository

import com.egorpoprotskiy.galleryview.domain.model.MediaItem

//2. Интерфейс репозитория.
interface MediaRepository {
    suspend fun getAllMedia(): List<MediaItem>
}