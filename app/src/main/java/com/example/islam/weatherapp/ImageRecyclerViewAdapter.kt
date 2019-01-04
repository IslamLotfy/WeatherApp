package com.example.islam.weatherapp

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.image_recycler_view_item.view.*

class ImageRecyclerViewAdapter(private val imageList:MutableList<Bitmap>) : RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageItemViewHolder>() {
    class ImageItemViewHolder(var view: View ) : RecyclerView.ViewHolder(view ) {
        fun bindDataToView(bitmap: Bitmap){
            view.image_view_item.setImageBitmap(bitmap)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemViewHolder {
        return ImageItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.image_recycler_view_item,parent,false))
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(imageItemViewHolder: ImageItemViewHolder, itemIndex: Int) {
        imageItemViewHolder.bindDataToView(imageList[itemIndex])
    }
}