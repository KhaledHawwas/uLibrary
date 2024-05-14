package com.hawwas.ulibrary.di

import android.app.*
import com.hawwas.ulibrary.data.remote.*
import com.hawwas.ulibrary.data.repo.*
import com.hawwas.ulibrary.domain.repo.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.components.*
import javax.inject.*

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppStorage(myApp: Application): LocalStorage {
        return LocalStorageImpl(myApp.applicationContext)
    }


    @Provides
    @Singleton
    fun provideAndroidDownloader(myApp: Application): AndroidDownloader {
        return AndroidDownloader(myApp.applicationContext)
    }

    @Provides
    @Singleton
    fun provideAppDataRepo(): AppDataRepo {
        return AppDataRepoImpl()
    }

    @Provides
    @Singleton
    fun provideRemoteRepo(): RemoteRepo {
        return RemoteRepoImpl()
    }
}