package com.mobile.freshpicks

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.mobile.freshpicks.databinding.ActivityMainBinding
import com.mobile.freshpicks.view.detect.AnalyzeOptionActivity
import com.mobile.freshpicks.view.home.HomeFragment
import com.mobile.freshpicks.view.savedresult.SavedResultFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.navView.background = null
        replaceFragment(HomeFragment())

        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> replaceFragment(HomeFragment())
                R.id.navigation_saved -> replaceFragment(SavedResultFragment())

                else -> {}
            }
            true
        }

        binding.fabDetect.setOnClickListener {
            val intent = Intent(this@MainActivity, AnalyzeOptionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}