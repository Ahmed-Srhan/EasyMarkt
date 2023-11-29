package com.srhan.easymarkt.ui.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.srhan.easymarkt.adapters.HomeViewPagerAdapter
import com.srhan.easymarkt.databinding.FragmentHomeBinding
import com.srhan.easymarkt.ui.fragments.categories.AccessoryFragment
import com.srhan.easymarkt.ui.fragments.categories.ChairFragment
import com.srhan.easymarkt.ui.fragments.categories.CupboardFragment
import com.srhan.easymarkt.ui.fragments.categories.FurnitureFragment
import com.srhan.easymarkt.ui.fragments.categories.MainCategoryFragment
import com.srhan.easymarkt.ui.fragments.categories.TableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var viewPagerAdapter: HomeViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragment = arrayListOf(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )
        binding.viewPager2.isUserInputEnabled = false
        viewPagerAdapter = HomeViewPagerAdapter(fragment, childFragmentManager, lifecycle)
        binding.viewPager2.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Main"
                1 -> tab.text = "Chair"
                2 -> tab.text = "Cupboard"
                3 -> tab.text = "Table"
                4 -> tab.text = "Accessory"
                5 -> tab.text = "Furniture"
            }
        }.attach()


    }

}