package com.example.rushabhtawkto.ui.userdetail

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rushabhtawkto.data.repository.UserDetailRepository
import com.example.rushabhtawkto.utils.NetworkUtil.Companion.hasInternetConnection
import com.example.rushabhtawkto.utils.Resource
import com.example.tawktopractice.data.model.UserDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userDetailRepository: UserDetailRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val userDetail: MutableLiveData<Resource<UserDetail>> = MutableLiveData()
    val noteData: MutableLiveData<String> = MutableLiveData()

    fun getUserDetail(login: String) = viewModelScope.launch {
        getUserDetailFromLogin(login = login)
    }

    fun saveUserNote(userdetail: UserDetail) = viewModelScope.launch {
        saveUserNotetoDB(userdetail = userdetail)
    }

    private suspend fun saveUserNotetoDB(userdetail: UserDetail) {
        userDetail.postValue(Resource.Loading())
        try {
            userDetailRepository.saveUserNote(userdetail = userdetail)
            Log.d("TAG", "updateUser:")
            userDetail.postValue(Resource.Success(userdetail))
            val localUser =
                userDetailRepository.getUserFromLoginName(loginName = userdetail.login.toString())
            localUser.let {
                try {
                    it.isNoteAvailable = userdetail.note?.isNotBlank()
                    it.note = userdetail.note.orEmpty()
                } catch (ex: Exception) {
                    it.isNoteAvailable = false
                    it.note = userdetail.note.orEmpty()
                }
                val id = userDetailRepository.updateUser(it)
                Log.d("TAG", "insertUser: $id")
            }
            noteData.postValue("Your notes has been saved successfully")
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> {
                    userDetail.postValue(Resource.Error("Network Failure"))
                    noteData.postValue("Failed to save notes. Please try again")
                }
                else -> {
                    userDetail.postValue(Resource.Error("Something went wrong! Please try again later"))
                    noteData.postValue("Failed to save notes. Please try again")
                }
            }
        }
    }

    private suspend fun getUserDetailFromLogin(login: String) {
        userDetail.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(context)) {
                val response = userDetailRepository.getUserDetail(loginName = login)
                val result = handleBreakingNewsResponse(response)
                val localUserDetail = userDetailRepository.getUserDetailFromLogin(loginName = login)
                response.body()?.let {
                    try {
                        it.note = localUserDetail.note
                    } catch (ex: Exception) {
                        it.note = ""
                    }
                    val id = userDetailRepository.insertUserDetail(it)
                    Log.d("TAG", "insertUserDetail: $id")
                }
                userDetail.postValue(result)
            } else {
                val localUserDetail = userDetailRepository.getUserDetailFromLogin(loginName = login)
                localUserDetail.let { userDetail.postValue(Resource.Success(it)) }
            }
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> userDetail.postValue(Resource.Error("Network Failure"))
                else -> userDetail.postValue(Resource.Error("Something went wrong! Please try again later"))
            }
        }
    }

    private fun handleBreakingNewsResponse(response: Response<UserDetail>): Resource<UserDetail> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}