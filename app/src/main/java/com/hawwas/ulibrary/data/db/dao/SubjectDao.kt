package com.hawwas.ulibrary.data.db.dao

import androidx.room.*
import com.hawwas.ulibrary.data.db.entity.*

@Dao
interface SubjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun upsertSubject(subjectEntity: SubjectEntity)


    @Delete
     fun deleteSubject(subjectEntity: SubjectEntity)

    @Transaction
    @Query("SELECT * FROM subject_table WHERE name = :subjectName")
     fun getSubjectWithItems(subjectName: String): SubjectWithItems

    @Query("SELECT * FROM subject_table")
        fun getAllSubjects(): List<SubjectWithItems>
}