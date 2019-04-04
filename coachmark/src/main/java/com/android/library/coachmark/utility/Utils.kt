package com.android.library.coachmark.utility

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View

object Utils {
    fun pxToDp(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    fun dpToPx(context: Context, dp: Int): Float {
        val px: Float = dpToPx(context, dp.toFloat())
        return px
    }

    fun dpToPx(context: Context, dipValue: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }

    fun isEmpty(list: List<Any>?): Boolean {
        return !(list != null && !list.isEmpty())
    }

    fun isNotEmpty(list: List<Any>?): Boolean {
        return (list != null && !list.isEmpty())
    }

    fun isEmpty(string: String?): Boolean {
        return !(string != null && string != "" && string != "null")
    }

    fun isNotEmpty(string: String?): Boolean {
        return (string != null && string != "" && string != "null")
    }

    fun getViewBounds(view: View): Rect {
        val size = Rect()
        view.getGlobalVisibleRect(size)
        return size
    }

    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }
}