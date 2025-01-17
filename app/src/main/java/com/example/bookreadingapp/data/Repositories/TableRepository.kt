package com.example.bookreadingapp.data.Repositories

import com.example.bookreadingapp.data.Dao.TableDao
import com.example.bookreadingapp.data.entities.Image
import com.example.bookreadingapp.data.entities.Table
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class TableRepository(private val tableDao: TableDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var allTablesFromSpecificBook: Flow<List<Table>> = emptyFlow()

    fun getAllTablesOfSpecificChapter(chapterId: Int): Flow<List<Table>> {
        return tableDao.getAllTables(chapterId)

    }

//    fun getAllTablesOfSpecificChapter(chapterId: Int): Flow<List<Table>> {
//        setAllTablesOfSpecificChapter(chapterId)
//        return allTablesFromSpecificBook
//    }

    fun insertTable(table: Table) {
        coroutineScope.launch(Dispatchers.IO) {
            tableDao.insert(table)
        }
    }

    fun deleteTable(table: Table) {
        coroutineScope.launch(Dispatchers.IO) {
            tableDao.delete(table)
        }
    }
}