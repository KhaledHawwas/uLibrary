package com.hawwas.ulibrary.ui.settings

import android.content.*
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hawwas.ulibrary.R
import com.hawwas.ulibrary.databinding.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.domain.repo.LocalStorage.Companion.rootDir
import com.hawwas.ulibrary.ui.chooser.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class SettingActivity: AppCompatActivity() {
   private lateinit var binding: ActivitySettingBinding
   @Inject lateinit var localStorage: LocalStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.rootDirTv.text= localStorage.getAppDir()+"/"+rootDir
        binding.headersBtn.setOnClickListener {
            Intent(this, SubjectChooserActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}