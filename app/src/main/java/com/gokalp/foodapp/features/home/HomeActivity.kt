package com.gokalp.foodapp.features.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.gokalp.foodapp.R
import com.gokalp.foodapp.core.ui.BaseActivity

class HomeActivity : BaseActivity(R.layout.activity_home) {

    companion object {
        fun newIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        currentFragmentId = R.id.container

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    HomeFragment.newInstance()
                ).commitNow()
        }
    }
}