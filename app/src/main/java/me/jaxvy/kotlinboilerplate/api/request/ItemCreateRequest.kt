package me.jaxvy.kotlinboilerplate.api.request

import io.reactivex.Single
import me.jaxvy.kotlinboilerplate.api.model.Item
import me.jaxvy.kotlinboilerplate.api.response.ItemCreateResponse

class ItemCreateRequest(
        val title: String,
        val description: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
) : BaseRequest<ItemCreateResponse>(onSuccess, onError) {

    override fun getSingle(): Single<ItemCreateResponse> {
        return api.createItem(firebaseAuth.currentUser?.uid, Item(title, description))
    }
}