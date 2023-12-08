package com.wisal.android.todocompose.di

import com.wisal.android.todocompose.data.source.LocalDataSource
import com.wisal.android.todocompose.data.source.LocalDataSourceImp
import com.wisal.android.todocompose.data.source.TasksDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataSourceModule {

    @Singleton
    @Provides
    fun provideIoDispatcher():CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideLocalDataSource(
        dao: TasksDao,
        coroutineDispatcher: CoroutineDispatcher
    ): LocalDataSource {
        return LocalDataSourceImp(
            dao = dao,
            ioDispatcher = coroutineDispatcher
        )
    }
}