package com.gokalp.foodapp.features.home

import android.os.Bundle
import android.view.View
import com.gokalp.foodapp.R
import com.gokalp.foodapp.core.ui.BaseFragment

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
        listeners()
    }

    private fun observers() {

    }

    private fun listeners() {

    }
}