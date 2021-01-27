package com.android.hackathon.movieapp.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.hackathon.movieapp.MainActivity
import com.android.hackathon.movieapp.R
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import java.io.ByteArrayOutputStream
import java.util.*


class ProfileActivity : AppCompatActivity() {
    val GALLERY_PICK = 1
    private val cameraRequest = 1888

    private val profileImage by lazy { findViewById<ImageView>(R.id.profile_image) }
    private val profileName by lazy { findViewById<TextView>(R.id.profile_name) }
    private val profileBalance by lazy { findViewById<TextView>(R.id.profile_balance) }

    private lateinit var userUid: String

    private lateinit var userDatabase: DatabaseReference
    private var currentUser: FirebaseUser? = null

    private lateinit var imageStorage: StorageReference

    private lateinit var bottomNavBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        bottomNavBar = findViewById(R.id.bottom_nav_bar)
        bottomNavBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> startActivity(Intent(this, MainActivity::class.java))
                R.id.nav_fav -> startActivity(Intent(this, FavoriteActivity::class.java))
            }
            true
        }
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                cameraRequest
            )
//        imageView = findViewById(R.id.imageView)
        profileImage.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, cameraRequest)
        }

        prepareFirebase()
    }

    private fun prepareFirebase() {
        imageStorage = FirebaseStorage.getInstance().reference

//        profileImage.setOnClickListener {
//            val galleryIntent = Intent()
//            galleryIntent.type = "image/*"
//            galleryIntent.action = Intent.ACTION_GET_CONTENT
//
//            startActivityForResult(
//                Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK
//            )
//        }
        currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LogInActivity::class.java))
            return
        }
        userUid = currentUser!!.uid

        userDatabase = FirebaseDatabase.getInstance().reference.child("users").child(userUid)
        userDatabase.keepSynced(true)

        userDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").value as String?
                val image = snapshot.child("image").value as String?
                val balance = snapshot.child("balance").value as String?
                if (image != "default") {
//                    Log.v("Tag", snapshot.child("image").toString())
//                    Glide.with(applicationContext).load(snapshot.child("image")).into(profileImage)
                    //Picasso.get().load(image).placeholder(R.drawable.settings_default_image).into(userImageView);
                    Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.ic_person)
                        .into(profileImage, object : Callback {
                            override fun onSuccess() {}
                            override fun onError(e: java.lang.Exception?) {
                                Picasso.get().load(image)
                                    .placeholder(R.drawable.ic_person)
                                    .into(profileImage)
                            }
                        })
                }
                profileName.text = name
                profileBalance.text = balance
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getImageUri(inContext: Context?, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            this.contentResolver,
            inImage,
            UUID.randomUUID().toString().toString() + ".png",
            "drawing"
        )
        return Uri.parse(path)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == cameraRequest) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            profileImage.setImageBitmap(photo)
            uploadImage(photo)
        }

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            val imageUri = data!!.data
            CropImage.activity(imageUri) //.setAspectRatio(1,1)
                .start(this)
        }
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
//            if (resultCode == RESULT_OK) {
//                val resultUri: Uri = result.uri
//                uploadImage(resultUri)
//                uploadThumbImage(resultUri)

//               filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            //               ---------- ?? ----------               //
//                            String downloadUri = task.getResult().getUploadSessionUri().toString();//.getDownloadUrl().toString();
//                            userDatabase.child("image").setValue(downloadUri).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(SettingActivity.this, "Success uploading", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            });
//
//                        } else {
//                            Toast.makeText(SettingActivity.this, "not working", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                val error: Exception = result.getError()
//            }
//        }
    }

//    private fun uploadThumbImage(resultUri: Uri) {
//        val thumb_filepath: StorageReference = imageStorage.child("profile_images").child("thumbs")
//            .child(currentUser.getUid() + ".jpg")
//        val thumb_bytes = convertToBitmap(resultUri)
//        val uploadBitmap = thumb_filepath.putBytes(thumb_bytes)
//        val taskBitmap: Task<Uri> = uploadBitmap.continueWithTask(
//            Continuation<Any?, Task<Uri?>?> { task ->
//                if (!task.isSuccessful) throw task.exception!!
//                thumb_filepath.getDownloadUrl()
//            }).addOnCompleteListener(OnCompleteListener<Uri> { task ->
//            val downloadUrl = task.result.toString()
//            userDatabase.child("thumb_image").setValue(downloadUrl).addOnCompleteListener(
//                OnCompleteListener<Void?> { task ->
//                    if (task.isSuccessful) {
//                        Toast.makeText(
//                            this,
//                            "Success uploading thumb image",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    } else {
//                        Toast.makeText(this, "not working", Toast.LENGTH_LONG)
//                            .show()
//                    }
//                })
//        })
//    }

    private fun uploadImage(bitmap: Bitmap) {
        val filePath = imageStorage.child("profile_images").child(currentUser!!.uid + ".jpg")
        val uploadTask = getImageUri(this, bitmap)?.let { filePath.putFile(it) }
        val urlTask = uploadTask?.continueWithTask { task ->
            if (!task.isSuccessful) throw task.exception!!
            filePath.downloadUrl
        }?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result.toString()
                userDatabase.child("image").setValue(downloadUrl).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Success uploading", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

//    private suspend fun convertToBitmap(resultUri: Uri): ByteArray {
//        val thumb_filePath = File(resultUri.path)
//        val bitmap = Compressor.compress(this, thumb_filePath) {
//            default(width = 200, height = 200, quality = 75, format = Bitmap.CompressFormat.JPEG)
//        }
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        return baos.toByteArray()
//    }
}