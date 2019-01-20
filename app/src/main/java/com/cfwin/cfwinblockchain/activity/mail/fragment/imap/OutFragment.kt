package com.cfwin.cfwinblockchain.activity.mail.fragment.imap

import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.mail.fragment.abs.AbsProtocolFragment

/**
 * imap协议 发信服务器设置
 */
class OutFragment: AbsProtocolFragment() {

    override fun getLayoutId(): Int {
        return R.layout.merge_protocol_layout
    }

    override fun initView() {
        super.initView()
        method.text = getString(R.string.out_service)
        serviceAddress.hint = "www.baidu.com"

    }

    override fun initData() {
    }
}