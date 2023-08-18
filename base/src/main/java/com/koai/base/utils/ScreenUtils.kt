package com.koai.base.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager

object ScreenUtils {
    fun getScreenWidth(context: Context): Int {
        val wm = getWindowManager(context)
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        val wm = getWindowManager(context)
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    @SuppressLint("PrivateApi")
    fun getStatusHeight(context: Context): Int {
        var statusHeight = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val height = clazz.getField("status_bar_height").get(`object`)!!.toString().toInt()
            statusHeight = context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return statusHeight
    }

    /**
     * 获取虚拟功能键高度
     */
    fun getVirtualBarHeigh(context: Context): Int {
        var vh = 0
        val windowManager = getWindowManager(context)
        val display = windowManager.defaultDisplay
        val dm = DisplayMetrics()
        try {
            val c = Class.forName("android.view.Display")
            val method = c.getMethod("getRealMetrics", DisplayMetrics::class.java)
            method.invoke(display, dm)
            vh = dm.heightPixels - windowManager.defaultDisplay.height
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return vh
    }

    fun getVirtualBarHeigh(activity: Activity): Int {
        var titleHeight = 0
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusHeight = frame.top
        titleHeight =
            activity.window.findViewById<View>(Window.ID_ANDROID_CONTENT).top - statusHeight
        return titleHeight
    }

    private fun getWindowManager(context: Context): WindowManager {
        return context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
}