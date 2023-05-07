package com.gokalp.foodapp.core.ui

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import com.gokalp.foodapp.core.listeners.FoodAppTab
import com.gokalp.foodapp.core.listeners.FoodAppTabTargetScreen
import com.gokalp.foodapp.core.listeners.NavigationListener

abstract class BaseActivity(@LayoutRes layoutId: Int) : AppCompatActivity(layoutId), NavigationListener {

    private var checkNavigate: Boolean = false
    private var tempFragment: Fragment? = null
    private var tempTag: String? = null
    private var tempFragmentIsAdd: Boolean = false
    private val currentTabFragment get() = supportFragmentManager.findFragmentById(currentFragmentId) as? TabFragment

    @IdRes
    protected var currentFragmentId = 0

    override fun onBackPressed() {
        supportFragmentManager.apply {
            val fragment = findFragmentById(currentFragmentId)
            if (fragment is BaseFragment) {
                if (fragment.onBackPressed()) {
                    return
                }
            }
            if (backStackEntryCount > 0) {
                popBackStack()
                return
            }
            finish()
        }
    }

    private fun open(isAdding: Boolean = false, @IdRes containerId: Int, fragment: Fragment, tag: String? = null) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED).not()) {
            tempFragment = fragment
            tempTag = tag
            tempFragmentIsAdd = isAdding
            checkNavigate = true
            return
        }
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            val currentFragment = supportFragmentManager.findFragmentById(currentFragmentId)
            if (currentFragment != null) {
                hide(currentFragment)
            }
            if (isAdding) {
                add(containerId, fragment, tag)
            } else {
                replace(containerId, fragment, tag)
            }
            show(fragment)
            addToBackStack(tag)
        }
    }

    override fun navigate(fragment: Fragment, tag: String?, isAdding: Boolean) {
        currentTabFragment?.let {
            it.navigate(fragment, tag, isAdding)
            return
        }
        open(isAdding, currentFragmentId, fragment, tag)
    }

    // Alternative of clearBackStack()
    override fun navigateHome() {
        currentTabFragment?.let {
            it.navigateHome()
            return
        }
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED).not()) return
        if (isFinishing) return

        supportFragmentManager.apply {
            if (backStackEntryCount > 0) {
                popBackStack(getBackStackEntryAt(0).id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }

    override fun navigateBack(tag: String) {
        currentTabFragment?.let {
            it.navigateBack(tag)
            return
        }
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED).not()) return
        if (isFinishing) return
        supportFragmentManager.apply {
            if (backStackEntryCount > 0) {
                popBackStack(tag, 0)
            }
        }
    }

    override fun navigateTab(tab: FoodAppTab, target: FoodAppTabTargetScreen, reset: Boolean) = Unit
}