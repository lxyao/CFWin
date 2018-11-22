package com.cfwin.cfwinblockchain.activity.home.fragment

import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import butterknife.BindView
import com.android.volley.VolleyError
import com.baison.common.utils.BaseRefreshUtil
import com.cfwin.base.utils.LogUtil
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.AbsParentBaseActivity
import com.cfwin.cfwinblockchain.activity.SubBaseFragment
import com.cfwin.cfwinblockchain.activity.user.ADD_IDENTIFY
import com.cfwin.cfwinblockchain.activity.user.EC_DIR
import com.cfwin.cfwinblockchain.activity.user.KEY_END_WITH
import com.cfwin.cfwinblockchain.activity.user.WALLET_DIR
import com.cfwin.cfwinblockchain.adapter.LoginLogAdapter
import com.cfwin.cfwinblockchain.beans.LoginLogItem
import com.cfwin.cfwinblockchain.beans.UserBean
import com.cfwin.cfwinblockchain.beans.response.ScoreResponse
import com.cfwin.cfwinblockchain.beans.response.log.LogData
import com.cfwin.cfwinblockchain.beans.response.log.LogList
import com.cfwin.cfwinblockchain.db.LocalDBManager
import com.cfwin.cfwinblockchain.db.tables.LogOperaDao
import com.cfwin.cfwinblockchain.http.JsonVolleyUtil
import com.cfwin.cfwinblockchain.http.VolleyListenerInterface
import com.cfwin.cfwinblockchain.http.VolleyRequestUtil
import com.cfwin.cfwinblockchain.utils.UrlSignUtil
import com.chanven.lib.cptr.PtrClassicFrameLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

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
    private var pwdTxt: EditText? = null
    private lateinit var dir: String

    override fun getLayoutId(): Int {
        return R.layout.fragment_loginlog
    }

    override fun initView() {
        super.initView()
        toolBar.navigationIcon = null
        topTitle.text = getString(R.string.login_log)
        toolBar.setBackgroundColor(resources.getColor(R.color.bg_272F4F))
        topLine.setBackgroundColor(resources.getColor(R.color.bg_272F4F))
        refreshUtil = BaseRefreshUtil(this, refreshView)
        refreshUtil.initView()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser && fragmentView != null){
            initData()
        }
    }

    override fun initData() {
        if(mContext is AbsParentBaseActivity){
            val tmp = mContext as AbsParentBaseActivity
            user = tmp.getUser()
            logHost = tmp.getServer(Constant.API.TYPE_LOG)
            if(TextUtils.isEmpty(user?.address))user?.address = ""
            if(user?.type == ADD_IDENTIFY){
                dir = "${mContext?.filesDir}$EC_DIR"
                onPullDownRefresh()
            }else{
                dir = "${mContext?.filesDir}$WALLET_DIR"
                tmp.showDialog(title = "查看日志", contentId = R.layout.show_alert_input)
            }
        }
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

    override fun onAlertView(v: View) {
        pwdTxt = v.findViewById(R.id.input)
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.double_sure){
            val pwd = pwdTxt?.text.toString().trim()
            if(TextUtils.isEmpty(pwd)){
                showToast(msg = getString(R.string.input_pwd_hint))
                return
            }
            onPullDownRefresh()
        }else
            super.onClick(v)
    }

    private fun getRand(){
        JsonVolleyUtil.request(mContext!!,
                logHost+Constant.API.LOG_RAND,
                "loginRand",
                 "{\"address\":\"${user?.address}\"}",
                object :VolleyListenerInterface(mContext, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener){
                    override fun onMySuccess(result: String?) {
                        LogUtil.e(TAG!!, "日志随机数获取 = $result", true)
                        result?.let {
                            val tmp = Gson().fromJson(it, object :TypeToken<ScoreResponse<Map<String, String>>>(){}.type) as ScoreResponse<Map<String, String>>
                            if(tmp.code == 200 && (tmp.data?.isEmpty() == false)){
                                val rand = tmp.data!!["rand"]
                                getList(page, rand!!)
                            }
                        }
                    }

                    override fun onMyError(error: VolleyError?) {
                        LogUtil.e(TAG!!, "日志随机数获取错误 e = $error")
                        refreshUtil.resetRefresh(page)
                    }
                },
                false)
    }

    private fun getList(page :Int, rand :String){
        val pwd = if(pwdTxt == null)null else pwdTxt?.text.toString().trim()
        val selfSign = UrlSignUtil.signLog(pwd, dir, "${user?.address}$KEY_END_WITH", (user?.address+rand).toByteArray())
        val params = "?pageIndex=$page&pageSize=$pageSize&address=${user?.address}&selfSign=$selfSign"
        VolleyRequestUtil.RequestGet(mContext!!,
                logHost+Constant.API.LOG_LIST+params,
                "loginList",
                object :VolleyListenerInterface(mContext, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener){
                    override fun onMySuccess(result: String?) {
                        LogUtil.e(TAG!!, "登录日志获取 result=$result", true)
                        result?.let {
                            val logList = Gson().fromJson(it, object :TypeToken<ScoreResponse<LogData>>(){}.type) as ScoreResponse<LogData>
                            if(logList.code == 200 && !logList.data!!.records.isEmpty()){
                                compare(logList.data!!.records, page)
                            }else{
                                if(!TextUtils.isEmpty(logList.msg))showToast(msg = logList.msg)
                            }
                        }
                        refreshUtil.resetRefresh(page)
                    }

                    override fun onMyError(error: VolleyError?) {
                        LogUtil.e(TAG!!, "登录日志获取错误 error= $error")
                        refreshUtil.resetRefresh(page)
                    }
                },
                false)
    }

    private fun compare(data: List<LogList>, page: Int){
        var addData = ArrayList<LoginLogItem>(data.size)
        val logDao = LocalDBManager(mContext!!).getTableOperation(LogOperaDao::class.java)
        for(server in data){
            val tmp = logDao.queryData(server)
            if(tmp.isEmpty()){
                val sd = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",Locale.getDefault())
                var time = sd.parse(server.loginTime)
                sd.applyPattern("yyyy-MM-dd HH:mm:ss")
                addData.add(LoginLogItem(loginAccount = server.address, loginUrl = server.loginUrl, sign = server.sign, state = true, time = sd.format(time)))
            }else addData.addAll(tmp)
        }
        if(page == 1){
            adapter = LoginLogAdapter(mContext!!, addData)
            listView.adapter = adapter
        }else{
            adapter.addData(addData, false)
        }
    }
}