package me.jaxvy.kotlinboilerplate.api

import io.reactivex.Observable
import me.jaxvy.kotlinboilerplate.api.model.Item
import me.jaxvy.kotlinboilerplate.api.response.ItemCreateResponse
import me.jaxvy.kotlinboilerplate.api.response.ItemListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {

    @POST("/users/{userId}/items.json")
    fun createItem(@Path("userId") userId: String?, @Body item: Item): Observable<ItemCreateResponse>

    @GET("/users/{userId}.json")
    fun getItems(@Path("userId") userId: String?): Observable<ItemListResponse>
}