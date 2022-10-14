package com.binar.latihan_datastore_login.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.binar.latihan_datastore_login.data.LoginDataStoreManager
import com.binar.latihan_datastore_login.databinding.ActivityMainBinding
import com.binar.latihan_datastore_login.util.ViewModelFactory
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel
    private lateinit var pref: LoginDataStoreManager

    private val REQUEST_CODE_PERMISSION = 3
    private val GALLERY_RESULT_CODE = 15

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            viewModel.setProfileImage(result.toString())
        }

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                handleCameraImage(result.data)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        initView()
        setOnClickListener()
    }

    private fun setupViewModel() {
        pref = LoginDataStoreManager(this)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]
    }

    private fun initView() {
        viewModel.getUser().observe(this) {
            binding.tvUsername.text = it.username
        }
        viewModel.getUserProfileImage().observe(this) {
            Log.d("profile", "observe $it")
            if (it.isNullOrEmpty().not()) {
                setProfileImage(convertStringToBitmap(it))
            }
        }
    }

    private fun setProfileImage(bitmap: Bitmap) {
        binding.ivProfileImage.setImageBitmap(bitmap)
    }

    private fun convertStringToBitmap(string: String): Bitmap {
        val imageBytes = Base64.decode(string, 0)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun setOnClickListener() {
        binding.btnUploadProfileImage.setOnClickListener {
            checkPermissions()
        }
    }

    private fun checkPermissions() {
        if (isGranted(
                this,
                Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION
            )) {
            //openGallery()
            openCamera()
        }
    }

    private fun openGallery() {
        val imageIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        imageIntent.type = "image/*"
        imageIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(imageIntent, GALLERY_RESULT_CODE)
        //galleryResult.launch(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_RESULT_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val profileImage = data.data
            viewModel.setProfileImage(profileImage.toString())
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResult.launch(cameraIntent)
    }

    private fun handleCameraImage(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap
        Log.d("profile", "set ${bitmap}")
        viewModel.setProfileImage(bitMapToString(bitmap))
    }

    private fun bitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun isGranted(
        activity: Activity,
        permission: String, //for camera
        permissions: Array<String>, //for read write storage/gallery
        request: Int
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }

    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission denied")
            .setMessage("Permission is denied, Please allow app permission from App Settings")
            .setPositiveButton("App Settings") { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}