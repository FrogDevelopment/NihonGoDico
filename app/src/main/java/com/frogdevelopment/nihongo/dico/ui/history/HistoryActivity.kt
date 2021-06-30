package com.frogdevelopment.nihongo.dico.ui.history

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.frogdevelopment.nihongo.dico.R
import com.frogdevelopment.nihongo.dico.databinding.DefaultActivityBinding

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DefaultActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.bottomAppBar)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.container, HistoryFragment::class.java, null)
                setReorderingAllowed(true)
            }
        }

        binding.title.text = getText(R.string.history_title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}