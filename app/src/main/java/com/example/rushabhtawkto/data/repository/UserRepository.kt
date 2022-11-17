package com.example.rushabhtawkto.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.rushabhtawkto.api.UserApi
import com.example.rushabhtawkto.data.datasource.UserDataSource
import com.example.rushabhtawkto.data.db.UserDatabase
import com.example.rushabhtawkto.data.remoteMediator.UserRemoteMediator
import com.example.tawktopractice.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val db: UserDatabase
) {

    private val pagingSourceFactory = { db.getUserDao().getUsers() }

    /**
     * for caching
     */
    @ExperimentalPagingApi
    fun getUsers(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = UserRemoteMediator(
                userApi,
                db
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow //can also return livedata
    }


    /**
     * The same thing but with Livedata
     */
    fun getUsersLiveData(
    ): LiveData<PagingData<User>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                UserDataSource(userApi)
            }
        ).liveData
    }

    /**
     * for caching
     */
    @ExperimentalPagingApi
    fun getUsers(query: String): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { db.getUserDao().getUsers(query = "%"+query+"%") }
        ).flow //can also return livedata
    }


    /**
     * The same thing but with Livedata
     */
    fun getUsersLiveData(
        query: String
    ): LiveData<PagingData<User>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                UserDataSource(userApi)
            }
        ).liveData
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 25
    }

}