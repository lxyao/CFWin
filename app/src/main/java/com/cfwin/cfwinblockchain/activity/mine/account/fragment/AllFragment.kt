package com.cfwin.cfwinblockchain.activity.mine.account.fragment

import android.text.TextUtils
import com.android.volley.VolleyError
import com.cfwin.base.utils.LogUtil
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.adapter.account.DetailAdapter
import com.cfwin.cfwinblockchain.beans.AccountUseItem
import com.cfwin.cfwinblockchain.beans.response.ScoreResponse
import com.cfwin.cfwinblockchain.beans.response.integral.DataBean
import com.cfwin.cfwinblockchain.beans.response.integral.ScoreDetail
import com.cfwin.cfwinblockchain.db.LocalDBManager
import com.cfwin.cfwinblockchain.db.tables.TransOperaDao
import com.cfwin.cfwinblockchain.http.VolleyListenerInterface
import com.cfwin.cfwinblockchain.http.VolleyRequestUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 账户所有状态显示
 */
class AllFragment :BaseDetailFragment() {

    private lateinit var transOperaDao: TransOperaDao
    private var adapter: DetailAdapter? = null

    override fun initView() {
        super.initView()
        initData()
    }

    override fun initData() {
        transOperaDao = LocalDBManager(mContext!!).getTableOperation(TransOperaDao::class.java)
        onPullDownRefresh()
    }

    override fun onPullDownRefresh() {
        page = 1
        getLocal(page)
        getList(page)
    }

    override fun onPullUpRefresh() {
        page++
        getLocal(page)
    }

    override fun getList(page: Int) {
        val params = "?page=$page&pageSize=${Constant.PAGE_SIZE}&address=${user?.address}"
        VolleyRequestUtil.RequestGet(mContext!!,
                serverHost+Constant.API.ACCOUNT_HISTORY+params,
                "queryhistory",
                object :VolleyListenerInterface(mContext, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener){
                    override fun onMySuccess(result: String?) {
                        LogUtil.e(TAG!!, "交易历史记录 result=$result", true)
                        result?.let {
                            val dataBean = Gson().fromJson(it, object :TypeToken<ScoreResponse<DataBean>>(){}.type) as ScoreResponse<DataBean>
                            if(dataBean.code == 200 && dataBean.data != null){
                                val tmpData = dataBean.data!!.data
                                for(tmp in tmpData){
                                    //查询本地是否有数据
                                    if(!transOperaDao.isData(tmp.tx_id)){
                                        //没有就调用详细接口更新本地
                                        getDetail(tmp.tx_id)
                                    }
                                }
                            }
                        }
                    }

                    override fun onMyError(error: VolleyError?) {
                        LogUtil.e(TAG!!, "交易历史记录 e=$error")
                    }
                },false)
    }

    private fun getDetail(txId: String){
        VolleyRequestUtil.RequestGet(mContext!!,
                serverHost+Constant.API.ACCOUNT_TRANS_DETAIL+"?txid=$txId",
                "transDetail",
                object :VolleyListenerInterface(mContext, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener){
                    override fun onMySuccess(result: String?) {
                        LogUtil.e(TAG!!, "交易详细返回数据 result=$result", true)
                        result?.let {
                            val tmpData = Gson().fromJson(it, object :TypeToken<ScoreResponse<ScoreDetail>>(){}.type) as ScoreResponse<ScoreDetail>
                            if(tmpData.result == tmpData.STATE_SUCCESS){
                                val tmpEnvelop = tmpData.data!!.tx_info
                                val tmpArray = tmpEnvelop.transactionEnvelopeInfo.transactionActionInfoArray[0].argArray
                                var num = tmpArray[3]
                                if(TextUtils.isEmpty(num))num = "0"
                                num = "${num.toLong() / Math.pow(10.0, 8.0)}"
                                var state = 0
                                state = when(tmpEnvelop.isValid){
                                    true-> 1
                                    false-> 2
                                }
                                val bean = AccountUseItem(txId = txId, time = tmpEnvelop.timestamp, fromAccount = tmpArray[1], toAccount = tmpArray[2], num = num, state = state )
                                transOperaDao.insertData(bean)
                            }
                        }
                    }

                    override fun onMyError(error: VolleyError?) {
                        LogUtil.e(TAG!!, "交易详细错误 error=$error")
                    }
                },false)
    }

    private fun getLocal(page: Int){
        val tmp = transOperaDao.queryData(page, user!!.address)
        if(page == 1){
            if(adapter == null){
                adapter = DetailAdapter(mContext!!, tmp, user!!.accountName)
                listView.adapter = adapter
            }else adapter!!.addData(tmp, true)
        }else adapter!!.addData(tmp, false)
        getRefreshUtil().resetRefresh(page)
        refreshView.isLoadMoreEnable = !(tmp.size <= Constant.PAGE_SIZE)
    }
}