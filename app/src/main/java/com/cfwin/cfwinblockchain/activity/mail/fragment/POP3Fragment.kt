package com.cfwin.cfwinblockchain.activity.mail.fragment

import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.mail.fragment.abs.AbsConfigFragment

/**
 * POP3协议设置界面
 */
class POP3Fragment: AbsConfigFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_imap

    }

    override fun initView() {
        super.initView()
        configName = Constant.popConfig
        initData()
    }

    override fun initData() {
        super.initData()
        inFragment.port.hint = "110"
        outFragment.port.hint = "25"
    }
}