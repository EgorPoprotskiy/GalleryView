package com.egorpoprotskiy.galleryview.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.egorpoprotskiy.galleryview.presentation.screen.GalleryScreen
import com.egorpoprotskiy.galleryview.presentation.viewmodel.GalleryViewModel

//9. Создание графа навигации
@Composable
fun GalleryNavGraph(
    navController: NavHostController,
    viewModel: GalleryViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Gallery.route
    ) {
        //Маршрут для главного экрана (GalleryScreen)
        composable(route = Screen.Gallery.route) {
            GalleryScreen(
                viewModel = viewModel,
                //Передаем лямбду, которая принимает 'mediaId'
                onMediaClick = { mediaId ->
                    // Навигация вызывается только при клике, используя переданный mediaId
                    navController.navigate(Screen.Details.createRoute(mediaId))
                }
            )
        }
            composable(
                route = Screen.Details.route,
                arguments = listOf(
                    navArgument(Screen.Details.MEDIA_ID_KEY) {
                        type = NavType.LongType
                    }
                )
            ) {
                Text("Детали медиафайла")
            }
    }
}