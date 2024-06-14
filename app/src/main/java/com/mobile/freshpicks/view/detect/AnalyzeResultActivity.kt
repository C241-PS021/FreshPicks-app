package com.mobile.freshpicks.view.detect

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
    }
}