package me.jaxvy.kotlinboilerplate.api.request

import io.reactivex.Observable
import me.jaxvy.kotlinboilerplate.api.response.ItemListResponse
import me.jaxvy.kotlinboilerplate.persistence.entity.Item

class ItemListRequest : BaseRequest<ItemListResponse>() {

    override fun getObservable(): Observable<ItemListResponse> {
        return api.getItems(firebaseAuth.currentUser?.uid)
    }

    override fun runOnBackgroundThread(itemListResponse: ItemListResponse) {
        if (itemListResponse.items != null) {
            for ((id, responseItem) in itemListResponse.items) {
                var item = Item(id, responseItem.title, responseItem.description)
                db.itemDao().createOrUpdate(item)
            }
        }
    }
}