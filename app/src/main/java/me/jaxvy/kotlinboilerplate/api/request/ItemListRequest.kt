package me.jaxvy.kotlinboilerplate.api.request

import io.reactivex.Single
import me.jaxvy.kotlinboilerplate.api.response.ItemListResponse
import me.jaxvy.kotlinboilerplate.persistence.entity.Item

class ItemListRequest : BaseRequest<ItemListResponse>() {

    override fun getSingle(): Single<ItemListResponse> {
        return api.getItems(firebaseAuth.currentUser?.uid)
    }

    override fun databaseOperationCallback(itemListResponse: ItemListResponse) {
        itemListResponse.items?.run {
            forEach { (id, responseItem) ->
                db.itemDao().createOrUpdate(Item(id, responseItem.title, responseItem.description))
            }
        }
    }
}