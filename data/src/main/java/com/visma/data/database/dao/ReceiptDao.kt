package com.visma.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.visma.data.database.entity.ReceiptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDao {

    @Query("SELECT * FROM receipts ORDER BY date DESC")
    fun getAllReceipts(): Flow<List<ReceiptEntity>>

    @Query("SELECT * FROM receipts WHERE id = :id ORDER BY date DESC")
    suspend fun getReceipt(id: Long): ReceiptEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceipt(note: ReceiptEntity): Long

    @Query("DELETE FROM receipts WHERE id = :id")
    suspend fun deleteReceipt(id: Long)

}
