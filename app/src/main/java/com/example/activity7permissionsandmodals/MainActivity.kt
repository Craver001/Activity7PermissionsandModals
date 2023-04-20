package com.example.activity7permissionsandmodals

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.activity7permissionsandmodals.ViewModel.SubjectList
import com.example.activity7permissionsandmodals.ViewModel.SubjectViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    // Request code for camera permission
    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    // Request code for image capture
    private val REQUEST_IMAGE_CAPTURE = 1


    private lateinit var imageProfile: CircleImageView
    lateinit var subjectList:SubjectList


    private lateinit var imageUri: Uri

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                imageProfile.setImageURI(imageUri)
                imageProfile.setBackgroundResource(0) // remove background
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_info)



        imageProfile = findViewById(R.id.profile_image)

        subjectList = SubjectList()

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SubjectViewHolder(subjectList.list)

        imageProfile.setOnClickListener {
            showDialogBox()

        }

    }

    fun showDialogBox() {
        val dialogView = layoutInflater.inflate(R.layout.dialog, null)
        val camera = dialogView.findViewById<Button>(R.id.camera)
        val gallery = dialogView.findViewById<Button>(R.id.gallery)

        val builder = AlertDialog.Builder(this)
            .setView(dialogView)

        val alertDialog = builder.create()



        camera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), CAMERA_PERMISSION_REQUEST_CODE)
            } else {
                dispatchTakePictureIntent()
            }
        }

        gallery.setOnClickListener {
            // Add your code here to handle the square button click
            openGallery()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras = data?.extras
            val imageBitmap = extras?.get("data") as Bitmap
            imageProfile.setImageBitmap(imageBitmap)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent()
                } else {
                    // Permission denied, show message or handle it in some other way
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    private fun enableCameraButton() {
        imageProfile.isEnabled = true
        imageProfile.isClickable = true
        imageProfile.alpha = 1.0f
    }

    private fun openGallery() {
        getContent.launch("image/*")
    }


}