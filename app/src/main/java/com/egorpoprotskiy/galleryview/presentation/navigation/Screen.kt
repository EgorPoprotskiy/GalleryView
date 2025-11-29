package com.egorpoprotskiy.galleryview.presentation.navigation

//8. Безопасное определене маршрутов для навигации
//Данный метод лучше подходит для небольших проектов(плохомасштабируем)
sealed class Screen(val route: String) {
    //Главный экран галереи (сетка)
    object Gallery: Screen("gallery_screen")
    //Экран деталей, который принимает ID в качестве аргумента.
    //'{mediaId}' — это шаблон аргумента в Compose Navigation.
    object Details: Screen("detail_screen/{mediaId}") {
        // Метод для создания маршрута с конкретным ID
        fun createRoute(mediaId: Long) = "detail_screen/$mediaId"
        // Ключ аргумента, который мы будем извлекать
        const val MEDIA_ID_KEY = "mediaId"
    }
}