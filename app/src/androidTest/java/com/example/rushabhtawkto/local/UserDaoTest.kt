package com.example.rushabhtawkto.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.rushabhtawkto.data.db.UserDatabase
import com.example.tawktopractice.data.local.UserDao
import com.example.tawktopractice.data.model.User
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
class UserDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userDatabase: UserDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        userDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDatabase::class.java
        ).allowMainThreadQueries().build()

        userDao = userDatabase.getUserDao()

        //Add default value
        val user = User(
            "rushabh",
            0,
            "123",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            false,
            10.0,
            "Testing Note",
            false
        )
        runTest {
            userDao.insert(user)
        }
    }

    @After
    fun teardown() {
        userDatabase.close()
    }

    @Test
    fun getAllUser() = runTest {
        val userList = userDao.getUsersList()
        assertThat(userList).isNotEmpty()
    }

    @Test
    fun insert() = runTest {
        val user = User(
            "rushabh",
            0,
            "123",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            false,
            10.0,
            "Testing Note",
            true
        )
        userDao.insert(user)

        val userList = userDao.getUsersList()
        assertThat(userList).contains(user)
    }

    @Test
    fun getUserFromLoginName_test() {
        runTest {
            val user = userDao.getUserFromLoginName(loginName = "rushabh")
            assertThat(user.login).contains("rushabh")
        }
    }

    fun updateUser() = runTest {
        val user = User(
            "rushabh11",
            0,
            "12311",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            true,
            11.0,
            "testing note",
            isNoteAvailable = false
        )
        userDao.insert(user)
        user.note = "Updating Note"
        userDao.update(user)
        val userQuery = userDao.getUserFromLoginName(loginName = "rushabh")
        assertThat(userQuery.note).isEqualTo("Updating Note")
    }

    @Test
    fun deleteAllUsers() = runTest {
        val user = User(
            "rushabh11",
            0,
            "12311",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            true,
            11.0,
            "false",
            isNoteAvailable = false
        )

        userDao.insert(user)
        val list = userDao.getUsersList()
        assertThat(list).isNotEmpty()
        userDao.clearRepos()

        val userList = userDao.getUsersList()
        assertThat(userList).isEmpty()
    }
}