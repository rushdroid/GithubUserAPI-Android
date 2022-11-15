package com.example.rushabhtawkto.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rushabhtawkto.data.dao.RemoteKeysDao
import com.example.rushabhtawkto.data.entity.RemoteKeys
import com.example.rushabhtawkto.di.ApplicationScope
import com.example.tawktopractice.data.local.UserDao
import com.example.tawktopractice.data.local.UserDetailDao
import com.example.tawktopractice.data.model.User
import com.example.tawktopractice.data.model.Userdetail
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [User::class, Userdetail::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao
    abstract fun getUserDetailDao(): UserDetailDao
    abstract val remoteKeysDao: RemoteKeysDao

    class Callback @Inject constructor(
        private val database: Provider<UserDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()
}