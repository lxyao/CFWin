package com.cfwin.base.activity.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cfwin.base.activity.BaseActivity
import com.cfwin.base.utils.ToastUtil

/**
 * 所有自定义Fragment基类
 */
abstract class BaseFragment : Fragment(), View.OnClickListener{

    protected var TAG :String? = null
    protected var mContext :Context? = null
    protected var fragmentView :View? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        TAG = this.javaClass.simpleName
        mContext = context
        if(mContext is BaseActivity){
            (mContext as BaseActivity).baseFragment = this
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        if(getLayoutId() > 0){
            fragmentView = inflater.inflate(getLayoutId(), container, false)
        }
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    /**
     * 显示toast窗
     */
    fun showToast(resId :Int = 0, msg :String) = ToastUtil.showCustomToast(this.activity!!, resId, msg)

    /**
     * 点击事件回调
     */
    override fun onClick(v: View?) {}

    open fun onAlertView(v: View){}

    /**
     * 设置当前布局文件资源
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 初始化view
     */
    protected abstract fun initView()

    /**
     * 初始化数据
     */
    protected abstract fun initData()
}