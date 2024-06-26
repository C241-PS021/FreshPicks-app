package com.mobile.freshpicks.view.detect

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.toAndroidPair
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
        val splitText = splitCamelCase(textLabel!!)

        showResult(splitText, imageUri)

        binding.backToHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.saveResult.setOnClickListener {
            val fruitName = splitText.second
            val score = textScore * 100
            val scanResult = "${splitText.first} (${score.format(1)}%)"

            saveResult(imageUri, fruitName, scanResult)
        }

        viewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Result saved!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to save result", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showResult(splitText: Pair<String, String>, imageUri: Uri){
        binding.ivPreview.setImageURI(imageUri)
        binding.scanResultTV.text = binding.root.context.getString(R.string.fresh, splitText.first)
        if (splitText.first == "Fresh") {
            val color = Color.parseColor("#B0FC38")
            binding.scanResultTV.backgroundTintList = ColorStateList.valueOf(color)
            binding.tvResult.text = binding.root.context.getString(R.string.detection_result_description_fresh, splitText.second, splitText.first)
        } else if (splitText.first == "Rotten") {
            val color = Color.parseColor("#FF2400")
            binding.scanResultTV.backgroundTintList = ColorStateList.valueOf(color)
            binding.tvResult.text = binding.root.context.getString(R.string.detection_result_description_rotten, splitText.second, splitText.first)
        }
    }

    private fun splitCamelCase(input: String): Pair<String, String> {
        val parts = input.split("(?=[A-Z])".toRegex())
        return Pair(parts[1], parts[2])
    }

    private fun Float.format(digits: Int) = "%.${digits}f".format(this)

    private fun saveResult(imageUri: Uri?, fruitNameString: String, scanResultString: String) {
        val file = imageUri?.let { uri ->
            val tempFile = getFileFromUri(uri)?.reduceFileImage()
            tempFile?.let {
                val requestFile = it.asRequestBody("image/jpeg".toMediaType())
                MultipartBody.Part.createFormData("image", it.name, requestFile)
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