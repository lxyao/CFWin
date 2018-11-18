package com.cfwin.cfwinblockchain.activity.mine.account.fragment

import com.android.volley.VolleyError
import com.cfwin.base.utils.LogUtil
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.http.VolleyListenerInterface
import com.cfwin.cfwinblockchain.http.VolleyRequestUtil

/**
 * 账户所有状态显示
 */
class AllFragment :BaseDetailFragment() {

    override fun initView() {
        super.initView()
        initData()
    }

    override fun initData() {
        onPullDownRefresh()
    }

    override fun onPullDownRefresh() {
        page = 1
        getList(page)
    }

    override fun onPullUpRefresh() {
        page++
        getList(page)
    }

    override fun getList(page: Int) {
        VolleyRequestUtil.RequestPost(mContext!!,
                serverHost+Constant.API.ACCOUNT_HISTORY,
                "queryhistory",
                mapOf<String, String>(),
                object :VolleyListenerInterface(mContext, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener){
                    override fun onMySuccess(result: String?) {
                        LogUtil.e(TAG!!, "交易历史记录 result=$result", true)

                        getRefreshUtil().resetRefresh(page)
                    }

                    override fun onMyError(error: VolleyError?) {
                        LogUtil.e(TAG!!, "交易历史记录 e=$error")
                        getRefreshUtil().resetRefresh(page)
                    }
                },false)
    }
}