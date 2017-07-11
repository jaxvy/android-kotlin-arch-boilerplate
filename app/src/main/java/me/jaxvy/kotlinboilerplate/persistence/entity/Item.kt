package me.jaxvy.kotlinboilerplate.persistence.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class Item {

    @PrimaryKey
    var id: String

    var title: String

    var description: String

    constructor(id: String, title: String, description: String) {
        this.id = id
        this.title = title
        this.description = description
    }
}