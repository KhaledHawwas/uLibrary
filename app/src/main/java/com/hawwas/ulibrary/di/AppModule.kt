package com.hawwas.ulibrary.di

import android.app.*
import android.content.*
import androidx.room.*
import com.hawwas.ulibrary.data.db.*
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
    fun provideRemoteRepo(androidDownloader: AndroidDownloader): RemoteRepo {
        return RemoteRepoImpl(androidDownloader)
    }
    @Provides
    @Singleton
    fun provideDatabase(myApp: Application): MyDatabase {
        return Room.databaseBuilder(
            myApp.applicationContext,
            MyDatabase::class.java,
            databaseName
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration().build()
    }
    @Provides
    @Singleton
    fun provideDatabaseRepo(database: MyDatabase): DatabaseRepo {
        return DatabaseRepoImpl(database)
    }
}