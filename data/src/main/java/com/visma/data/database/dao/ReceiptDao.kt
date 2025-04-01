package com.visma.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.visma.data.database.entity.ReceiptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDao {

    @Query("SELECT * FROM receipts ORDER BY date DESC")
    fun getAllReceipts(): Flow<List<ReceiptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceipt(note: ReceiptEntity): Long

    @Update
    suspend fun updateReceipt(note: ReceiptEntity)

    @Delete
    suspend fun deleteReceipt(note: ReceiptEntity)

}
