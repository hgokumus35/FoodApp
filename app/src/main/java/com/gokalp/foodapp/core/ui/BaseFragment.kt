package com.gokalp.foodapp.core.ui

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    open fun onBackPressed(): Boolean {
        return false
    }
}