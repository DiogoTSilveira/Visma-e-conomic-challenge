package com.visma.data.database.di

import android.content.Context
import androidx.room.Room
import com.visma.data.database.ReceiptDatabase
import com.visma.data.database.dao.ReceiptDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ReceiptDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ReceiptDatabase::class.java,
            ReceiptDatabase::class.java.simpleName
        ).build()
    }

    @Singleton
    @Provides
    fun provideReceiptDao(receiptDatabase: ReceiptDatabase): ReceiptDao {
        return receiptDatabase.receiptDao()
    }
}
