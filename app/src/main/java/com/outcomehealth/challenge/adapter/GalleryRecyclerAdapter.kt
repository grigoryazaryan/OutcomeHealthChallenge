package com.outcomehealth.challenge.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.outcomehealth.challenge.R
import com.outcomehealth.challenge.data.GalleryItem
import kotlinx.android.synthetic.main.gallery_item.view.*

/**
 * Created by Grigory Azaryan on 10/10/20.
 */

class GalleryRecyclerAdapter(val context: Context) : RecyclerView.Adapter<GalleryRecyclerAdapter.ItemViewHolder>() {
    private var data: List<GalleryItem> = emptyList()

    private var clickListener: ((item: GalleryItem, position: Int, view: View) -> Unit)? = null

    private var curSelectedItem: GalleryItem? = null

    fun setData(data: List<GalleryItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun selectItem(item: GalleryItem) {
        curSelectedItem?.let {
            // deselect currently selected item
            val position = data.indexOfFirst { i -> i.url == it.url }
            notifyItemChanged(position)
        }

        // find position of newly selected item and update it
        val newItemPosition = data.indexOfFirst { i -> i.url == item.url }
        curSelectedItem = item
        notifyItemChanged(newItemPosition)
    }

    fun setItemClickListener(listener: (item: GalleryItem, position: Int, view: View) -> Unit) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = data[position]

        holder.itemView.title.text = item.title

        val bgColorId = if (item.url == curSelectedItem?.url) R.color.gray_light else R.color.white
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, bgColorId))

        clickListener?.apply {
            holder.itemView.setOnClickListener {
                this.invoke(item, position, holder.itemView)
            }
        }

    }

    override fun getItemCount(): Int = data.size

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view)
}