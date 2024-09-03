package com.hawwas.ulibrary.data.db.dao

import androidx.room.*
import com.hawwas.ulibrary.data.db.entity.*

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun upsertItem(item: ItemEntity)

    @Delete
     fun deleteItem(item: ItemEntity)

    @Query("SELECT * FROM item_table WHERE subjectName = :subjectName")
     fun getItemsForSubject(subjectName: String): List<ItemEntity>
}