package me.jaxvy.kotlinboilerplate.persistence.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
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