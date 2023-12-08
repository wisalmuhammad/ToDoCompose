package com.wisal.android.todocompose.di

import com.wisal.android.todocompose.repositories.Repository
import com.wisal.android.todocompose.repositories.RepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {


    @Singleton
    @Binds
    abstract fun provideRepository(repositoryImp: RepositoryImp): Repository

}