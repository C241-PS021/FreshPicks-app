package com.mobile.freshpicks.view.detect

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobile.freshpicks.view.main.MainActivity
import com.mobile.freshpicks.R
import com.mobile.freshpicks.databinding.ActivityAnalyzeResultBinding
import java.text.NumberFormat

class AnalyzeResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalyzeResultBinding

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

        }
    }
}