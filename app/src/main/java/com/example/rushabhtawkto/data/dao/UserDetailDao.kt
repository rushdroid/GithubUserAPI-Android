package com.example.tawktopractice.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tawktopractice.data.model.UserDetail

@Dao
interface UserDetailDao {

    @Query("SELECT * FROM user_detail_table")
    fun getAllUserdetails(): LiveData<List<UserDetail>>

    @Query("SELECT * FROM user_detail_table where login In (:loginName)")
    suspend fun getUserDetailFromLogin(loginName: String): UserDetail

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userdetail: UserDetail): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(userdetail: UserDetail)

    @Delete
    suspend fun delete(userdetail: UserDetail)

    @Query("DELETE FROM user_detail_table")
    suspend fun deleteAllUsersDetail()
}