package com.egorpoprotskiy.galleryview.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.egorpoprotskiy.galleryview.domain.model.MediaItem
import androidx.compose.ui.graphics.Color
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest

//5. Создание одной карточки(фото)
@Composable
fun MediaGridItem(
    mediaItem: MediaItem, // Принимаем данные
    modifier: Modifier = Modifier
) {
    // Декодер для видео
    val imageRequest = ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
        .data(mediaItem.uri)
        .apply {
            if (mediaItem.mimeType.startsWith("video")) {
                decoderFactory(VideoFrameDecoder.Factory())
            }
        }
        .crossfade(true)
        .build()

    Box(
        modifier = modifier
            .aspectRatio(1f) //Задает элементу соотношение сторон 1:1(чтобы был квадрат)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        //В Coil всегда используй model для передачи того, что нужно загрузить.
        AsyncImage(
//            model = mediaItem.uri, //Основной параметр для передачи данных (ссылки, uri, файла) называется model.
            model = imageRequest, //Основной параметр для передачи данных (ссылки, uri, файла) называется model.
            contentDescription = mediaItem.name,// Описание
            modifier = Modifier.fillMaxSize(),//Заполняем весь Box
            contentScale = ContentScale.Crop,// Обрезает изображение, чтобы оно заполнило квадрат
        )
        // Добавлен фон для лучшей видимости иконки
        if (mediaItem.mimeType.startsWith("video")) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Video",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp) // Уменьшен размер иконки
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MediaGridItemPreview() {
    MaterialTheme {
//        MediaGridItem(
//            // Создаем фейковый объект MediaItem для предпросмотра
//            mediaItem = MediaItem(
//                id = 1L,
//                uri = "https://example.com/image.jpg", // Любая строка, Coil в превью покажет плейсхолдер или пустоту
//                name = "Test Image",
//                type = "image/jpeg"
//            ),
//            modifier = Modifier // Просто передаем пустой Modifier или убираем этот аргумент, т.к. у него есть дефолтное значение
//        )
    }
}
