package com.srhan.easymarkt.ui.fragments.shopping

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.srhan.easymarkt.R
import com.srhan.easymarkt.adapters.SearchRecyclerAdapter
import com.srhan.easymarkt.databinding.FragmentSearchBinding
import com.srhan.easymarkt.util.Resource
import com.srhan.easymarkt.util.showBottomNavigationView
import com.srhan.easymarkt.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    private lateinit var inputMethodManger: InputMethodManager
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var searchAdapter: SearchRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchRecyclerView()
        showKeyboardAutomatically()
        onHomeClick()

        searchProducts()
        lifecycleScope.launch {

            viewModel.search.collect { response ->
                when (response) {
                    is Resource.Loading -> {
                        Log.d("Search Loading", response.message.toString())

                    }

                    is Resource.Success -> {
                        val products = response.data
                        searchAdapter.differ.submitList(products)
                        showChancelTv()
                        Log.d("Search Success", response.data.toString())

                    }

                    is Resource.Error -> {
                        Log.d("Search Error", response.message.toString())
                        showChancelTv()

                    }

                    else -> Unit
                }
            }
        }


        onSearchTextClick()

        onCancelTvClick()


        binding.frameScan.setOnClickListener {

            Snackbar.make(
                requireView(),
                resources.getText(R.string.g_coming_soon),
                Snackbar.LENGTH_SHORT
            ).show()
        }
        binding.fragmeMicrohpone.setOnClickListener {
            Snackbar.make(
                requireView(),
                resources.getText(R.string.g_coming_soon),
                Snackbar.LENGTH_SHORT
            ).show()
        }

    }


    private fun onCancelTvClick() {
        binding.tvCancel.setOnClickListener {
            searchAdapter.differ.submitList(emptyList())
            binding.edSearch.setText("")
            hideCancelTv()
        }
    }

    private fun onSearchTextClick() {
        searchAdapter.onItemClick = { product ->
            val bundle = Bundle()
            bundle.putParcelable("product", product)

            /**
             * Hide the keyboard
             */

            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(requireView().windowToken, 0)

            findNavController().navigate(
                R.id.action_searchFragment_to_productDetailsFragment,
                bundle
            )

        }
    }

    private fun setupSearchRecyclerView() {
        searchAdapter = SearchRecyclerAdapter()
        binding.rvSearch.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }


    private fun observeSearch() {

    }

    var job: Job? = null
    fun searchProducts() {
        binding.edSearch.addTextChangedListener { query ->
            val queryTrim = query.toString().trim()
            if (queryTrim.isNotEmpty()) {
                val searchQuery = query.toString().substring(0, 1).toUpperCase()
                    .plus(query.toString().substring(1))
                job?.cancel()
                job = CoroutineScope(Dispatchers.IO).launch {
                    delay(500L)
                    viewModel.searchProducts(searchQuery)
                }
            } else {
                searchAdapter.differ.submitList(emptyList())
                hideCancelTv()
            }
        }


    }

    fun showChancelTv() {
        binding.tvCancel.visibility = View.VISIBLE
        binding.imgMic.visibility = View.GONE
        binding.imgScan.visibility = View.GONE
        binding.fragmeMicrohpone.visibility = View.GONE
        binding.frameScan.visibility = View.GONE

    }

    fun hideCancelTv() {
        binding.tvCancel.visibility = View.GONE
        binding.imgMic.visibility = View.VISIBLE
        binding.imgScan.visibility = View.VISIBLE
        binding.fragmeMicrohpone.visibility = View.VISIBLE
        binding.frameScan.visibility = View.VISIBLE
    }

    fun onHomeClick() {
        val btm = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        btm?.menu?.getItem(0)?.setOnMenuItemClickListener {
            activity?.onBackPressed()
            true
        }
    }

    private fun showKeyboardAutomatically() {
        inputMethodManger =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManger.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )

        binding.edSearch.requestFocus()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.edSearch.clearFocus()
    }

    override fun onResume() {
        super.onResume()

        showBottomNavigationView()
    }

}