package com.frogdevelopment.nihongo.dico.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider
import com.frogdevelopment.nihongo.dico.databinding.HistoryFragmentBinding
import java.time.Instant

class HistoryFragment : Fragment(), HistoryAdapter.OnHistoryClickListener {

    private lateinit var viewModel: HistoryViewModel

    private var _binding: HistoryFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HistoryFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.historyRecyclerview.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
//        favoritesViewModel.getFavorites().observe(viewLifecycleOwner, { favorites -> onSearchFinished(favorites) })

        val projection = arrayOf("display1", "display2", "date")
        val cursor = requireContext().contentResolver.query(
            NihonGoDicoContentProvider.TEST_URI,
            projection,
            null,
            null,
            "date DESC"
        )

        cursor?.apply {
            val indexText1: Int = getColumnIndex("display1")
            val indexText2: Int = getColumnIndex("display2")
            val indexDate: Int = getColumnIndex("date")
            val items = ArrayList<History>()
            while (moveToNext()) {
                val text1 = getString(indexText1)
                val text2 = getString(indexText2)

                val date = Instant.ofEpochMilli(getLong(indexDate))
                items.add(History(date.toString(), text1, text2))
            }

            close()

            onSearchFinished(items)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onSearchFinished(items: List<History>) {
        if (items.isEmpty()) {
            binding.historyRecyclerview.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.historyRecyclerview.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
            binding.historyRecyclerview.adapter = HistoryAdapter(requireContext(), items, this)
        }
    }

    override fun onHistoryClick(text: String) {
        TODO("Not yet implemented")
    }

}