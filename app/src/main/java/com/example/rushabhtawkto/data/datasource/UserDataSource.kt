package com.example.rushabhtawkto.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.Query
import com.example.rushabhtawkto.api.UserApi
import com.example.rushabhtawkto.utils.Constants.STARTING_PAGE_INDEX
import com.example.tawktopractice.data.model.User
import retrofit2.HttpException
import java.io.IOException


class UserDataSource(
    private val userApi: UserApi
) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = userApi.getUsers(page)
            val players = response
            val nextKey = response.last().id
            LoadResult.Page(
                data = players,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (nextKey == null) null else page + 1
            )
        } catch (exception: IOException) {
            val error = IOException("Please Check Internet Connection")
            LoadResult.Error(error)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition
    }
}