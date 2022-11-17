package com.example.rushabhtawkto.data.remoteMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.rushabhtawkto.api.UserApi
import com.example.rushabhtawkto.data.db.UserDatabase
import com.example.rushabhtawkto.data.entity.RemoteKeys
import com.example.rushabhtawkto.utils.Constants.STARTING_PAGE_INDEX
import com.example.tawktopractice.data.model.User
import retrofit2.HttpException
import java.io.IOException
/*
* UserRemoteMediator
* */
@ExperimentalPagingApi
class UserRemoteMediator(
    private val service: UserApi,
    private val db: UserDatabase
) : RemoteMediator<Int, User>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        val key = when (loadType) {
            LoadType.REFRESH -> {
                if (db.getUserDao().count() > 0) return MediatorResult.Success(
                    endOfPaginationReached = false
                )
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.APPEND -> {
                getKey()
            }
        }

        try {
            if (key != null) {
                if (key.isEndReached) return MediatorResult.Success(endOfPaginationReached = true)
            }

            val page: Int = key?.nextKey ?: STARTING_PAGE_INDEX
            val apiResponse = service.getUsers(page)

            val userList = apiResponse

            db.withTransaction {
                val nextKey = userList.last().id

                db.remoteKeysDao.insertKey(
                    RemoteKeys(
                        0,
                        nextKey = nextKey,
                        isEndReached = false
                    )
                )
                db.getUserDao().insertMultipleUsers(userList)
            }
            return MediatorResult.Success(false)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKey(): RemoteKeys? {
        return db.remoteKeysDao.getKeys().firstOrNull()
    }
}