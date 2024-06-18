package com.mobile.freshpicks.view.detect

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mobile.freshpicks.view.main.MainActivity
import com.mobile.freshpicks.R
import com.mobile.freshpicks.databinding.ActivityAnalyzeResultBinding
import com.mobile.freshpicks.helper.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat

class AnalyzeResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalyzeResultBinding

    private val viewModel by viewModels<DetectViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val MAXIMAL_SIZE = 1000000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyzeResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri = Uri.parse(imageUriString)
        val textLabel = intent.getStringExtra("label")
        val textScore = intent.getFloatExtra("score", 0.0f)

        binding.ivPreview.setImageURI(imageUri)
        binding.tvResult.text = binding.root.context.getString(R.string.detection_result_description, NumberFormat.getPercentInstance().format(textScore), textLabel )
        binding.backToHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.saveResult.setOnClickListener {
            val splitText = splitCamelCase(textLabel!!)
            val fruitName = splitText.first
            val scanResult = "${splitText.second} (${textScore})"

            saveResult(imageUri, fruitName, scanResult)
        }
    }

    private fun splitCamelCase(input: String): Pair<String, String> {
        val parts = input.split("(?=[A-Z])".toRegex())
        return Pair(parts[0], parts[1])
    }

    private fun saveResult(imageUri: Uri?, fruitNameString: String, scanResultString: String) {
        val file = imageUri?.let { uri ->
            val tempFile = getFileFromUri(uri)?.reduceFileImage()
            tempFile?.let {
                val requestFile = it.asRequestBody("image/jpeg".toMediaType())
                MultipartBody.Part.createFormData("photo", it.name, requestFile)
            }
        }

        val fruitName: RequestBody = RequestBody.create(MultipartBody.FORM, fruitNameString)
        val scanResult: RequestBody = RequestBody.create(MultipartBody.FORM, scanResultString)

        if (file != null) {
            viewModel.uploadScanResult(file, fruitName, scanResult)
            Log.e("AddStoryActivity", "file tidak null")
        } else {
            Log.e("AddStoryActivity", "File is null")
        }

        viewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                Log.d("Upload story: ", "Errorrrrrr")
            }
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("temp", null, cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun File.reduceFileImage(): File {
        val file = this
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }
}