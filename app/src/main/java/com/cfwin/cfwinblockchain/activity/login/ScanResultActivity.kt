package com.cfwin.cfwinblockchain.activity.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import butterknife.BindView
import com.android.volley.VolleyError
import com.cfwin.base.utils.DateUtil
import com.cfwin.base.utils.LogUtil
import com.cfwin.base.utils.ToastUtil
import com.cfwin.base.utils.UrlUtil
import com.cfwin.base.utils.encoded.EcKeyUtils
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.activity.user.EC_DIR
import com.cfwin.cfwinblockchain.activity.user.KEY_END_WITH
import com.cfwin.cfwinblockchain.activity.user.WALLET_DIR
import com.cfwin.cfwinblockchain.adapter.account.SelectedAdapter
import com.cfwin.cfwinblockchain.beans.LoginLogItem
import com.cfwin.cfwinblockchain.beans.UserBean
import com.cfwin.cfwinblockchain.beans.response.SubVolleyResponse
import com.cfwin.cfwinblockchain.db.LocalDBManager
import com.cfwin.cfwinblockchain.db.tables.LogOperaDao
import com.cfwin.cfwinblockchain.db.tables.UserOperaDao
import com.cfwin.cfwinblockchain.http.VolleyListenerInterface
import com.cfwin.cfwinblockchain.http.VolleyRequestUtil
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.google.zxing.CaptureActivity
import com.google.zxing.CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN
import java.util.*

/**
 * 扫码登录action,三方可以通过此信息启动扫码登录<br/>
 * 该action需要传递一个登录信息，字段名称为 ScanResultActivity.LOGIN_INFO<br/>
 * 该action返回一个结果信息，字段名称为 ScanResultActivity.RESULT_INFO
 */
const val ACTION_SCAN_LOGIN = "com.cfwin.cfwinblockchain.ACTION_SCAN_LOGIN"
/**
 * 扫描登录的地址
 */
const val LOGIN_INFO = "loginInfo"
/**
 * 处理的结果信息
 */
const val RESULT_INFO = "result"

/**
 * 扫码登录-扫描结果确认界面
 */
class ScanResultActivity :SubBaseActivity() {

    @BindView(R.id.ll_scan_result)
    lateinit var llScanResult: LinearLayout
    @BindView(R.id.icon)
    lateinit var urlIcon: ImageView
    @BindView(R.id.title)
    lateinit var urlTitle: TextView
    @BindView(R.id.address)
    lateinit var urlAddress: TextView
    private lateinit var inputTxt: TextView
    private var accountDialog :PopupWindow? = null
    private lateinit var map: MutableMap<String, String>
    private lateinit var accountData:MutableList<UserBean>
    private lateinit var currentAccount: UserBean
    private var isLoginInfo: Boolean = false

    override fun getLayoutId(): Int {
        return R.layout.activity_scan_result
    }

    override fun initView() {
        setTopNavBackground()
        topTitle.text = "扫码登录"
    }

    override fun initData() {
        map = HashMap()
        isLoginInfo = intent.hasExtra(LOGIN_INFO)
        if(isLoginInfo){
            val loginInfo = intent.getStringExtra(LOGIN_INFO)
            if(!scanResult(loginInfo)){
                setResult(Activity.RESULT_CANCELED, invokeResult("loginInfo data unavailable", loginInfo))
                finish()
                return
            }
        }else{
            startActivityForResult(Intent(this, CaptureActivity::class.java)
                    .putExtra("title", "扫码登录"), 101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101){
            if(resultCode == Activity.RESULT_OK){
                val bundle = data?.extras
                val url = bundle?.getString(INTENT_EXTRA_KEY_QR_SCAN)
                if(!scanResult(url)){
                    ToastUtil.showCustomToast(context = this, msg = "扫描结果有误")
                }
            }else{
//                llScanResult.visibility = View.GONE
                finish()
            }
        }
    }

    override fun onBackPressed() {
        if(accountDialog?.isShowing == true){
            accountDialog?.dismiss()
        }else super.onBackPressed()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.sure->{
                //选择账户 登录
                if(v.tag != null){
                    currentAccount = v.tag as UserBean
                    login()
                }
            }
            R.id.double_sure->{
                val txt = inputTxt.text.toString()
                if(TextUtils.isEmpty(txt)){
                    showToast("请输入登录密码")
                    return
                }
                sendRequest(txt, WALLET_DIR)
            }
            R.id.confirm->{
                val url = urlAddress.text.toString()
                if(TextUtils.isEmpty(url)){
                    showToast("请先扫描二维码获取登录信息！")
                    return
                }
                //查询身份下的所有账户
                accountData = LocalDBManager(this).getTableOperation(UserOperaDao::class.java).queryUser("")
                if(accountDialog == null)createAccountDialog()
                accountDialog!!.showAtLocation(v, Gravity.BOTTOM, 0, 0)
            }
            R.id.cancel-> onBackPressed()
            else -> super.onClick(v)
        }
    }

    override fun onAlertView(v: View) {
        inputTxt = v.findViewById(R.id.input)
    }

    /**
     * 创建账户对话框
     */
    private fun createAccountDialog(){
        val view = LayoutInflater.from(mContext).inflate(R.layout.popup_account_selected, null, false)
        accountDialog = PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        accountDialog!!.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))
        accountDialog!!.animationStyle = R.style.ver_popup
        accountDialog!!.isFocusable = true
        val listView = view.findViewById<ListView>(android.R.id.list)
        listView.adapter = SelectedAdapter(this, accountData)
        var bean: UserBean? = null
        listView.setOnItemClickListener { adapterView, _, i, _ ->
            bean = adapterView.adapter.getItem(i) as UserBean?
            (adapterView.adapter as SelectedAdapter).selected(i)
        }
        val cancel: TextView = view.findViewById(R.id.cancel)
        cancel.setOnClickListener {
            onClick(it)
            accountDialog?.dismiss()
        }
        val sure: TextView = view.findViewById(R.id.sure)
        sure.setOnClickListener {
            it.tag = bean
            onClick(it)
            accountDialog?.dismiss()
        }
    }

    /**
     * 登录信息判断
     */
    private fun login(){
        map.clear()
        map["Address"] = currentAccount.address
        if(currentAccount.type == 0){
            //身份登录
            sendRequest(null, EC_DIR)
        }else{
            //账户登录
            showDialog("请输入登录密码", R.layout.show_alert_input)
                    .setCanceledOnTouchOutside(false)
        }
    }

    /**
     * 发送登录请求
     */
    private fun sendRequest(pwd: String?= null, dir: String){
        var urlAddress = urlAddress.contentDescription.toString()
        analyzeUrl(urlAddress)
        val confirmBtn = findViewById<TextView>(R.id.confirm)
        try {
            val prikeystr = EcKeyUtils.getPrivateKey(pwd,  "$filesDir$dir",  "${map["Address"]}$KEY_END_WITH")
            val sign = EcKeyUtils.signReturnHexString(prikeystr, urlAddress.toByteArray())
            map["ECSign"] = sign
            val index = urlAddress.indexOf("?")
            confirmBtn.isEnabled = false
            VolleyRequestUtil.RequestPost(this,
                    urlAddress.substring(0, if(index > 0)index else urlAddress.length),
                    "login",
                    map,
                    object: VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener){
                        override fun onMySuccess(result: String?) {
                            result?.let {
                                try{
                                    val tmp = Gson().fromJson(it, object :TypeToken<SubVolleyResponse<String>>(){}.type) as SubVolleyResponse<*>
                                    if(tmp.result){
                                        //记录登录信息
                                        getSharedPreferences(Constant.configFileName, Context.MODE_PRIVATE)
                                                .edit()
                                                .putString(Constant.CURRENT_ACCOUNT, Gson().toJson(currentAccount))
                                                .apply()
                                        //存储登录日志信息到本地
                                        val bean = LoginLogItem(map["Address"]!!, urlAddress, DateUtil.getCurrentDateTime(), sign)
                                        LocalDBManager(mContext).getTableOperation(LogOperaDao::class.java).insertLog(bean)
                                        result(Activity.RESULT_OK, it, urlAddress)
                                    }else showToast(tmp.msg)
                                }catch (e: JsonSyntaxException){
                                    e.printStackTrace()
                                    LogUtil.e(TAG!!, "正确访问信息 e=$result")
                                }
                            }
                            confirmBtn.isEnabled = true
                        }

                        override fun onMyError(error: VolleyError?) {
                            var msg= error?.localizedMessage
                            if(TextUtils.isEmpty(msg)) msg = "服务器状态 code =${error?.networkResponse?.statusCode}"
                            LogUtil.e(TAG!!, "错误信息 e=$msg")
                            result(Activity.RESULT_CANCELED, msg, urlAddress)
                            confirmBtn.isEnabled = true
                        }
                    },
                    false)
        } catch (e: Exception){
            showToast("error:loginById")
            confirmBtn.isEnabled = true
        }
    }

    /**
     * 解析URL
     * @param scaninfo
     */
    private fun analyzeUrl(scaninfo: String) {
        val uri = Uri.parse(scaninfo)
        val names = uri.queryParameterNames
        for (name in names) {
            val value = uri.getQueryParameter(name)
            map[name] = value
        }
    }

    /**
     * 扫描结果验证
     */
    private fun scanResult(url: String?):Boolean{
        if(!TextUtils.isEmpty(url)){
            if(url!!.startsWith("http://") || url!!.startsWith("https://")){
                //显示扫描结果
                llScanResult.visibility = View.VISIBLE
                urlAddress.text = getString(R.string.item_append, getString(R.string.login_url), UrlUtil.get3W(url))
                urlAddress.contentDescription = url
                return true
            }
        }
        llScanResult.visibility = View.GONE
        return false
    }

    /**
     * 三方调用结果返回格式处理
     */
    private fun invokeResult(msg: String?, loginInfo: String) = Intent()
            .putExtra(RESULT_INFO, """{"msg":$msg,"loginInfo":$loginInfo}""")

    private fun result(state:Int, str: String?, urlAddress: String){
        if(isLoginInfo){
            setResult(state, invokeResult(str, urlAddress))
        }else{
            startActivity(Intent(mContext, LoginResultActivity::class.java)
                    .putExtra("state", state == Activity.RESULT_OK))
        }
        finish()
    }
}