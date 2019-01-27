package com.cfwin.cfwinblockchain.activity.mail.fragment

import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.mail.fragment.abs.AbsConfigFragment

/**
 * IMAP协议设置界面
 */
class IMAPFragment: AbsConfigFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_imap
    }

    override fun initView() {
        super.initView()
        configName = Constant.imapConfig
        initData()
    }
}