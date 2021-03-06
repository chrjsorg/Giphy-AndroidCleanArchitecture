package de.abauer.giphy_clean_architecture.presentation.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import de.abauer.giphy_androidcleanarchitecture.R
import de.abauer.giphy_androidcleanarchitecture.databinding.SearchFragmentBinding
import de.abauer.giphy_clean_architecture.domain.model.Giphy
import de.abauer.giphy_clean_architecture.presentation.MainActivity
import io.uniflow.androidx.flow.onStates
import org.koin.android.viewmodel.ext.android.viewModel
import viewLifecycleLazy

class SearchGiphysFragment : Fragment(R.layout.search_fragment), SearchClickListener {

    private val searchViewModel: SearchGiphysViewModel by viewModel()
    private val binding by viewLifecycleLazy {  SearchFragmentBinding.bind(requireView()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity!! as MainActivity).supportActionBar!!.title = getString(R.string.searchTitle)
        initSearch()
        initRecyclerView()
        initStateHandling()
    }

    private fun initStateHandling() {
        onStates(searchViewModel) { state ->
            when (state) {
                is SearchState.Loading -> {
                    clearAdapter()
                    showLoading()
                }
                is SearchState.ShowSuccess -> {
                    hideLoading()
                    showSearchResult(state.searchResultGiphys)
                }
                is SearchState.ShowError -> {
                    clearAdapter()
                    hideLoading()
                    showError()
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewSearchFragment.layoutManager = GridLayoutManager(context, 2)
        val searchAdapter = SearchAdapter(emptyList())
        searchAdapter.searchClickListener = this
        binding.recyclerViewSearchFragment.adapter = searchAdapter
    }

    private fun initSearch() {
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(
                fieldInput: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (fieldInput.length > 2)
                    searchViewModel.onSearchInput(fieldInput.toString())
            }
        })
    }

    private fun showSearchResult(searchResults: List<Giphy>) {
        (binding.recyclerViewSearchFragment.adapter as SearchAdapter).apply {
            searchResultGiphys = searchResults
            notifyDataSetChanged()
        }
    }

    private fun clearAdapter() {
        (binding.recyclerViewSearchFragment.adapter as SearchAdapter).apply {
            searchResultGiphys = emptyList()
            notifyDataSetChanged()
        }
    }

    private fun showError() {
        Toast.makeText(context, getString(R.string.trending_giphys_error), Toast.LENGTH_LONG).show()
    }

    private fun showLoading() {
        binding.spinKitSearch.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.spinKitSearch.visibility = View.GONE
    }

    override fun onSearchItemClick(giphyUrl: String, imageView: ImageView) {
        val action = SearchGiphysFragmentDirections.actionSearchGiphyFragmentToGiphyDetailFragment(giphyUrl)
        findNavController().navigate(action)
    }
}
