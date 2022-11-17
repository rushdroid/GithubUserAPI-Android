package com.example.rushabhtawkto.ui.userlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rushabhtawkto.data.repository.UserRepository
import com.example.tawktopractice.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private var currentResult: Flow<PagingData<User>>? = null

    @ExperimentalPagingApi
    fun getUsers(): Flow<PagingData<User>> {
        val newResult: Flow<PagingData<User>> =
            repository.getUsers().cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }

    @ExperimentalPagingApi
    fun getUsers(
        query: String
    ): Flow<PagingData<User>> {
        val newResult: Flow<PagingData<User>> =
            repository.getUsers(
                query = query
            ).cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }

    /**
     * Same thing but with Livedata
     */
    private var currentResultLiveData: LiveData<PagingData<User>>? = null

    fun getUsersLiveData(): LiveData<PagingData<User>> {
        val newResultLiveData: LiveData<PagingData<User>> =
            repository.getUsersLiveData().cachedIn(viewModelScope)
        currentResultLiveData = newResultLiveData
        return newResultLiveData
    }
}