package me.jaxvy.kotlinboilerplate.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.jaxvy.kotlinboilerplate.persistence.entity.Item

@Dao
interface ItemDao {

    @Query("SELECT * FROM item")
    fun getAll(): LiveData<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createOrUpdate(item : Item)

    @Query("DELETE FROM item")
    fun deleteAllItems()
}