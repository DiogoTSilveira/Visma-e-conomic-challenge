package com.visma.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.visma.data.database.dao.ReceiptDao
import com.visma.data.database.entity.ReceiptEntity

@Database(
    entities = [ReceiptEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ReceiptDatabase : RoomDatabase() {
    abstract fun receiptDao(): ReceiptDao
}