package com.srhan.easymarkt.ui.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.srhan.easymarkt.R
import com.srhan.easymarkt.adapters.BestDealsAdapter
import com.srhan.easymarkt.adapters.BestProductsAdapter
import com.srhan.easymarkt.adapters.SpecialProductsAdapter
import com.srhan.easymarkt.databinding.FragmentMainCategoryBinding
import com.srhan.easymarkt.util.Constants.Companion.PRODUCT_KEY
import com.srhan.easymarkt.util.Resource
import com.srhan.easymarkt.util.showBottomNavigationView
import com.srhan.easymarkt.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainCategoryFragment : Fragment() {
    lateinit var binding: FragmentMainCategoryBinding
    lateinit var specialProductsAdapter: SpecialProductsAdapter
    lateinit var bestProductsAdapter: BestProductsAdapter
    lateinit var bestDealsAdapter: BestDealsAdapter

    private val viewModel by viewModels<MainCategoryViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSpecialProductRv()
        setUpBestProductRv()
        setUpBestDealsRv()

        lifecycleScope.launch {
            viewModel.specialProducts.collect {
                when (it) {
                    is Resource.Loading -> {
                        Log.d("LoadingSpesialProduct", it.message.toString())

                        showLoading()
                    }

                    is Resource.Success -> {
                        hideLoading()
                        Log.d("SuccessSpesialProduct", it.message.toString())

                        it.data?.let { productList ->
                            specialProductsAdapter.differ.submitList(productList)

                        }
                    }

                    is Resource.Error -> {
                        hideLoading()
                        Log.d("ErrorSpecialProduct", it.message.toString())
                        Toast.makeText(
                            requireContext(), "Error : ${it.message.toString()}", Toast.LENGTH_LONG
                        ).show()
                    }

                    else -> Unit
                }

            }
        }
        lifecycleScope.launch {
            viewModel.bestProducts.collect {
                when (it) {
                    is Resource.Loading -> {
                        Log.d("LoadingBestProduct", it.message.toString())

                        binding.bestProductsProgressbar.visibility = View.VISIBLE

                    }

                    is Resource.Success -> {
                        Log.d("SuccessBestProduct", it.message.toString())

                        binding.bestProductsProgressbar.visibility = View.GONE
                        it.data?.let { productList ->
                            bestProductsAdapter.differ.submitList(productList)

                        }
                    }

                    is Resource.Error -> {
                        binding.bestProductsProgressbar.visibility = View.GONE
                        Log.d("ErrorBestProduct", it.message.toString())
                        Toast.makeText(
                            requireContext(), "Error : ${it.message.toString()}", Toast.LENGTH_LONG
                        ).show()
                    }

                    else -> Unit
                }

            }
        }
        lifecycleScope.launch {
            viewModel.bestDeals.collect {
                when (it) {
                    is Resource.Loading -> {
                        Log.d("LoadingBestDeals", it.message.toString())

                        showLoading()

                    }

                    is Resource.Success -> {
                        Log.d("SuccessBestDeals", it.message.toString())

                        hideLoading()
                        it.data?.let { productList ->
                            bestDealsAdapter.differ.submitList(productList)

                        }
                    }

                    is Resource.Error -> {
                        hideLoading()
                        Log.d("ErrorBestDeals", it.message.toString())
                        Toast.makeText(
                            requireContext(), "Error : ${it.message.toString()}", Toast.LENGTH_LONG
                        ).show()
                    }

                    else -> Unit
                }

            }
        }

        binding.nestedScrollMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                viewModel.fetchBestProducts()
            }
        })

        specialProductsAdapter.onClick = {
            val bundle = Bundle().apply {
                putParcelable(PRODUCT_KEY, it)
            }

            findNavController().navigate(
                R.id.action_homeFragment_to_productDetailsFragment, bundle
            )

        }


        bestDealsAdapter.onClick = {
            val bundle = Bundle().apply {
                putParcelable(PRODUCT_KEY, it)
            }

            findNavController().navigate(
                R.id.action_homeFragment_to_productDetailsFragment, bundle
            )

        }

        bestProductsAdapter.onClick = {
            val bundle = Bundle().apply {
                putParcelable(PRODUCT_KEY, it)
            }

            findNavController().navigate(
                R.id.action_homeFragment_to_productDetailsFragment, bundle
            )

        }


    }

    private fun setUpBestDealsRv() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDealsProducts.apply {
            adapter = bestDealsAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpBestProductRv() {
        bestProductsAdapter = BestProductsAdapter()
        binding.rvBestProducts.apply {
            adapter = bestProductsAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun showLoading() {
        binding.mainCategoryProgressbar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.mainCategoryProgressbar.visibility = View.GONE


    }


    private fun setUpSpecialProductRv() {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            adapter = specialProductsAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }

}