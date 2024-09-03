package com.hawwas.ulibrary.data.db

import androidx.room.*
import com.hawwas.ulibrary.data.db.dao.*
import com.hawwas.ulibrary.data.db.entity.*

const val databaseName = "MyDatabase.db"
const val dbVersion = 1
@Database(
    entities = [ ItemEntity::class, SubjectEntity::class],
    version = dbVersion,
)
abstract class MyDatabase :RoomDatabase (){
    abstract fun itemDao():ItemDao
abstract fun subjectDao():SubjectDao

}