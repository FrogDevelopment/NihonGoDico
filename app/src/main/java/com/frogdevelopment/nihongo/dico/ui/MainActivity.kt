package com.frogdevelopment.nihongo.dico.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.frogdevelopment.nihongo.dico.R
import com.frogdevelopment.nihongo.dico.databinding.MainActivityBinding
import com.frogdevelopment.nihongo.dico.ui.BottomNavigationDrawerFragment.OnNavigateToListener
import com.frogdevelopment.nihongo.dico.ui.favorites.FavoritesActivity
import com.frogdevelopment.nihongo.dico.ui.search.BottomSheetSearchFragment
import com.frogdevelopment.nihongo.dico.ui.search.SearchFragment
import com.frogdevelopment.nihongo.dico.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity(), OnNavigateToListener {

    private var isSearching = false
    private lateinit var binding: MainActivityBinding
    private lateinit var bottomNavigationDrawerFragment: BottomNavigationDrawerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.bottomAppBar)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.main_container, SearchFragment::class.java, null)
                setReorderingAllowed(true)
            }
            supportFragmentManager.addOnBackStackChangedListener { switchFaB() }
        }

        isSearching = true
        binding.fab.setOnClickListener {
            val bottomSheetSearchFragment = BottomSheetSearchFragment()
            bottomSheetSearchFragment.show(supportFragmentManager, bottomSheetSearchFragment.tag)
        }

        bottomNavigationDrawerFragment = BottomNavigationDrawerFragment(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (isSearching) {
                bottomNavigationDrawerFragment.show(supportFragmentManager, bottomNavigationDrawerFragment.tag)
            } else {
                onBackPressed()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun navigateTo(itemId: Int) {
        when (itemId) {
            R.id.nav_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.nav_favorites -> startActivity(Intent(this, FavoritesActivity::class.java))
            R.id.nav_web -> {
                val url = "https://www.nihongo-dico.frog-development.com"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
            else -> {
                // already on search, nothing to do
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun switchFaB() {
        if (isSearching) {
            switchToDetails()
        } else {
            switchToSearch()
        }
    }

    private fun switchToDetails() {
        binding.bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.fab.hide()
        isSearching = false
    }

    private fun switchToSearch() {
        binding.bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
        binding.fab.show()
        isSearching = true
    }
}