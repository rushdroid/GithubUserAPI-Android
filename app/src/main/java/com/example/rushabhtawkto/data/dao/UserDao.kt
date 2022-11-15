package com.example.tawktopractice.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.example.tawktopractice.data.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultiplePlayers(list: List<User>)

    @Query("SELECT * FROM user_table")
    fun getUsers(): PagingSource<Int, User>

    @Query("DELETE FROM user_table")
    suspend fun clearRepos()

    @Query("SELECT COUNT(id) from user_table")
    suspend fun count(): Int

    /*  @Query("SELECT * FROM user_table")
      fun getAllUsers(): LiveData<List<User>>

      @Query("SELECT * FROM user_table")
      suspend fun getAllUserList(): List<User>

      @Query("SELECT * FROM user_table where login LIKE :query")
      suspend fun searchUserWithLogin(query: String): List<User>

      @Insert(onConflict = OnConflictStrategy.REPLACE)
      suspend fun insert(user: User): Long

      @Insert(onConflict = OnConflictStrategy.REPLACE)
      suspend fun insertAll(users: List<User>)

      @Delete
      suspend fun delete(user: User)

      @Query("DELETE FROM user_table")
      suspend fun deleteAllUsers()*/
}