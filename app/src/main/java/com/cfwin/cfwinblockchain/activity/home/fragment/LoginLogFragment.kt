package com.cfwin.cfwinblockchain.activity.home.fragment

import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.ListView
import android.widget.TextView
import butterknife.BindView
import com.baison.common.utils.BaseRefreshUtil
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.AbsParentBaseActivity
import com.cfwin.cfwinblockchain.activity.SubBaseFragment
import com.cfwin.cfwinblockchain.adapter.LoginLogAdapter
import com.cfwin.cfwinblockchain.beans.LoginLogItem
import com.cfwin.cfwinblockchain.beans.UserBean
import com.cfwin.cfwinblockchain.db.LocalDBManager
import com.cfwin.cfwinblockchain.db.tables.LogOperaDao
import com.cfwin.cfwinblockchain.http.VolleyListenerInterface
import com.cfwin.cfwinblockchain.http.VolleyRequestUtil
import com.chanven.lib.cptr.PtrClassicFrameLayout

/**
 * 主界面 - 登录布局
 */
class LoginLogFragment : SubBaseFragment(), BaseRefreshUtil.IRefreshCallback<LoginLogFragment> {

    @BindView(R.id.toolbar)
    lateinit var toolBar: Toolbar
    @BindView(R.id.toolbar_title)
    lateinit var topTitle: TextView
    @BindView(R.id.top_line)
    lateinit var topLine: View
    @BindView(android.R.id.list)
    lateinit var listView: ListView
    @BindView(R.id.refresh_view)lateinit var refreshView: PtrClassicFrameLayout
    private lateinit var refreshUtil:BaseRefreshUtil<LoginLogFragment>
    private lateinit var adapter: LoginLogAdapter
    private var page = 1
    private val pageSize = Constant.PAGE_SIZE
    private lateinit var logHost: String
    private var user: UserBean? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_loginlog
    }

    override fun initView() {
        super.initView()
        toolBar.navigationIcon = null
        topTitle.text = getString(R.string.login_log)
        toolBar.setBackgroundColor(resources.getColor(R.color.bg_272F4F))
        topLine.setBackgroundColor(resources.getColor(R.color.bg_272F4F))
        initData()
    }

    override fun initData() {
        if(mContext is AbsParentBaseActivity){
            val tmp = mContext as AbsParentBaseActivity
            user = tmp.getUser()
            logHost = tmp.getServer(Constant.API.TYPE_LOG)
            if(TextUtils.isEmpty(user?.address))user?.address = ""
        }
//        val tmp = LocalDBManager(mContext!!).getTableOperation(LogOperaDao::class.java).queryData(user?.address!!, page)
//        listView.adapter = LoginLogAdapter(mContext!!, tmp as MutableList<LoginLogItem>)
        onPullDownRefresh()
    }

    override fun onPullDownRefresh() {
        page = 1
        getRand()
    }

    override fun onPullUpRefresh() {
        page++
        getRand()
    }

    override fun getRefreshUtil(): BaseRefreshUtil<LoginLogFragment> {
        return refreshUtil
    }

    private fun getRand(){

    }

    private fun getList(page :Int, rand :String){
//        VolleyRequestUtil.RequestGet()
    }
}