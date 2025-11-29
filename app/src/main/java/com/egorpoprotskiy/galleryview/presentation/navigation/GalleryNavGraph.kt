package com.egorpoprotskiy.galleryview.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.egorpoprotskiy.galleryview.presentation.screen.DetailScreen
import com.egorpoprotskiy.galleryview.presentation.screen.GalleryScreen
import com.egorpoprotskiy.galleryview.presentation.viewmodel.GalleryViewModel

//9. Создание графа навигации
@Composable
fun GalleryNavGraph(
    navController: NavHostController,
    viewModel: GalleryViewModel,
    modifier: Modifier = Modifier //экраны вне статус бара
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Gallery.route,
        modifier = modifier //экраны вне статус бара
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
        ) { backStackEntry ->
            //Извлекаем ID: используем константу ключа и метод getLong()
            val mediaId = backStackEntry.arguments?.getLong(Screen.Details.MEDIA_ID_KEY) ?: 0L
            //Вызываем DetailScreen, передавая извлеченный ID и ViewModel
            DetailScreen(
                mediaId = mediaId,
                viewModel = viewModel,
                navigateBack = {navController.navigateUp()}
            )
        }
    }
}