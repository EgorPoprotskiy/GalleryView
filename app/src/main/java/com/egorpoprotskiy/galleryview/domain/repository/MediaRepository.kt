package com.egorpoprotskiy.galleryview.domain.repository

import com.egorpoprotskiy.galleryview.domain.model.MediaItem

interface MediaRepository {
    suspend fun getAllMedia(): List<MediaItem>
}