package com.gokalp.foodapp.core.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

fun View.show(duration: Long) {
    visibility = View.VISIBLE
    animate()
        .alpha(1f)
        .setDuration(duration)
        .setListener(null)
}

fun View.hide(duration: Long) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                visibility = View.GONE
            }
        })
}