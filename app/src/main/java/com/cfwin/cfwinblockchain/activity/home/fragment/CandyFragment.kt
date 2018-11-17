package com.cfwin.cfwinblockchain.activity.home.fragment

import android.content.Intent
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ListView
import android.widget.TextView
import butterknife.BindView
import butterknife.OnItemClick
import com.android.volley.VolleyError
import com.baison.common.utils.BaseRefreshUtil
import com.baison.common.utils.BaseRefreshUtil.IRefreshCallback
import com.cfwin.base.utils.LogUtil
import com.cfwin.cfwinblockchain.BuildConfig
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.AbsParentBaseActivity
import com.cfwin.cfwinblockchain.activity.SubBaseFragment
import com.cfwin.cfwinblockchain.activity.home.CandyDetailActivity
import com.cfwin.cfwinblockchain.adapter.CandyListAdapter
import com.cfwin.cfwinblockchain.beans.CandyItem
import com.cfwin.cfwinblockchain.beans.UserBean
import com.cfwin.cfwinblockchain.beans.response.candy.CandyList
import com.cfwin.cfwinblockchain.http.VolleyListenerInterface
import com.cfwin.cfwinblockchain.http.VolleyRequestUtil
import com.chanven.lib.cptr.PtrClassicFrameLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 主界面 - 糖果布局
 */
class CandyFragment : SubBaseFragment(), IRefreshCallback<CandyFragment> {

    @BindView(R.id.toolbar)
    lateinit var toolBar: Toolbar
    @BindView(R.id.toolbar_title)
    lateinit var topTitle: TextView
    @BindView(R.id.top_line)
    lateinit var topLine: View
    @BindView(android.R.id.list)
    lateinit var listView: ListView
    @BindView(R.id.refresh_view)lateinit var refreshView:PtrClassicFrameLayout
    private lateinit var refreshUtil:BaseRefreshUtil<CandyFragment>
    private lateinit var adapter: CandyListAdapter

    private lateinit var host: String
    private lateinit var user: UserBean
    private var page = 1
    private val pageSize = Constant.PAGE_SIZE
    private lateinit var candyDetailUrl: String


    override fun getLayoutId(): Int {
        return R.layout.fragment_candy
    }

    override fun initView() {
        super.initView()
        toolBar.navigationIcon = null
        topTitle.text = getString(R.string.candy)
        toolBar.setBackgroundColor(resources.getColor(R.color.bg_272F4F))
        topLine.setBackgroundColor(resources.getColor(R.color.bg_272F4F))
        refreshUtil = BaseRefreshUtil(this, refreshView)
        refreshUtil.initView()
        initData()
    }

    override fun initData() {
        if(mContext is AbsParentBaseActivity){
            val act = mContext as AbsParentBaseActivity
            host = act.getServer(Constant.API.TYPE_CANDY)+Constant.API.CANDY_LIST
            user = act.getUser()
        }
        onPullDownRefresh()
    }

    @OnItemClick(android.R.id.list)
    fun onItemClick(position: Int){
        val bean:CandyItem = listView.adapter.getItem(position) as CandyItem
        startActivity(Intent(mContext, CandyDetailActivity::class.java)
                .putExtra("title", bean.Link)
                .putExtra("id", bean.Id)
                .putExtra("detailUrl", if(BuildConfig.DEBUG)"http://40.73.96.49:8083" else candyDetailUrl))
    }

    override fun onPullDownRefresh() {
        page = 1
        getList(page)
    }

    override fun onPullUpRefresh() {
        page++
        getList(page)

    }

    override fun getRefreshUtil(): BaseRefreshUtil<CandyFragment> {
       return refreshUtil
    }

    private fun getList(page: Int){
        VolleyRequestUtil.RequestGet(mContext!!,
                host+user.address+"&pageIndex=$page&pageSize=$pageSize",
                "candyList",
                object: VolleyListenerInterface(mContext, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener){
                    override fun onMySuccess(result: String?) {
                        LogUtil.e(TAG!!, "糖果列表信息 result= $result", true)
                        result?.let {
                            val candyList = Gson().fromJson(result, object :TypeToken<CandyList>(){}.type) as CandyList
                            candyDetailUrl = candyList.Request
                            refreshUtil.resetRefresh(page)
                            if(page == 1){
                                adapter = CandyListAdapter(mContext, candyList.Data?.Items as MutableList<CandyItem>)
                                listView.adapter = adapter
                            }else{
                                adapter.addData(candyList.Data?.Items as MutableList<CandyItem>, false)
                            }
                            try{
                                refreshView.isLoadMoreEnable = page < candyList.Data!!.PageCount
                            }catch (e: Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onMyError(error: VolleyError?) {
                        LogUtil.e(TAG!!, "糖果列表信息错误 error= ${error.toString()}")
                        refreshUtil.resetRefresh(page)
                    }
                },
                false)
    }
}