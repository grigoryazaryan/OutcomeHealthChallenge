package com.outcomehealth.challenge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.outcomehealth.challenge.R
import com.outcomehealth.challenge.data.GalleryItem
import kotlinx.android.synthetic.main.gallery_item.view.*

/**
 * Created by Grigory Azaryan on 10/10/20.
 */

class GalleryRecyclerAdapter : RecyclerView.Adapter<GalleryRecyclerAdapter.ItemViewHolder>() {
    private var data: List<GalleryItem> = emptyList()

    private var clickListener: ((item: GalleryItem, position: Int, view: View) -> Unit)? = null

    fun setData(data: List<GalleryItem>) {
        this.data = data
        notifyDataSetChanged()
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

        clickListener?.apply {
            holder.itemView.setOnClickListener {
               this.invoke(item, position, holder.itemView)
            }
        }

    }

    override fun getItemCount(): Int = data.size

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view)
}