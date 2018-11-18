package com.cfwin.cfwinblockchain.activity.mine.account.fragment

import android.widget.ListView
import butterknife.BindView
import butterknife.OnItemClick
import com.baison.common.utils.BaseRefreshUtil
import com.cfwin.base.activity.BaseActivity
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseFragment
import com.cfwin.cfwinblockchain.activity.mine.account.DetailActivity
import com.cfwin.cfwinblockchain.adapter.account.DetailAdapter
import com.cfwin.cfwinblockchain.beans.AccountUseItem
import com.cfwin.cfwinblockchain.beans.UserBean
import com.chanven.lib.cptr.PtrClassicFrameLayout

/**
 * 账户详情 子菜单基类
 */
open class BaseDetailFragment :SubBaseFragment(), BaseRefreshUtil.IRefreshCallback<BaseDetailFragment>{

    @BindView(android.R.id.list)protected lateinit var listView: ListView
    @BindView(R.id.refresh_view)protected lateinit var refreshView: PtrClassicFrameLayout
    private lateinit var refreshUtil: BaseRefreshUtil<BaseDetailFragment>
    protected var page: Int = 1
    protected var pageSize = Constant.PAGE_SIZE
    protected var user: UserBean? = null
    protected var serverHost: String? = ""

    override fun getLayoutId(): Int {
        return R.layout.fragment_account_detail
    }

    override fun initView() {
        super.initView()
        refreshUtil = BaseRefreshUtil(this, refreshView)
        refreshUtil.initView()
        if(mContext is DetailActivity){
            val tmp = mContext as DetailActivity
            user = tmp.getUser()
            serverHost = tmp.getServer(Constant.API.TYPE_SCORE)
        }
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

    override fun onPullDownRefresh() {}

    override fun onPullUpRefresh() {}

    override fun getRefreshUtil(): BaseRefreshUtil<BaseDetailFragment> {
        return refreshUtil
    }

    open fun getList(page: Int){}
}