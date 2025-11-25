package com.egorpoprotskiy.galleryview.presentation.screen

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.egorpoprotskiy.galleryview.domain.model.MediaItem

//5. Создание одной карточки(фото)
@Composable
fun MediaGridItem(
    mediaItem: MediaItem, // Принимаем данные
    modifier: Modifier = Modifier
) {
    //В Coil всегда используй model для передачи того, что нужно загрузить.
    AsyncImage(
        model = mediaItem.uri, //Основной параметр для передачи данных (ссылки, uri, файла) называется model.
        contentDescription = mediaItem.name,// Описание
        modifier = modifier.aspectRatio(1f),//Задает элементу соотношение сторон 1:1(чтобы был квадрат)
        contentScale = ContentScale.Crop,// Обрезает изображение, чтобы оно заполнило квадрат
    )
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
