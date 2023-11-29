package com.srhan.easymarkt.ui.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.srhan.easymarkt.R
import com.srhan.easymarkt.adapters.ColorsAdapter
import com.srhan.easymarkt.adapters.SizesAdapter
import com.srhan.easymarkt.adapters.ViewPager2ImagesAdapter
import com.srhan.easymarkt.data.models.CartProduct
import com.srhan.easymarkt.databinding.FragmentProductDetailsBinding
import com.srhan.easymarkt.util.Resource
import com.srhan.easymarkt.util.hideBottomNavigationView
import com.srhan.easymarkt.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.vejei.viewpagerindicator.indicator.CircleIndicator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPager2Adapter: ViewPager2ImagesAdapter by lazy { ViewPager2ImagesAdapter() }
    private val colorsAdapter: ColorsAdapter by lazy { ColorsAdapter() }
    private val sizesAdapter: SizesAdapter by lazy { SizesAdapter() }
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private var selectedColor: Int? = null
    private var selectedSize: String? = null
    private val viewModel by viewModels<DetailsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        hideBottomNavigationView()
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPagerRv()
        setupColorsRv()
        setupSizesRv()

        val product = args.product


        sizesAdapter.onItemClick = {
            selectedSize = it
        }

        colorsAdapter.onItemClick = {
            selectedColor = it
        }

        binding.buttonAddToCart.setOnClickListener {
            viewModel.addUpdateProductInCart(CartProduct(product, 1, selectedColor, selectedSize))
        }

        lifecycleScope.launch {
            viewModel.addToCart.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonAddToCart.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.buttonAddToCart.revertAnimation()
                        binding.buttonAddToCart.setBackgroundColor(resources.getColor(R.color.black))
                    }

                    is Resource.Error -> {
                        binding.buttonAddToCart.stopAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = "$ ${product.price}"
            tvProductDescription.text = product.description

            if (product.colors.isNullOrEmpty())
                tvProductColors.visibility = View.INVISIBLE
            if (product.sizes.isNullOrEmpty())
                tvProductSize.visibility = View.INVISIBLE
        }

        viewPager2Adapter.differ.submitList(product.images)
        product.colors?.let { colorsAdapter.differ.submitList(it) }
        product.sizes?.let { sizesAdapter.differ.submitList(it) }

        binding.imgClose.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun setupSizesRv() {

        binding.rvSizes.apply {
            adapter = sizesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

    }

    private fun setupColorsRv() {
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

    }

    private fun setupViewPagerRv() {
        binding.viewpager2Images.apply {
            adapter = viewPager2Adapter
        }
        binding.circleIndicator.setWithViewPager2(binding.viewpager2Images)
        binding.circleIndicator.itemCount = (args.product.images.size)
        binding.circleIndicator.setAnimationMode(CircleIndicator.AnimationMode.SLIDE)

    }


}

