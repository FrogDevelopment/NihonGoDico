package fr.frogdevelopment.nihongo.dico.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.frogdevelopment.nihongo.dico.R
import fr.frogdevelopment.nihongo.dico.databinding.SettingsActivityBinding

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.settings_container, SettingsFragment.newInstance())
                    .commitNow()
        }
        setSupportActionBar(binding.bottomAppBar)
    }
}