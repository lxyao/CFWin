package com.cfwin.cfwinblockchain.activity.mail.fragment.imap

import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.mail.fragment.abs.AbsProtocolFragment

/**
 * imap协议 收信服务器设置
 */
class InFragment: AbsProtocolFragment() {

    override fun getLayoutId(): Int {
        return R.layout.merge_protocol_layout
    }

    override fun initView() {
        super.initView()
        serviceAddress.hint = "partner.outlook.cn"
    }

    override fun initData() {
    }
}