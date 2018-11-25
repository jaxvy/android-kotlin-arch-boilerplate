package me.jaxvy.kotlinboilerplate.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

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