package com.abhay.common.di

import com.abhay.common.DrawerStateManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {
    @Provides
    @Singleton
    @ProvideDrawerState // Use the custom annotation
    fun provideDrawerStateManager(): DrawerStateManager {
        return DrawerStateManager
    }
}
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ProvideDrawerState