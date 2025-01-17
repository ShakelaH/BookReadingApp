package com.example.bookreadingapp.data.Repositories

import com.example.bookreadingapp.data.Dao.ImageDao
import com.example.bookreadingapp.data.entities.Image
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ImagesRepository(private val imageDao: ImageDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

//    private fun setAllImagesOfSpecificChapter(chapterId: Int) {
//        coroutineScope.launch(Dispatchers.IO) {
//            imageDao.getAllImages(chapterId).collect{ allImagesFromSpecificBook}
//        }
//    }

    fun getAllImagesOfSpecificChapter(id: Int): Flow<List<Image>> {
//        setAllImagesOfSpecificChapter(id)
        return imageDao.getAllImages(id)
    }

    fun insertImage(image: Image) {
        coroutineScope.launch(Dispatchers.IO) {
            imageDao.insert(image)
        }
    }

    fun deleteImage(image: Image) {
        coroutineScope.launch(Dispatchers.IO) {
            imageDao.delete(image)
        }
    }
}