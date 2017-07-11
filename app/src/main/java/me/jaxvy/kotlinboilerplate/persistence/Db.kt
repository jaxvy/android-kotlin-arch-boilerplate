package me.jaxvy.kotlinboilerplate.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import me.jaxvy.kotlinboilerplate.persistence.dao.ItemDao
import me.jaxvy.kotlinboilerplate.persistence.entity.Item

@Database(entities = arrayOf(Item::class), version = 1)
abstract class Db : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}