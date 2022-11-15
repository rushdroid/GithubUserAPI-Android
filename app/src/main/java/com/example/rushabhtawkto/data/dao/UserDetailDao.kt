package com.example.tawktopractice.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tawktopractice.data.model.Userdetail

@Dao
interface UserDetailDao {

    @Query("SELECT * FROM user_detail_table")
    fun getAllUserdetails(): LiveData<List<Userdetail>>

    @Query("SELECT * FROM user_detail_table where login In (:loginName)")
    suspend fun getUserDetailFromName(loginName: String): Userdetail

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userdetail: Userdetail): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(userdetail: Userdetail)

    @Delete
    suspend fun delete(userdetail: Userdetail)

    @Query("DELETE FROM user_detail_table")
    suspend fun deleteAllUsersDetail()
}