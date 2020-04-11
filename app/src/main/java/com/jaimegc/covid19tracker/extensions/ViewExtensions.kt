package com.jaimegc.covid19tracker.extensions

import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.TextView

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.rotateLeftAnimation(duration: Long = 250) =
    this.rotateAnimation(duration, 0f, 180f)

fun View.rotateRightAnimation(duration: Long = 250) =
    this.rotateAnimation(duration, 180f, 0f)

private fun View.rotateAnimation(duration: Long = 250, fromDegrees: Float, toDegrees: Float) {
    val animation = RotateAnimation(10f, 180f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    animation.duration = duration
    animation.fillAfter = true
    this.startAnimation(animation)
}

fun TextView.setTextSizeSp(size: Float) =
    this.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)