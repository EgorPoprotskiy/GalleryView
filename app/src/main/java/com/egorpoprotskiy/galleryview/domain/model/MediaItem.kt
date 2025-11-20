package com.egorpoprotskiy.galleryview.domain.model

import android.net.Uri

data class MediaItem (
    val id: Long,
    val uri: Uri,
    val name: String,
    val mimeType: String
)