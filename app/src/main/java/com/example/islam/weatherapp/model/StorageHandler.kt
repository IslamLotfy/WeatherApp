package com.example.islam.weatherapp.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import io.reactivex.Observable
import java.io.File
import java.io.FileOutputStream



class StorageHandler {

    init {

    }
    fun writeImageToStorage(bitmap: Bitmap,directory:String , imageName:String,quality:Int ){
        writeBitmap(bitmap,Bitmap.CompressFormat.JPEG,quality,directory,imageName )

    }
    private fun writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int,path:String,name:String ) {
        val folder=File(path)
        folder.mkdirs()
        val imageFile = File(folder, "$name.jpg")
        try {
            val os =  FileOutputStream(imageFile)
            bitmap.compress(format, quality, os)
            os.flush()
            os.close()
        }catch (e:Exception){
            Log.e("error in storage",e.cause.toString())
        }

    }
    fun readFilesFromFolder(folderPath: String):Observable<MutableList<Bitmap>>{
        val file:File= File(folderPath)
        var files:List<File>
        var bitmapList:MutableList<Bitmap> = mutableListOf()
        if(file.exists()){
            files = file.listFiles { _, name -> (name.endsWith(".jpg")
                    || name.endsWith(".jpeg")
                    || name.endsWith(".png"))
                    && name.contains("edited")}.toList()
            files.forEach{it->
                if(BitmapFactory.decodeFile(it.absolutePath) != null)
                    bitmapList.add(BitmapFactory.decodeFile(it.absolutePath))
            }
            return Observable.fromArray(bitmapList)

        }

        return Observable.fromArray(bitmapList)
    }
}