package com.example.rushabhtawkto.data.repository

import com.example.rushabhtawkto.api.UserApi
import com.example.tawktopractice.data.local.UserDao
import com.example.tawktopractice.data.local.UserDetailDao
import com.example.tawktopractice.data.model.User
import com.example.tawktopractice.data.model.UserDetail
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDetailRepository @Inject constructor(
    private val userDao: UserDao,
    private val userDetailDao: UserDetailDao,
    private val userApi: UserApi
) {

    suspend fun getUserDetail(loginName: String) = userApi.getUserDetail(login = loginName)

    suspend fun getUserDetailFromLogin(loginName: String) =
        userDetailDao.getUserDetailFromLogin(loginName = loginName)

    suspend fun getUserFromLoginName(loginName: String) =
        userDao.getUserFromLoginName(loginName = loginName)

    suspend fun insertUserDetail(userdetail: UserDetail) =
        userDetailDao.insert(userdetail = userdetail)

    suspend fun saveUserNote(userdetail: UserDetail) =
        userDetailDao.update(userdetail = userdetail)

    suspend fun updateUser(user: User) = userDao.update(user = user)
}