package com.example.nanitbirthdayapp.di

import android.content.Context
import com.example.nanitbirthdayapp.data.local.SharedPreferencesManager
import com.example.nanitbirthdayapp.data.repository.BirthdayRepositoryImpl
import com.example.nanitbirthdayapp.domain.repository.BirthdayRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBirthdayRepository(
        birthdayRepositoryImpl: BirthdayRepositoryImpl
    ): BirthdayRepository

    companion object {
        @Provides
        @Singleton
        fun provideSharedPreferencesManager(
            @ApplicationContext context: Context
        ): SharedPreferencesManager {
            return SharedPreferencesManager(context)
        }
    }
}
