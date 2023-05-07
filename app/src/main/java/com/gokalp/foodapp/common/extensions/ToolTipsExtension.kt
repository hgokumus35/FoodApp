package com.gokalp.foodapp.common.extensions

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.forEach
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

fun BottomNavigationView.disableTooltips() {
    menu.forEach { findViewById<View>(it.itemId)?.disableTooltips() }
}

fun TabLayout.disableTooltips() {
    for (index in 0..tabCount) {
        getTabAt(index)?.view?.disableTooltips()
    }
}

fun Toolbar.disableTooltips() {
    menu.forEach { findViewById<View>(it.itemId)?.disableTooltips() }
}

fun View.disableTooltips() {
    setOnLongClickListener { true }
    TooltipCompat.setTooltipText(this, null)
}