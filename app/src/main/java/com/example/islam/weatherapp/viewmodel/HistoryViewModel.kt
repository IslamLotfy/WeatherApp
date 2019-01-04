package com.example.islam.weatherapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.graphics.Bitmap
import android.net.Uri
import com.example.islam.weatherapp.model.storage.StorageHandler
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HistoryViewModel(): BaseViewModel() {
    private var storageHandler: StorageHandler = StorageHandler()
    private var mutableLiveData: MutableLiveData<MutableList<Uri>> = MutableLiveData()
    init {

    }

    private fun loadImages(path:String) {
        storageHandler.readFilesFromFolder(path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it->
                    mutableLiveData.value=it
                },{

                })
    }

    private fun writeImagesToStorage(bitmap: Bitmap, directory:String, imageName:String, quality:Int){
        Observable.just(storageHandler.writeImageToStorage(bitmap,directory,imageName,quality))
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({

                },{

                })
    }
    fun getImages(path: String):MutableLiveData<MutableList<Uri>>{
        loadImages(path)
        return mutableLiveData
    }

    fun writeImages(bitmap: Bitmap,directory:String , imageName:String,quality:Int){
        writeImagesToStorage(bitmap,directory,imageName,quality)
    }

}