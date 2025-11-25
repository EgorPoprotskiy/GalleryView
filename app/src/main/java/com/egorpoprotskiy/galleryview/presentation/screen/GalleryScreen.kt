package com.egorpoprotskiy.galleryview.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.egorpoprotskiy.galleryview.presentation.viewmodel.GalleryViewModel

//6. Создание сетки для отображения медиафайлов.
@Composable
fun GalleryScreen(
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    //считывает состояние mediaList с помощью collectAsState()
    val mediaList by viewModel.mediaList.collectAsStateWithLifecycle()
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // Определяем 3 колонки
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(
            items = mediaList,
            key = {item -> item.id}
        ) { mediaItem ->
            MediaGridItem(
                mediaItem = mediaItem
            )
        }
    }
}