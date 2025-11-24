package com.egorpoprotskiy.galleryview.data.repository.impl

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.egorpoprotskiy.galleryview.domain.model.MediaItem
import com.egorpoprotskiy.galleryview.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//3. Класс должен реализовывать (:) MediaRepository
class MediaRepositoryImpl(
    private val context: Context
): MediaRepository { // Реализуем наш контракт
    override suspend fun getAllMedia(): List<MediaItem> = withContext(Dispatchers.IO){ // 👈 Переключаемся на IO поток
        val mediaList = mutableListOf<MediaItem>()
        // Здесь будет логика ContentResolver
        val collectionUri: Uri = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,           // ID файла
            MediaStore.Files.FileColumns.DISPLAY_NAME,  // Имя файла
            MediaStore.Files.FileColumns.MIME_TYPE,     // Тип файла (image/jpeg, video/mp4)
            MediaStore.Files.FileColumns.DATE_ADDED,    // Поле, которое поможет сортировать
        )
        // a) Условия выборки (WHERE)
        val selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ? OR ${ MediaStore.Files.FileColumns.MEDIA_TYPE } = ?"
        val selectionArgs = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )
        // б) Порядок сортировки (DESC - по убыванию, то есть от новых к старым)
        val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
        // в) Выполнение запроса и безопасное закрытие курсора
        context.contentResolver.query(
            collectionUri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            //а) Получение индекса столбцов.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)

            //б) Итерируем по всем строкам(файлам), которые вернул запрос.
            while (cursor.moveToNext()) {
                //в) Извлекаем данные из текущей строки
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val dateAdded = cursor.getLong(dateAddedColumn)
                //г) Создаем финальный URI для файла
                val contentUri = Uri.withAppendedPath(
                    collectionUri,
                    id.toString()
                )
                //д) Создаем объекты MediaItem и добавляем его в список
                mediaList.add(
                    MediaItem(
                        id = id,
                        uri = contentUri, //используем созданный Uri
                        name = name,
                        mimeType = mimeType
                    )
                )
            }
        }
        // Пока оставим заглушку
        return@withContext mediaList
    }
}