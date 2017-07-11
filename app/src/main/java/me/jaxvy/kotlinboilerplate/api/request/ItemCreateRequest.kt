package me.jaxvy.kotlinboilerplate.api.request

import android.util.Log
import io.reactivex.Observable
import me.jaxvy.kotlinboilerplate.api.model.Item
import me.jaxvy.kotlinboilerplate.api.response.ItemCreateResponse

class ItemCreateRequest(val title: String,
                        val description: String,
                        onSuccess: () -> Unit,
                        onError: (Throwable) -> Unit) :
        BaseRequest<ItemCreateResponse>(onSuccess, onError, "ItemCreateRequest") {

    override fun getObservable(): Observable<ItemCreateResponse> {
        return api.createItem(firebaseAuth.currentUser?.uid, Item(title, description))
    }

    override fun runOnBackgroundThread(itemCreateResponse: ItemCreateResponse) {
        val name = itemCreateResponse.name
        Log.d("ItemCreateRequest", "Item id: " + name)
    }
}