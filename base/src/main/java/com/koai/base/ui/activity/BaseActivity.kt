/*
 * *
 *  * Created by Nguyễn Kim Khánh on 7/18/23, 10:10 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/18/23, 10:10 AM
 *
 */

package com.koai.base.ui.activity

import android.app.ActionBar.LayoutParams
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.koai.base.R
import com.koai.base.customView.loading.BaseLoadingView
import com.koai.base.databinding.ActivityBaseBinding
import com.koai.base.utils.NetworkUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ViewBinding
    private lateinit var rootView: ActivityBaseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }
        rootView = DataBindingUtil.inflate(layoutInflater, R.layout.activity_base, null, false)
        binding = getBindingView()
        rootView.container.addView(binding.root)
        rootView.loading.addView(getLoadingView())
        setContentView(rootView.root)
        initView(savedInstanceState, binding)
        NetworkUtil(this).observe(this) {
            if (!it){
                Toast.makeText(this, resources.getString(R.string.you_are_offline), Toast.LENGTH_SHORT).show()
            }
        }
    }

    abstract fun getBindingView(): ViewBinding

    abstract fun initView(savedInstanceState: Bundle?, binding: ViewBinding)

    open fun getLoadingView(): View {
        return BaseLoadingView(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    fun openActivity(destination: Class<*>, canBack: Boolean = true, bundle: Bundle? = null) {
        val intent = Intent(this, destination)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
        this.overridePendingTransition(
            R.anim.enter_from_left, R.anim.exit_to_right
        )
        if (!canBack) {
            finish()
        }
    }

    fun addFragment(fragment: Fragment, containerId: Int) {
        supportFragmentManager.beginTransaction()
            .add(containerId, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun replaceFragment(fragment: Fragment, containerId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .commit()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveEvent(event: Any?) {
        handleEvent(event)
    }

    //receiver any event
    open fun handleEvent(event: Any?) {

    }

    fun sendEvent(message: Any?) {
        requireNotNull(message) { "Object message can not be null" }
        EventBus.getDefault().post(message)
    }

    fun showKeyboard(view: EditText) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun closeKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.window.decorView.windowToken, 0)
    }

    fun toggleProgressLoading(isShow: Boolean) {
        rootView.hasLoading = isShow
    }
}