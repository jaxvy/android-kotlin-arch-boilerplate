package me.jaxvy.kotlinboilerplate.ui.home

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.adapter_item.view.*
import me.jaxvy.kotlinboilerplate.R
import me.jaxvy.kotlinboilerplate.persistence.entity.Item
import me.jaxvy.kotlinboilerplate.utils.inflate

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private var itemList: List<Item>? = null
    private val onClickListener: (Item) -> Unit

    constructor(onClickListener: (Item) -> Unit) : super() {
        this.onClickListener = onClickListener
    }

    fun setItems(newItemList: List<Item>) {
        itemList = newItemList;
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup?, viewType: Int): ItemAdapter.ItemViewHolder {
        return ItemViewHolder(viewGroup?.inflate(R.layout.adapter_item), onClickListener)
    }

    override fun onBindViewHolder(viewHolder: ItemAdapter.ItemViewHolder?, position: Int) {
        viewHolder?.bind(itemList!![position])
    }

    override fun getItemCount() = itemList?.size ?: 0

    class ItemViewHolder(itemView: View?, val onClickListener: (Item) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Item) = with(itemView) {
            title.text = item.title
            description.text = item.description
            setOnClickListener { onClickListener(item) }
        }
    }
}