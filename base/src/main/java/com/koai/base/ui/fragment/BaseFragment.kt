/*
 * *
 *  * Created by Nguyễn Kim Khánh on 7/18/23, 10:10 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/18/23, 10:10 AM
 *
 */

package com.koai.base.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.koai.base.R
import com.koai.base.databinding.WidgetLoadingBinding
import com.koai.base.ui.activity.BaseActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseFragment : Fragment() {
    private lateinit var binding: ViewBinding
    private lateinit var activity: BaseActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getBindingView(container)
        activity = requireActivity() as BaseActivity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState, binding)
    }

    abstract fun getBindingView(container: ViewGroup?): ViewBinding

    abstract fun initView(savedInstanceState: Bundle?, binding: ViewBinding)

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
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
       activity.showKeyboard(view)
    }

    fun closeKeyBoard() {
       activity.closeKeyBoard()
    }

    fun openActivity(destination: Class<*>, canBack: Boolean = true, bundle: Bundle? = null) {
        val intent = Intent(activity, destination)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
        activity.overridePendingTransition(
            R.anim.enter_from_left, R.anim.exit_to_right
        )
        if (!canBack) {
            activity.finish()
        }
    }
}