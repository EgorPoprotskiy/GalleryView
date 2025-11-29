package com.egorpoprotskiy.galleryview.presentation

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.egorpoprotskiy.galleryview.data.repository.impl.MediaRepositoryImpl
import com.egorpoprotskiy.galleryview.domain.repository.MediaRepository
import com.egorpoprotskiy.galleryview.presentation.screen.GalleryScreen
import com.egorpoprotskiy.galleryview.presentation.ui.theme.GalleryViewTheme
import com.egorpoprotskiy.galleryview.presentation.viewmodel.GalleryViewModel


//7.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GalleryViewTheme {
                // Создаем зависимости вручную (Ручной DI)
                val repository = remember { MediaRepositoryImpl(applicationContext) }
                // Используем ViewModel с созданным Factory
                val viewModel: GalleryViewModel = viewModel(
                    factory = GalleryViewModelFactory(repository)
                )
                // Вызываем приложение
                GalleryApp(
                    viewModel = viewModel,
                    context = applicationContext
                )
            }
        }
    }
}

// Создаем Factory для ViewModel, чтобы передать в него Repository
// (Ручное внедрение зависимостей)
class GalleryViewModelFactory(
    private val repository: MediaRepository
): ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        // Проверяем, что это наш ViewModel, и создаем его.
        if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GalleryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun GalleryApp(viewModel: GalleryViewModel, context: Context) {
    // 1. Состояние, которое отслеживает, предоставлены ли разрешения
    var permissionsGranted by rememberSaveable { mutableStateOf(false) }
    // 2. Список разрешений, зависящий от версии Android
    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        //Android 13
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
        )
    } else {
        // Android 12 и ниже
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
    // 3. Лаунчер для запроса разрешений
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissionsMap ->
            // Проверяем, предоставлены ли все разрешения
            permissionsGranted = permissionsMap.values.all { isGranted -> isGranted }
        }
    )
    // 4. Запускаем запрос разрешений при первом запуске
    LaunchedEffect(Unit) {
        // Если разрешения еще не предоставлены, запрашиваем их
        if (!permissionsGranted) {
            permissionsLauncher.launch(permissionsToRequest)
        }
    }
    // 5. Условное отображение UI
    if (permissionsGranted) {
        // Если разрешения есть, показываем Галерею
        GalleryScreen(
            viewModel = viewModel,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        // Если разрешений нет, показываем заглушку
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Требуется доступ к медиафайлам для отображения галереи.")
        }
    }
}