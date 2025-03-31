package com.visma.data.di

import com.visma.data.receipt.repository.ReceiptRepositoryImpl
import com.visma.domain.receipt.repository.ReceiptRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(repository: ReceiptRepositoryImpl): ReceiptRepository

}
