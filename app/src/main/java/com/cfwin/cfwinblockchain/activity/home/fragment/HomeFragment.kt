package com.cfwin.cfwinblockchain.activity.home.fragment

import android.content.Intent
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseFragment
import com.cfwin.cfwinblockchain.activity.login.ScanResultActivity

/**
 * 主界面 - 首页布局
 */
class HomeFragment : SubBaseFragment() {

    @BindView(R.id.toolbar)
    lateinit var toolBar : Toolbar
    @BindView(R.id.toolbar_title)
    lateinit var topTitle :TextView
    @BindView(R.id.top_line)
    lateinit var topLine: View

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        super.initView()
        toolBar.navigationIcon = null
        topTitle.text = "CFCHAIN"
        toolBar.setBackgroundColor(resources.getColor(R.color.bg_272F4F))
        topLine.setBackgroundColor(resources.getColor(R.color.bg_272F4F))
    }

    override fun initData() {}

    @OnClick(R.id.scanLogin)
    override fun onClick(v: View?) {
        if(v?.id == R.id.scanLogin){
            startActivity(Intent(mContext!!, ScanResultActivity::class.java))
        }else super.onClick(v)
    }
}