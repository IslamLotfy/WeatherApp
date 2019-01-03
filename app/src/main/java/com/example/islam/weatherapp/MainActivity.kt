package com.example.islam.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.islam.weatherapp.model.WeatherAPIService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_main.*
import com.tbruyelle.rxpermissions2.RxPermissions
import android.text.Layout
import android.text.StaticLayout
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.text.TextPaint
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData
import kotlinx.android.synthetic.main.content_main.*
import rx_activity_result2.RxActivityResult
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    private val weatherAPIService by lazy {
        WeatherAPIService.create()
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude:Double = 0.0
    private var longitude:Double = 0.0
    var mCurrentPhotoPath: String=""
    private val  REQUEST_IMAGE_CAPTURE = 1
    private var photoUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        RxPaparazzo.register(this.application)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        RxPermissions(this)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION , Manifest.permission.CAMERA)
                .subscribe({ granted ->
                    if (granted) { // Always true pre-M
                        obtieneLocalizacion()
                    } else {
                        Toast.makeText(this,"couldn't grant location premission",Toast.LENGTH_SHORT).show()
                    }
                })
        fab.setOnClickListener { view ->
            dispatchTakePictureIntent33()
        }
    }

    private val REQUEST_TAKE_PHOTO: Int =1

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            image_view.setImageURI(photoUri)
            val bitmapDrawable = image_view.getDrawable()as BitmapDrawable


            image_view.setImageBitmap(drawMultilineTextToBitmap(this, bitmapDrawable.bitmap,"OK\nokkk"))
        }
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

    private fun dispatchTakePictureIntent33() {
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


    @SuppressLint("MissingPermission")
    private fun obtieneLocalizacion(){
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    latitude = location?.latitude!!
                    longitude = location.longitude
                    getDayWeatherData()
                }
    }

    private fun getDayWeatherData(){
        disposable=weatherAPIService.getTwoDayWeather(latitude,longitude,id="6c35d564f5156b9cd6aa2eba1f1bbb3b")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    response->Toast.makeText(this,response.main.temp.toString(),Toast.LENGTH_SHORT).show()
                },{
                    error->
                    Log.e("erorr",error.message)
                })
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun drawMultilineTextToBitmap(gContext: Context,
                                  bitmapParam: Bitmap,
                                  gText: String): Bitmap {

        // prepare canvas
        val resources = gContext.getResources()
        val scale = resources.getDisplayMetrics().density
        var bitmap=bitmapParam
        var bitmapConfig: android.graphics.Bitmap.Config? = bitmap.config
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888
        }
        bitmap = bitmap.copy(bitmapConfig, true)

        val canvas = Canvas(bitmap)

        val paint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        paint.setColor(Color.rgb(120, 61, 120))
        paint.setTextSize((140 * scale).toInt().toFloat())
        paint.setShadowLayer(1f, 1f, 1f, Color.WHITE)

        val textWidth = canvas.getWidth() - (160 * scale).toInt()

        // init StaticLayout for text
        val textLayout = StaticLayout(
                gText, paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false)

        // get height of multiline text
        val textHeight = textLayout.height

        // get position of text's top left corner
        val x = ((bitmap.width - textWidth) ).toFloat()
        val y = ((bitmap.height - textHeight) ).toFloat()

        // draw text to the Canvas center
        canvas.save()
        canvas.translate(x, y)
        textLayout.draw(canvas)
        canvas.restore()

        return bitmap
    }
}
