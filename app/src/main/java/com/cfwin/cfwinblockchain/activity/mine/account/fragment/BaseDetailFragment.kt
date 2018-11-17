package com.cfwin.cfwinblockchain.activity.mine.account.fragment

import android.widget.ListView
import butterknife.BindView
import butterknife.OnItemClick
import com.cfwin.base.activity.BaseActivity
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseFragment
import com.cfwin.cfwinblockchain.adapter.account.DetailAdapter
import com.cfwin.cfwinblockchain.beans.AccountUseItem

/**
 * 账户详情 子菜单基类
 */
open class BaseDetailFragment :SubBaseFragment() {

    @BindView(android.R.id.list)protected lateinit var listView: ListView

    override fun getLayoutId(): Int {
        return R.layout.fragment_account_detail
    }

    override fun initData() {
        val tmp = listOf(AccountUseItem(state = false), AccountUseItem(state = true), AccountUseItem(state = true))
        listView.adapter = DetailAdapter(mContext!!, tmp as MutableList<AccountUseItem>)
    }

    @OnItemClick(android.R.id.list)
    open fun onItemClick(position: Int){
        if(mContext is BaseActivity){
            (mContext as BaseActivity).showDialog("是否确定重新发起转赠？")
        }
    }
}