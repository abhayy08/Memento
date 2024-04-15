package com.abhay.common.di

import com.abhay.common.DrawerStateManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    @ProvideDrawerState
    fun provideDrawerStateManager(): DrawerStateManager {
        return DrawerStateManager
    }
}
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ProvideDrawerState