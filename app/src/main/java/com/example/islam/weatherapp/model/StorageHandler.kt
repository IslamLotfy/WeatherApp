package com.example.islam.weatherapp.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
    fun readFilesFromFolder(folderPath: String):Observable<MutableList<Uri>>{
        val file= File(folderPath)
        var files:List<File>
        var uriList:MutableList<Uri> = mutableListOf()
        if(file.exists()){
            files = file.listFiles { _, name -> (name.endsWith(".jpg")
                    || name.endsWith(".jpeg")
                    || name.endsWith(".png"))
                    && name.contains("edited")}.toList()
            files.forEach{it->
                uriList.add(Uri.fromFile(it))
            }
            return Observable.fromArray(uriList)

        }

        return Observable.fromArray(uriList)
    }
}