package com.jaimegc.covid19tracker.common.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import android.view.Menu
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vdurmont.emoji.EmojiParser

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Float.toPx(): Float = (this * Resources.getSystem().displayMetrics.density)

fun Float.toDp(): Float = (this / Resources.getSystem().displayMetrics.density)

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.isVisible(): Boolean =
    visibility == View.VISIBLE

inline fun <reified T : View> View.find(@IdRes id: Int): T = findViewById(id)

inline fun <reified T : View> Activity.find(@IdRes id: Int): T = findViewById(id)

fun View.rotateLeftAnimation(duration: Long = 250) =
    this.rotateAnimation(duration, 0f, 180f)

fun View.rotateRightAnimation(duration: Long = 250) =
    this.rotateAnimation(duration, 180f, 0f)

private fun View.rotateAnimation(duration: Long = 250, fromDegrees: Float, toDegrees: Float) {
    val animation = RotateAnimation(
        fromDegrees,
        toDegrees,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    animation.duration = duration
    animation.fillAfter = true
    this.startAnimation(animation)
}

fun TextView.setTextSizeSp(size: Float) =
    this.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)

fun TextView.setTextColor(context: Context, colorResource: Int) =
    setTextColor(ContextCompat.getColor(context, colorResource))

fun TextView.setEmojiCountry(emojiCountryCharacters: String) {
    text = if (emojiCountryCharacters.length == 2) {
        val firstLetter =
            Character.codePointAt(emojiCountryCharacters, 0) - 0x41 + 0x1F1E6
        val secondLetter =
            Character.codePointAt(emojiCountryCharacters, 1) - 0x41 + 0x1F1E6
        String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
    } else {
        when (emojiCountryCharacters.isNotEmpty()) {
            true -> EmojiParser.parseToUnicode(emojiCountryCharacters)
            false -> EmojiParser.parseToUnicode(":question:")
        }
    }
}

fun RecyclerView.updateAdapter(adapter: ConcatAdapter) {
    if (this.adapter != adapter) this.adapter = adapter
}

fun ConcatAdapter.removeAllAdapters() =
    this.adapters.map { adapter -> removeAdapter(adapter) }

fun <T : RecyclerView.ViewHolder> ConcatAdapter.removeAllAdaptersExcept(adapter: RecyclerView.Adapter<T>) =
    this.adapters.map { if (it != adapter) removeAdapter(adapter) }

fun <T : RecyclerView.ViewHolder> ConcatAdapter.containsAdapter(
    adapter: RecyclerView.Adapter<T>,
    removeOthers: Boolean = false
): Boolean =
    this.adapters.contains(adapter).also { isContent ->
        if (isContent && removeOthers) removeAllAdaptersExcept(adapter)
    }

fun Menu.showItems(vararg itemsPos: Int) {
    for (itemPos in itemsPos) getItem(itemPos).isVisible = true
}

fun Menu.hideItems(vararg itemsPos: Int) {
    for (itemPos in itemsPos) getItem(itemPos).isVisible = false
}

fun Menu.enableItem(itemPos: Int) {
    for (index in 0 until size()) {
        if (index != itemPos) {
            getItem(index).icon.setTint(Color.BLACK)
            getItem(index).isChecked = false
        } else {
            getItem(index).icon.setTint(Color.WHITE)
            getItem(index).isChecked = true
        }
    }
}

fun Menu.isCurrentItemChecked(itemPos: Int): Boolean = getItem(itemPos).isChecked

fun Menu.isCurrentItemChecked(): Int {
    var currentItem = 0
    for (index in 0 until size()) {
        if (getItem(index).isChecked) {
            currentItem = index
            break
        }
    }

    return currentItem
}

fun Spinner.onItemSelected(ignoreFirst: Boolean = false, itemSelectedPos: (Int) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
            if (ignoreFirst.not() || pos != 0) itemSelectedPos(pos)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
    }
}

fun <T> Context.openActivity(it: Class<T>, noAnimation: Boolean = true) {
    val intent = Intent(this, it)
    if (noAnimation) intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
    startActivity(intent)
}