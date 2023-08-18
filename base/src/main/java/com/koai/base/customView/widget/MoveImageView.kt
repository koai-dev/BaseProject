package com.koai.base.customView.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.widget.AppCompatImageView
import com.koai.base.utils.ScreenUtils

class MoveImageView : AppCompatImageView {
    private var start_x = 0
    private var start_y = 0
    var exitTi: Long = 0
    private var width = 0
    private var screenHeight = 0
    private var statusHeight = 0
    private var navigationHeight = 0

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init()
    }

    constructor(context: Context?) : super(context!!) {
        init()
    }

    private fun init() {
        width = ScreenUtils.getScreenWidth(context)
        screenHeight = ScreenUtils.getScreenHeight(context) //获得屏幕高度
        statusHeight = ScreenUtils.getStatusHeight(context) //获得状态栏的高度
        navigationHeight = ScreenUtils.getVirtualBarHeigh(context) //获取虚拟功能键高度
    }

    fun setWidthHeight(height: Int) {
        screenHeight = height
    }

    interface ClickImageViewCallBack {
        fun onClickSideEnd()
    }

    private var onClickCallBack: ClickImageViewCallBack? = null
    fun setCallBackClick(clickImageViewCallBack: ClickImageViewCallBack?) {
        onClickCallBack = clickImageViewCallBack
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                start_x = event.x.toInt()
                start_y = event.y.toInt()
                exitTi = System.currentTimeMillis() //记录按下时间
                val sideEndX = (getWidth() / 5 * 4).toFloat()
                val sideTopY = (height / 5).toFloat()
                if (event.x > sideEndX && event.y < sideTopY) {
                    if (onClickCallBack != null) {
                        onClickCallBack!!.onClickSideEnd()
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                val loast_x = event.x.toInt()
                val loast_y = event.y.toInt()
                val px = loast_x - start_x
                val py = loast_y - start_y
                val x = x + px
                var y = y + py


                //检测是否到达边缘 左上右下
                //x = x < 0 ? 0 : x > width - getWidth() ? width - getWidth() : x;//暂时去掉x方向的判断
                if (y < 0) { //顶部界限判断，可以根据需求加上标题栏等高度
                    y = 0f
                }

                //底部界限判断，可以根据需求减去底部tap切换栏高度
                if (y > screenHeight - height) {
                    y = (screenHeight - height).toFloat()
                }
                Log.e("position:", "dX:$x dY:$y")
                setX(x)
                setY(y)
            }

            MotionEvent.ACTION_UP -> {
                val rawX = event.rawX.toInt()
                if (rawX >= width / 2) {
                    animate().setInterpolator(DecelerateInterpolator())
                        .setDuration(500)
                        .xBy(width - getWidth() - x)
                        .start()
                } else {
                    val oa = ObjectAnimator.ofFloat(this, "x", x, 0f)
                    oa.interpolator = DecelerateInterpolator()
                    oa.duration = 500
                    oa.start()
                }
                if (System.currentTimeMillis() - exitTi > 200) { // 系统时间和记录的退出时间差大于2秒
                    //只触发滑动事件
                    return true
                }
            }
        }

        //触发点击事件
        return super.onTouchEvent(event)
    }
}