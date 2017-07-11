package me.jaxvy.kotlinboilerplate.ui.home

interface HomeActivityCallback {

    fun createNewItem(title: String, description: String)

    fun isCreatingItem() : Boolean
}