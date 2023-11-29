package com.srhan.easymarkt.ui.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.srhan.easymarkt.R
import com.srhan.easymarkt.adapters.BestProductsAdapter
import com.srhan.easymarkt.adapters.OfferProductsAdapter
import com.srhan.easymarkt.databinding.FragmentBaseCategoryBinding
import com.srhan.easymarkt.util.Constants
import com.srhan.easymarkt.util.showBottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseCategoryFragment : Fragment() {
    private lateinit var binding: FragmentBaseCategoryBinding
    protected val offerAdapter: OfferProductsAdapter by lazy { OfferProductsAdapter() }
    protected val bestProductsAdapter: BestProductsAdapter by lazy { BestProductsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOfferRv()
        setupBestProductsRv()

        bestProductsAdapter.onClick = {
            val bundle = Bundle().apply {
                putParcelable(Constants.PRODUCT_KEY, it)
            }

            findNavController().navigate(
                R.id.action_homeFragment_to_productDetailsFragment, bundle
            )
        }

        offerAdapter.onClick = {
            val bundle = Bundle().apply {
                putParcelable(Constants.PRODUCT_KEY, it)
            }

            findNavController().navigate(
                R.id.action_homeFragment_to_productDetailsFragment, bundle
            )
        }

    }

    fun showOfferLoading() {
        binding.offerProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideOfferLoading() {
        binding.offerProductsProgressBar.visibility = View.GONE
    }

    fun showBestProductsLoading() {
        binding.bestProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideBestProductsLoading() {
        binding.bestProductsProgressBar.visibility = View.GONE
    }


    private fun setupBestProductsRv() {
        binding.rvBestProducts.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductsAdapter
        }
    }

    private fun setupOfferRv() {
        binding.rvOfferProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offerAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }

}