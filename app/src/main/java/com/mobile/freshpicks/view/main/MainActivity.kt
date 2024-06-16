package com.mobile.freshpicks.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobile.freshpicks.R
import com.mobile.freshpicks.databinding.ActivityMainBinding
import com.mobile.freshpicks.helper.ViewModelFactory
import com.mobile.freshpicks.view.detect.AnalyzeOptionActivity
import com.mobile.freshpicks.view.home.HomeFragment
import com.mobile.freshpicks.view.login.LoginActivity
import com.mobile.freshpicks.view.savedresult.SavedResultFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

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
            startActivity(Intent(this@MainActivity, AnalyzeOptionActivity::class.java))
        }

        checkLogin()
    }

    private fun checkLogin() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}