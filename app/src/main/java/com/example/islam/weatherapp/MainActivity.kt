package com.example.islam.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.islam.weatherapp.model.WeatherAPIService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import com.tbruyelle.rxpermissions2.RxPermissions
import com.example.islam.weatherapp.model.DayWeatherAPIResponse
import com.example.islam.weatherapp.model.StorageHandler
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var storageHandler:StorageHandler
    private lateinit var directory:String
    private lateinit var imageRecyclerViewAdapter: ImageRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        RxPermissions(this)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.CAMERA
                        ,Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) { // Always true pre-M
                        fetchImagesFromStorage()
                    } else {
                        Toast.makeText(this,"couldn't grant location premission",Toast.LENGTH_SHORT).show()
                    }
                }
        storageHandler= StorageHandler()
        directory=getExternalFilesDir(Environment.DIRECTORY_PICTURES).absolutePath
        images_recycler_view.layoutManager=LinearLayoutManager(this)
        imageRecyclerViewAdapter= ImageRecyclerViewAdapter()
        images_recycler_view.adapter=imageRecyclerViewAdapter
        fab.setOnClickListener { view ->
            val intent:Intent=Intent(this,ImageCaptureActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchImagesFromStorage() {
        storageHandler.readFilesFromFolder(directory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it->Log.e("files",it.size.toString())
                    if(it.size>0){
                        imageRecyclerViewAdapter.setImageList(it)
                        no_history_tex_view.visibility= View.GONE
                    }
                },{
                    it->Log.e("error",it.message)
                })    }

    override fun onResume() {
        super.onResume()
        fetchImagesFromStorage()
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


}
