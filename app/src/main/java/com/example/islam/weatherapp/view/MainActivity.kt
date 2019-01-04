package com.example.islam.weatherapp.view

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.islam.weatherapp.R
import kotlinx.android.synthetic.main.activity_main.*
import com.tbruyelle.rxpermissions2.RxPermissions
import com.example.islam.weatherapp.viewmodel.StorageViewModel
import com.example.islam.weatherapp.viewmodel.StorageViewModelFactory
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

   // private lateinit var storageHandler:StorageHandler
    private lateinit var storageViewModel: StorageViewModel
    private lateinit var directory:String
    private lateinit var imageRecyclerViewAdapter: ImageRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        storageViewModel=ViewModelProviders.of(this, StorageViewModelFactory()).get(StorageViewModel::class.java)
        RxPermissions(this)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.CAMERA
                        ,Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe( { granted ->
                    if (granted) { // Always true pre-M
                        fetchImagesFromStorage()
                    } else {
                        Toast.makeText(this,"couldn't grant location premission",Toast.LENGTH_SHORT).show()
                    }},{
                    it->Log.e("error",it.cause.toString())
                })
        //storageHandler= StorageHandler()

        directory=getExternalFilesDir(Environment.DIRECTORY_PICTURES).absolutePath
        images_recycler_view.layoutManager=LinearLayoutManager(this)
        imageRecyclerViewAdapter= ImageRecyclerViewAdapter()
        images_recycler_view.adapter=imageRecyclerViewAdapter
        fab.setOnClickListener {
            val intent:Intent=Intent(this, ImageCaptureActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchImagesFromStorage() {
        storageViewModel.getImages(directory).observe(this, Observer {
            it->imageRecyclerViewAdapter.setImageList(it!!)
        })
//        storageHandler.readFilesFromFolder(directory)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    it->Log.e("files",it.size.toString())
//                    if(it.size>0){
//                        imageRecyclerViewAdapter.setImageList(it)
//                        no_history_tex_view.visibility= View.GONE
//                    }
//                },{
//                    it->Log.e("error",it.message)
//                })
    }

//    override fun onResume() {
//        super.onResume()
//        fetchImagesFromStorage()
//    }

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
