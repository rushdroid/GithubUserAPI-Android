package com.example.rushabhtawkto.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.rushabhtawkto.data.db.UserDatabase
import com.example.tawktopractice.data.local.UserDetailDao
import com.example.tawktopractice.data.model.UserDetail
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class UserdetaiDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userDatabase: UserDatabase
    private lateinit var userDetailDao: UserDetailDao

    @Before
    fun setup() {
        userDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDatabase::class.java
        ).allowMainThreadQueries().build()

        userDetailDao = userDatabase.getUserDetailDao()

        //Add default value
        val userdetail = UserDetail(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            10,
            "",
            20,
            "",
            "",
            "",
            "",
            "",
            9,
            "",
            "",
            "",
            "",
            "",
            0,
            1,
            "",
            "",
            false,
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
        runTest {
            userDetailDao.insert(userdetail)
        }
    }

    @After
    fun teardown() {
        userDatabase.close()
    }

    @Test
    fun getAllUserdetails() = runTest {
        val userdetail = userDetailDao.getAllUserdetails().getOrAwaitValue()
        assertThat(userdetail).isNotEmpty()
    }

    @Test
    fun insertUser() = runTest {
        val userdetail = UserDetail(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            10,
            "",
            20,
            "",
            "",
            "",
            "",
            "",
            9,
            "",
            "",
            "",
            "",
            "",
            0,
            1,
            "",
            "",
            false,
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
        userDetailDao.insert(userdetail)

        val userDetailList = userDetailDao.getAllUserdetails().getOrAwaitValue()
        assertThat(userDetailList).contains(userdetail)
    }

    @Test
    fun deleteUser() = runTest {
        val userdetail = UserDetail(
            "https://google.com",
            "",
            "",
            "",
            "",
            "",
            "",
            10,
            "",
            20,
            "",
            "",
            "",
            "",
            "",
            9,
            "",
            "",
            "",
            "",
            "",
            0,
            1,
            "",
            "",
            false,
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )

        val userdetail2 = UserDetail(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            10,
            "",
            20,
            "",
            "",
            "",
            "",
            "",
            10,
            "",
            "",
            "",
            "",
            "",
            0,
            1,
            "",
            "",
            false,
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
        userDetailDao.insert(userdetail)
        userDetailDao.insert(userdetail2)
        userDetailDao.delete(userdetail)

        val userDetailList = userDetailDao.getAllUserdetails().getOrAwaitValue()
        assertThat(userDetailList).doesNotContain(userdetail)
    }

    @Test
    fun deleteAllUsers() = runTest {
        val userdetail = UserDetail(
            "https://google.com",
            "",
            "",
            "",
            "",
            "",
            "",
            10,
            "",
            20,
            "",
            "",
            "",
            "",
            "",
            9,
            "",
            "",
            "",
            "",
            "",
            0,
            1,
            "",
            "",
            false,
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )

        userDetailDao.insert(userdetail)
        userDetailDao.deleteAllUsersDetail()

        val userDetailList = userDetailDao.getAllUserdetails().getOrAwaitValue()
        assertThat(userDetailList).isEmpty()
    }

    @Test
    fun updateUserDetails() = runTest {
        val userdetail = UserDetail(
            "https://google.com",
            "",
            "",
            "",
            "",
            "",
            "",
            10,
            "",
            20,
            "",
            "",
            "",
            "",
            "",
            9,
            "",
            "",
            "",
            "",
            "",
            0,
            1,
            "",
            "",
            false,
            "",
            "",
            "",
            "",
            "",
            "",
            "123"
        )

        userDetailDao.update(userdetail)

        val userDetailList = userDetailDao.getAllUserdetails().getOrAwaitValue()
        assertThat(userDetailList).contains(userdetail)
    }
}