package com.gokalp.foodapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.gokalp.foodapp.common.extensions.disableTooltips
import com.gokalp.foodapp.common.extensions.hide
import com.gokalp.foodapp.common.extensions.show
import com.gokalp.foodapp.core.listeners.FoodAppTab
import com.gokalp.foodapp.core.listeners.FoodAppTabTargetScreen
import com.gokalp.foodapp.core.listeners.TabViewVisibilityHandler
import com.gokalp.foodapp.core.ui.BaseActivity
import com.gokalp.foodapp.core.ui.TabFragment
import com.gokalp.foodapp.features.addrecipe.AddRecipeFragment
import com.gokalp.foodapp.features.collection.CollectionFragment
import com.gokalp.foodapp.features.home.HomeFragment
import com.gokalp.foodapp.features.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Job

class MainActivity : BaseActivity(R.layout.activity_main), TabViewVisibilityHandler {

    companion object {
        private const val EXT_TARGET = "EXT_TARGET"
        private const val SELECTED_TAB_ID: String = "SELECTED_TAB_ID"
    }

    private var updateTabJob: Job? = null
    private var launchTabJob: Job? = null
    private lateinit var bottomNavigationView: BottomNavigationView

    private var currentTabPos: Int? = null
        set(value) {
            val oldValue = field
            field = value
            if (value != null && oldValue != value) {
                val transition = supportFragmentManager.beginTransaction()
                var hideView: View? = null
                if (oldValue != null) {
                    val currentTabLayoutId = tabLayoutIds[oldValue]
                    supportFragmentManager.findFragmentById(currentTabLayoutId)?.let {
                        transition.detach(it)
                    }
                    hideView = findViewById(currentTabLayoutId)
                }
                val newTabLayoutId = tabLayoutIds[value]
                supportFragmentManager.findFragmentById(newTabLayoutId)?.let {
                    transition.attach(it)
                }
                val contentView = findViewById<View>(newTabLayoutId)
                transition.commitNowAllowingStateLoss()
                contentView.isVisible = true
                hideView?.isVisible = false
            }
        }

    private val tabLayoutIds by lazy(LazyThreadSafetyMode.NONE) {
        listOf(
            R.id.fragmentTabHome,
            R.id.fragmentTabAddRecipe,
            R.id.fragmentTabCollection,
            R.id.fragmentTabProfile,
        )
    }

    private val tabItemIds by lazy(LazyThreadSafetyMode.NONE) {
        listOf(
            R.id.tabHome,
            R.id.tabAddRecipe,
            R.id.tabCollection,
            R.id.tabProfile,
        )
    }

    private val currentTab
        get() = when (bottomNavigationView.selectedItemId) {
            R.id.tabAddRecipe -> FoodAppTab.ADD_RECIPE
            R.id.tabCollection -> FoodAppTab.COLLECTION
            R.id.tabProfile -> FoodAppTab.PROFILE
            else -> FoodAppTab.HOME
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        initBottomNavigation()
        if (savedInstanceState == null) {
            updateTab(R.id.tabHome)
        } else {
            val itemId = savedInstanceState.getInt(SELECTED_TAB_ID, R.id.tabHome)
            updateTab(itemId)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            outState.putInt(SELECTED_TAB_ID, bottomNavigationView.selectedItemId)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?): Intent? {
        return intent?.apply {
            val host = data?.host
            if (data == null && extras != null) {
                for (key in extras!!.keySet()) {
                    val value = extras!!.getString(key)
                    val parsedKey = key.lowercase()
            //        if (parsedKey == "redirecturl" || parsedKey == "redirectUrl") openUrl(value)
                }
            }

            when (val target = intent.getParcelableExtra(EXT_TARGET) as? FoodAppTabTargetScreen) {
                FoodAppTabTargetScreen.None -> navigateTab(FoodAppTab.HOME, target, true)
                FoodAppTabTargetScreen.AddRecipe -> navigateTab(FoodAppTab.ADD_RECIPE, target, true)
                FoodAppTabTargetScreen.Collection -> navigateTab(FoodAppTab.COLLECTION, target, true)
                FoodAppTabTargetScreen.Profile -> navigateTab(FoodAppTab.PROFILE, target, true)
                else -> {}
            }
            intent.removeExtra(EXT_TARGET)
        }
    }

    private fun initBottomNavigation() = with(bottomNavigationView) {
        setOnItemSelectedListener { item ->
            updateTab(item.itemId)
            true
        }
        setOnItemReselectedListener {
            updateTab(it.itemId).invokeOnCompletion {
                val currentFragment = supportFragmentManager.findFragmentById(currentFragmentId)
                (currentFragment as? TabFragment)?.navigateHome()
            }
        }
        disableTooltips()
    }

    private fun updateTab(itemId: Int): Job {
        val previousJob = updateTabJob
        if (bottomNavigationView.selectedItemId == itemId && previousJob != null) {
            return previousJob
        }
        updateTabJob = lifecycleScope.launchWhenStarted {
            val tabPos = tabItemIds.indexOf(itemId)
            currentFragmentId = tabLayoutIds[tabPos]

            val currentFragment = supportFragmentManager.findFragmentById(currentFragmentId)
            if (currentFragment == null) { // create tab fragment
                val tabFragment = TabFragment()
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add(currentFragmentId, tabFragment)
                }

                val childFragment = when (itemId) {
                    R.id.tabHome -> HomeFragment.newInstance()
                    R.id.tabAddRecipe -> AddRecipeFragment.newInstance()
                    R.id.tabCollection -> CollectionFragment.newInstance()
                    R.id.tabProfile -> ProfileFragment.newInstance()
                    else -> null
                } ?: Fragment()
                launchTabJob = tabFragment.launch(childFragment)
            }
            currentTabPos = tabPos
        }
        return updateTabJob!!
    }

    override fun navigateTab(tab: FoodAppTab, target: FoodAppTabTargetScreen, reset: Boolean) {
        val itemId = when (tab) {
            FoodAppTab.HOME -> R.id.tabHome
            FoodAppTab.ADD_RECIPE -> R.id.tabAddRecipe
            FoodAppTab.COLLECTION -> R.id.tabCollection
            FoodAppTab.PROFILE -> R.id.tabProfile
            else -> {
                0
            }
        }
        if (bottomNavigationView.selectedItemId != itemId) {
            bottomNavigationView.selectedItemId = itemId
        }
        updateTab(itemId).invokeOnCompletion { e ->
            if (e != null) return@invokeOnCompletion // got error
            navigateTarget(target, reset)
        }
    }

    private fun navigateTarget(target: FoodAppTabTargetScreen, reset: Boolean) {
        launchTabJob?.invokeOnCompletion {
            if (it != null) return@invokeOnCompletion // got error
            if (reset) {
                navigateHome()
            }
        }
    }

    override fun hideTabView() {
        bottomNavigationView.hide(0)
    }

    override fun showTabView() {
        val duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        bottomNavigationView.show(duration)
    }
}
