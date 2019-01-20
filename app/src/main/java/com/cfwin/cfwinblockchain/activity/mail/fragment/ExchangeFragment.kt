package com.cfwin.cfwinblockchain.activity.mail.fragment

import android.widget.LinearLayout
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.mail.fragment.abs.AbsProtocolFragment

/**
 * Exchange协议设置界面
 */
class ExchangeFragment: AbsProtocolFragment() {

    override fun getLayoutId(): Int {
        return R.layout.merge_protocol_layout
    }

    override fun initView() {
        super.initView()
        method.text = getString(R.string.mail_service)
        accountTitle.text = getString(R.string.area)
        pwdTitle.text = getString(R.string.userCode)
        portTitle.text =getString(R.string.pwd)
        sslTitle.text = getString(R.string.ssl_type)
        secret.isChecked = true
        secret.isClickable = true
        fragmentView?.findViewById<LinearLayout>(R.id.ssl_group)?.isClickable = false
    }

    override fun initData() {
    }
}