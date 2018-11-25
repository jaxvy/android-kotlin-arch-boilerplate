package me.jaxvy.kotlinboilerplate.ui.home

import androidx.lifecycle.LiveData
import me.jaxvy.kotlinboilerplate.api.request.ItemCreateRequest
import me.jaxvy.kotlinboilerplate.api.request.ItemListRequest
import me.jaxvy.kotlinboilerplate.persistence.entity.Item
import me.jaxvy.kotlinboilerplate.ui.common.BaseViewModel

class HomeViewModel : BaseViewModel() {

    internal var items: LiveData<List<Item>>

    internal var isCreatingItem = false
    private var onCreateItemSuccess: (() -> (Unit))? = null
    private var onCreateItemError: ((Throwable) -> Unit)? = null

    /**
     * This constructor is called once during orientation changes (because of it's a ViewModel dahh)
     */
    init {
        fetchItems()
        items = db.itemDao().getAll()
    }

    fun fetchItems() {
        apiManager.call(ItemListRequest())
    }

    fun logout() {
        dbManager.runOnBackgroundThread({ db.itemDao().deleteAllItems() })
        firebaseAuth.signOut();
        sharedPrefs.clearAuthToken();
    }

    fun registerCreateItemHandlers(onSuccess: () -> (Unit), onError: (Throwable) -> Unit) {
        onCreateItemSuccess = onSuccess
        onCreateItemError = onError
    }

    /**
     * We need to call the onCreateItemSuccess and onCreateItemError from their corresponding
     * callbacks rather than passing them as for the callbacks of the ItemCreateRequest.
     * This way correct activity's instance is referenced and we don't have memory leaks on
     * orientation change
     */
    fun createNewItem(title: String, description: String) {
        isCreatingItem = true
        apiManager.call(ItemCreateRequest(title, description,
                onSuccess = {
                    isCreatingItem = false
                    onCreateItemSuccess?.invoke()
                },
                onError = {
                    throwable ->
                    isCreatingItem = false
                    onCreateItemError?.invoke(throwable)
                }))
    }

    override fun onCleared() {
        super.onCleared()
        onCreateItemSuccess = null
        onCreateItemError = null
    }
}