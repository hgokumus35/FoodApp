package com.gokalp.foodapp.core.ui

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity(@LayoutRes layoutId: Int) : AppCompatActivity(layoutId) {

//    private val currentTabFragment get() = supportFragmentManager.findFragmentById(currentFragmentId) as? TabFragment

    @IdRes
    protected var currentFragmentId = 0

}