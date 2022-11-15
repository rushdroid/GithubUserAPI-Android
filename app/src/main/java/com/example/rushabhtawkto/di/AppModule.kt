package com.example.rushabhtawkto.di

import android.app.Application
import androidx.room.Room
import com.example.rushabhtawkto.api.UserApi
import com.example.rushabhtawkto.data.dao.RemoteKeysDao
import com.example.rushabhtawkto.data.db.UserDatabase
import com.example.rushabhtawkto.utils.Constants
import com.example.tawktopractice.data.local.UserDao
import com.example.tawktopractice.data.local.UserDetailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideGetUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }


    @Provides
    @Singleton
    fun provideDatabase(application: Application, callback: UserDatabase.Callback): UserDatabase {
        return Room.databaseBuilder(application, UserDatabase::class.java, "user_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideUserDao(db: UserDatabase): UserDao {
        return db.getUserDao()
    }

    @Provides
    fun provideUserDetailDao(db: UserDatabase): UserDetailDao {
        return db.getUserDetailDao()
    }

    @Singleton
    @Provides
    fun providesKeysDao(appDataBase: UserDatabase): RemoteKeysDao = appDataBase.remoteKeysDao

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope