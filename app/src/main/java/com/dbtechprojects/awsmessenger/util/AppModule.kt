package com.dbtechprojects.awsmessenger.util

import android.content.Context
import com.dbtechprojects.awsmessenger.database.AmplifyAuth
import com.dbtechprojects.awsmessenger.database.DatabaseHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Singleton

// App class to provide dependencies

@Module
@InstallIn(ActivityComponent::class)
class AppModule {


    @Provides
    fun provideDB(@ActivityContext context: Context): DatabaseHandler = DatabaseHandler()

    @Provides
    fun provideAuth(@ActivityContext context: Context): AmplifyAuth = AmplifyAuth()

}