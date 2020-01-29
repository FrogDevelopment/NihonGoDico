package fr.frogdevelopment.nihongo.dico.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import fr.frogdevelopment.nihongo.dico.R
import fr.frogdevelopment.nihongo.dico.databinding.BottomsheetFragmentBinding

class BottomNavigationDrawerFragment(private val onNavigateToListener: OnNavigateToListener?) : BottomSheetDialogFragment(), NavigationView.OnNavigationItemSelectedListener {

    private var _binding: BottomsheetFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.NavigationBottomSheet)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = BottomsheetFragmentBinding.inflate(layoutInflater)
        binding.navigationView.setNavigationItemSelectedListener(this)
        return binding.root
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        onNavigateToListener?.navigateTo(item.itemId)
        dismiss()
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnNavigateToListener {
        fun navigateTo(itemId: Int)
    }

}