package com.example.islam.weatherapp

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_recycler_view_item.view.*

class ImageRecyclerViewAdapter() : RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageItemViewHolder>() {
    class ImageItemViewHolder(var view: View ) : RecyclerView.ViewHolder(view ) {
        fun bindDataToView(imageUri: Uri){
            Picasso.get().load(imageUri).into(view.image_view_item);

        }
    }

    private var imageList:MutableList<Uri>
    init {
        imageList= mutableListOf()
    }
    fun setImageList(images:MutableList<Uri>){
        imageList=images
        this.notifyDataSetChanged()
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