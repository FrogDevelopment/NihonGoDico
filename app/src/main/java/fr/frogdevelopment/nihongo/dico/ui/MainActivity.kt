package fr.frogdevelopment.nihongo.dico.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton.OnVisibilityChangedListener
import fr.frogdevelopment.nihongo.dico.R
import fr.frogdevelopment.nihongo.dico.databinding.MainActivityBinding
import fr.frogdevelopment.nihongo.dico.ui.BottomNavigationDrawerFragment.OnNavigateToListener
import fr.frogdevelopment.nihongo.dico.ui.search.BottomSheetSearchFragment
import fr.frogdevelopment.nihongo.dico.ui.search.SearchFragment
import fr.frogdevelopment.nihongo.dico.ui.settings.SettingsActivity

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
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, SearchFragment.newInstance())
                    .commit()
            supportFragmentManager
                    .addOnBackStackChangedListener { switchFaB() }
        }

        isSearching = true
        binding.fab.setOnClickListener {
            if (isSearching) {
                val bottomSheetSearchFragment = BottomSheetSearchFragment()
                bottomSheetSearchFragment.show(supportFragmentManager, bottomSheetSearchFragment.tag)
            }
        }

        bottomNavigationDrawerFragment = BottomNavigationDrawerFragment(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (isSearching) {
                bottomNavigationDrawerFragment.show(supportFragmentManager, bottomNavigationDrawerFragment.tag)
            } else {
                switchFaB()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun navigateTo(itemId: Int) {
        when (itemId) {
            R.id.nav_settings -> {
                binding.fab.hide()
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            R.id.nav_favorites -> {
                binding.fab.hide()
                Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show()
            }
            else -> {
                binding.fab.show()
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_container, SearchFragment.newInstance())
                        .commitNow()
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        binding.fab.show()
    }

    private val onVisibilityChangedListener: OnVisibilityChangedListener = object : OnVisibilityChangedListener() {

        override fun onHidden(fab: FloatingActionButton) {
            if (isSearching) {
                switchToDetails()
            } else {
                switchToSearch()
            }
            fab.show()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun slideUpBottomBar() {
        binding.bottomAppBar.performShow()
        val layoutParams = binding.fab.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = layoutParams.behavior as HideBottomViewOnScrollBehavior<FloatingActionButton>?
        behavior!!.slideUp(binding.fab)
    }

    private fun switchFaB() {
        slideUpBottomBar()
        binding.fab.hide(onVisibilityChangedListener)
    }

    private fun switchToDetails() {
        binding.bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        binding.fab.setImageResource(R.drawable.ic_baseline_record_voice_over_24)
        isSearching = false
    }

    private fun switchToSearch() {
        binding.bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
        binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        binding.fab.setImageResource(R.drawable.ic_baseline_search_24)
        isSearching = true
        supportFragmentManager.popBackStackImmediate()
    }
}