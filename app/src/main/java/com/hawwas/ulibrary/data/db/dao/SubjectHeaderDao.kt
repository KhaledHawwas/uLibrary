package com.hawwas.ulibrary.data.db.dao

import androidx.room.*
import com.hawwas.ulibrary.data.db.entity.*

@Dao
interface SubjectHeaderDao {

    @Query("SELECT * FROM subject_header_table")
    fun getAllSubjectHeaders(): List<SubjectHeaderEntity>

    @Query("SELECT * FROM subject_header_table WHERE name = :name")
    fun getSubjectHeader(name: String): SubjectHeaderEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(subjectHeaderEntity: SubjectHeaderEntity)

    @Delete
    fun delete(subjectHeaderEntity: SubjectHeaderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertAll(subjectHeaderEntities: List<SubjectHeaderEntity>)


}