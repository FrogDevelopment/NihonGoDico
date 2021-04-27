package com.frogdevelopment.nihongo.dico.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.frogdevelopment.nihongo.dico.R
import com.frogdevelopment.nihongo.dico.databinding.DefaultActivityBinding

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DefaultActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.container, SettingsFragment::class.java, null)
                setReorderingAllowed(true)
            }
        }

        setSupportActionBar(binding.bottomAppBar)

        binding.title.text = getText(R.string.settings_title)
    }
}