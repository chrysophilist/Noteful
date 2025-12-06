package com.prince.noteful.di

import android.content.Context
import androidx.room.Room
import com.prince.noteful.data.local.NoteDao
import com.prince.noteful.data.local.NotefulDatabase
import com.prince.noteful.data.repo.NotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotesModule{

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NotefulDatabase = Room.databaseBuilder(
        context = context,
        klass = NotefulDatabase::class.java,
        name = "noteful.db"
    )
        .fallbackToDestructiveMigration(true)
        .build()

    @Provides
    @Singleton
    fun provideDao(database: NotefulDatabase): NoteDao = database.noteDao()


    @Provides
    @Singleton
    fun provideRepo(dao: NoteDao): NotesRepository = NotesRepository(dao)

}