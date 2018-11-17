package com.cfwin.cfwinblockchain.activity.mine.fragment

import android.widget.ListView
import butterknife.BindView
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseFragment
import com.cfwin.cfwinblockchain.adapter.mine.MsgAdapter
import com.cfwin.cfwinblockchain.beans.MsgItem

/**
 * 系统消息 界面
 */
open class SysFragment : SubBaseFragment() {

    @BindView(R.id.msg_list)
    lateinit var listview: ListView

    override fun getLayoutId(): Int {
        return R.layout.fragment_sys_msg
    }

    override fun initView() {
        super.initView()
        initData()
    }

    override fun initData() {
        val tmp = listOf(MsgItem(), MsgItem())
        listview.adapter = MsgAdapter(mContext!!, tmp as MutableList<MsgItem>)
    }
}