package com.outcomehealth.challenge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.gallery_item.view.*

/**
 * Created by Grigory Azaryan on 10/10/20.
 */

class GalleryRecyclerAdapter : RecyclerView.Adapter<GalleryRecyclerAdapter.ItemViewHolder>() {
    private var data: List<GalleryItem> = emptyList()


    fun setData(data: List<GalleryItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = data[position]

        holder.itemView.title.text = item.title
    }

    override fun getItemCount(): Int = data.size

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view)
}