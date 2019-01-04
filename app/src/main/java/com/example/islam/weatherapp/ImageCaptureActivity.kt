package com.example.islam.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import com.example.islam.weatherapp.ImageDrawable.Companion.drawMultilineTextToBitmap
import com.example.islam.weatherapp.model.DayWeatherAPIResponse
import com.example.islam.weatherapp.model.StorageHandler
import com.example.islam.weatherapp.model.WeatherAPIService
import com.facebook.share.model.ShareContent
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_image_capture.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImageCaptureActivity : AppCompatActivity() {

    private val  REQUEST_IMAGE_CAPTURE = 1
    private var photoUri : Uri? = null
    var mCurrentPhotoPath: String=""
    private val REQUEST_TAKE_PHOTO: Int =1

    private val weatherAPIService by lazy {
        WeatherAPIService.create()
    }

    private var disposable: Disposable? = null
    private var latitude:Double = 0.0
    private var longitude:Double = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var dayWeatherAPIResponse:DayWeatherAPIResponse? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_capture)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        RxPermissions(this)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION , Manifest.permission.CAMERA)
                .subscribe { granted ->
                    if (granted) { // Always true pre-M
                        obtieneLocalizacion()
                    } else {
                        Toast.makeText(this,"couldn't grant location premission",Toast.LENGTH_SHORT).show()
                    }
                }

        dispatchTakePictureIntent()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            image_view.setImageURI(photoUri)
            if(dayWeatherAPIResponse != null)
                writeDataOnImage()
            else
                obtieneLocalizacion()

        }
    }
    @SuppressLint("MissingPermission")
    private fun obtieneLocalizacion(){
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    latitude = location?.latitude!!
                    longitude = location.longitude
                    getDayWeatherData()
                }
    }

    private fun writeDataOnImage(){
        val bitmapDrawable = image_view.getDrawable()as BitmapDrawable
        val string  = "temp = "+dayWeatherAPIResponse?.main?.temp+"\n"+dayWeatherAPIResponse?.name
        val bitmapEditedImage:Bitmap=drawMultilineTextToBitmap(this, bitmapDrawable.bitmap,string)
        image_view.setImageBitmap(bitmapEditedImage)
        prepareEditedImageToShare(bitmapEditedImage)
        writeEditedImageToStorage(bitmapEditedImage)
    }
    private fun prepareEditedImageToShare(bitmap: Bitmap){
        val sharePhoto:SharePhoto=SharePhoto.Builder().setBitmap(bitmap).build()
        val shareContent:SharePhotoContent=SharePhotoContent.Builder().addPhoto(sharePhoto).build()
        image_share_button.shareContent=shareContent
    }
    private fun writeEditedImageToStorage(bitmap: Bitmap){
        val storageHandler = StorageHandler()
        val directory:String=getExternalFilesDir(Environment.DIRECTORY_PICTURES).absolutePath
        val imageName:String=SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())+"edited"
        val quality=100
        val child="edited"
        Observable.just(storageHandler.writeImageToStorage(bitmap,directory,imageName,quality))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }
    private fun getDayWeatherData(){
        disposable=weatherAPIService.getTwoDayWeather(latitude,longitude,id="6c35d564f5156b9cd6aa2eba1f1bbb3b")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    response->
                    dayWeatherAPIResponse=response
                },{
                    error->
                    Log.e("erorr",error.message)
                })
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.islam.weatherapp.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    photoUri=photoURI
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

}
