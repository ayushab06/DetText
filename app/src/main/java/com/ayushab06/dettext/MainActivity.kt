package com.ayushab06.dettext

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    private var imageBitMap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSnap.setOnClickListener {
            takePicture()
        }
        btnDetect.setOnClickListener {
            detectText()
        }
    }

    private fun detectText() {
        val image = FirebaseVisionImage.fromBitmap(imageBitMap!!)
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        detector.processImage(image).addOnSuccessListener { it ->
            processText(it)

        }.addOnFailureListener { Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show() }

    }

    private fun processText(text: FirebaseVisionText) {
        val blocks = text.textBlocks
        if (blocks.size== 0) {
            Toast.makeText(this, "No text found", Toast.LENGTH_SHORT).show()
            return
        }
        for(block in text.textBlocks){
            val txt=block.text
            tvText.text=txt
        }

    }

    private fun takePicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data!!.extras
            imageBitMap = extras!!.get("data") as Bitmap
            ivPic!!.setImageBitmap(imageBitMap)
        }
    }
}