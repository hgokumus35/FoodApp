package com.gokalp.foodapp.core.listeners

import android.os.Parcelable
import androidx.fragment.app.Fragment
import kotlinx.parcelize.Parcelize

enum class FoodAppTab {
    HOME, ADD_RECIPE, COLLECTION, PROFILE
}

sealed class FoodAppTabTargetScreen : Parcelable {

    @Parcelize
    object None : FoodAppTabTargetScreen()

    @Parcelize
    object Home : FoodAppTabTargetScreen()

    @Parcelize
    object AddRecipe : FoodAppTabTargetScreen()

    @Parcelize
    object Collection : FoodAppTabTargetScreen()

    @Parcelize
    object Profile : FoodAppTabTargetScreen()
}

interface NavigationListener {

    fun navigate(fragment: Fragment, tag: String? = null, isAdding: Boolean = false)

    fun onBackPressed()

    fun navigateHome()

    fun navigateBack(tag: String)

    fun navigateTab(
        tab: FoodAppTab,
        target: FoodAppTabTargetScreen = FoodAppTabTargetScreen.None,
        reset: Boolean = true
    )
}

interface NavigationTabListener {

    fun navigate(fragment: Fragment, tag: String? = null, isAdding: Boolean = false)

    fun onBackPressed(): Boolean

    fun navigateHome()

    fun navigateBack(tag: String)
}