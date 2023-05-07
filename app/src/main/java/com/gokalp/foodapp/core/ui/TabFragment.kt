package com.gokalp.foodapp.core.ui

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.gokalp.foodapp.R
import com.gokalp.foodapp.core.listeners.NavigationTabListener

class TabFragment : BaseFragment(R.layout.fragment_tab), NavigationTabListener {

    private val currentFragmentId = R.id.container
    val currentFragment get() = childFragmentManager.findFragmentById(currentFragmentId) as? BaseFragment

 /*   override fun updateStatusBar() {
        currentFragment?.updateStatusBar()
    }

  */

    override fun onBackPressed(): Boolean {
        childFragmentManager.run {
            val fragment = findFragmentById(currentFragmentId)
            if (fragment is BaseFragment) {
                if (fragment.onBackPressed()) {
                    return true
                }
            }
            if (backStackEntryCount > 0) {
                lifecycleScope.launchWhenStarted {
                    popBackStack()
                }
                return true
            }
            return false
        }
    }

    private fun open(fragmentTag: String?, isAdding: Boolean = false, @IdRes containerId: Int, fragment: Fragment) =
        lifecycleScope.launchWhenStarted {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED).not()) {
                return@launchWhenStarted
            }
//        val fragmentTag: String = fragment.javaClass.simpleName as String
//        if (childFragmentManager.popBackStackImmediate(fragmentTag, 0)) return@launchWhenStarted
            childFragmentManager.commit {
                setReorderingAllowed(true)
                val currentFragment = childFragmentManager.findFragmentById(currentFragmentId)
                if (currentFragment != null) {
          /*          setCustomAnimations(
                        R.anim.pz_fragment_slide_right_enter,
                        R.anim.pz_fragment_slide_left_exit,
                        R.anim.pz_fragment_slide_left_enter,
                        R.anim.pz_fragment_slide_right_exit
                    )

           */
                    hide(currentFragment)
                }
                if (isAdding) {
                    add(containerId, fragment, fragmentTag)
                } else {
                    replace(containerId, fragment, fragmentTag)
                }
                show(fragment)
                addToBackStack(fragmentTag)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            }
        }

    override fun navigate(fragment: Fragment, tag: String?, isAdding: Boolean) {
        open(tag, isAdding, currentFragmentId, fragment)
    }

    // Alternative of clearBackStack()
    override fun navigateHome() {

        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED).not()) return
        if (isRemoving) return

        childFragmentManager.apply {
            if (backStackEntryCount > 0) {
                popBackStack(getBackStackEntryAt(0).id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }

    override fun navigateBack(tag: String) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED).not()) return
        if (isRemoving) return
        childFragmentManager.apply {
            if (backStackEntryCount > 0) {
                popBackStack(tag, 0)
            }
        }
    }

    fun launch(childFragment: Fragment) =
        lifecycleScope.launchWhenCreated {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED).not()) {
                return@launchWhenCreated
            }
            // if (tabFragment.childFragmentManager.fragments.isNotEmpty()) return@launchWhenCreated
            childFragmentManager.commit {
                setReorderingAllowed(true)
                add(currentFragmentId, childFragment)
            }
        }
}