package com.hawwas.ulibrary.di

import android.app.Application
import com.hawwas.ulibrary.data.*
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
    fun provideRemoteService(): RemoteService {
        return RemoteService()
    }



    @Provides
    @Singleton
    fun provideAppStorage(myApp: Application): FileStorage {
        return AppStorage(myApp.applicationContext)
    }


    @Provides
    @Singleton
    fun provideDataStore(myApp: Application): DataStoreManager {
        return DataStoreManager(myApp.applicationContext)
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
}