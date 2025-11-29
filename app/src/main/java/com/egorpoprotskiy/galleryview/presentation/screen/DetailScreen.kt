package com.egorpoprotskiy.galleryview.presentation.screen

import android.net.Uri
import com.egorpoprotskiy.galleryview.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.egorpoprotskiy.galleryview.domain.model.MediaItem
import com.egorpoprotskiy.galleryview.domain.repository.MediaRepository
import com.egorpoprotskiy.galleryview.presentation.components.GalleryTopAppBar
import com.egorpoprotskiy.galleryview.presentation.viewmodel.GalleryViewModel

//10. Создание экрана Деталей
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    mediaId: Long, //ID файла, переданный через навигацию
    viewModel: GalleryViewModel,
    navigateBack: () -> Unit
) {
    //Считываем состояние всего списка медиафайлов из ViewModel
    val mediaList by viewModel.mediaList.collectAsStateWithLifecycle()
    val mediaItem: MediaItem? = remember(mediaList, mediaId) {
        mediaList.find { item ->
            item.id == mediaId
        }
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            GalleryTopAppBar(
                title = mediaItem?.name ?: stringResource(R.string.detail_screen),
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior
            )
        }
    ) {  innerPadding ->
    if (mediaItem != null) {
        val scrollState = rememberScrollState()
        //Отображаем найденный файл
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(scrollState)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Text(
//                text = mediaItem.name,
//                style = MaterialTheme.typography.headlineSmall,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
            // AsyncImage для отображения медиафайла в полном размере
            AsyncImage(
                model = mediaItem.uri, //Основной параметр для передачи данных (ссылки, uri, файла) называется model.
                contentDescription = mediaItem.name,// Описание
                modifier = Modifier
//                    .fillMaxSize(),//Заполняем весь Box
                    .fillMaxWidth(),//Заполняем весь Box
//                    .aspectRatio(1f), //соотношение сторон(квадрат или можно выбрать другое)
                contentScale = ContentScale.Fit,// Fit, чтобы увидеть весь файл
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Тип файла: ${mediaItem.mimeType}")
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Медиафайл с ID=$mediaId не найден.", color = Color.Red)
        }
    }
    }
}


//ДЛЯ ПРЕВЬЮ
// ВАЖНО: Создаем заглушку (Mock) для MediaRepository
class MockMediaRepository : MediaRepository {
    // Возвращаем список, содержащий ТОЛЬКО один фиктивный элемент для превью
    override suspend fun getAllMedia(): List<MediaItem> {
        return listOf(
            MediaItem(
                id = 999L,
                uri = Uri.parse("android.resource://com.example.mygallery/drawable/ic_launcher_foreground"), // Использование системного drawable
                name = "Test Media File.jpg",
                mimeType = "image/jpeg",
//                dateAdded = System.currentTimeMillis() / 1000
            )
        )
    }
}

// ВАЖНО: Создаем заглушку (Mock) для ViewModelFactory
class MockGalleryViewModelFactory(
    private val repository: MediaRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GalleryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Preview(showBackground = true, name = "Detail Screen Preview")
@Composable
fun DetailScreenPreview() {
    // 1. Создаем ViewModel с Mock Repository
    val mockRepository = MockMediaRepository()
    val mockViewModel: GalleryViewModel = viewModel(
        factory = MockGalleryViewModelFactory(mockRepository)
    )

    // 2. Вызываем DetailScreen с ID фиктивного файла (999L)
    //    ViewModel загрузит список, содержащий файл с ID=999L
    MaterialTheme { // Обертываем в вашу тему
        DetailScreen(
            mediaId = 999L,
            viewModel = mockViewModel,
            navigateBack = {}
        )
    }
}