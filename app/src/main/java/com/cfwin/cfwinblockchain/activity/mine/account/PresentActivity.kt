package com.cfwin.cfwinblockchain.activity.mine.account

import Decoder.BASE64Encoder
import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import com.android.volley.VolleyError
import com.cfwin.base.beans.TransactionCmd
import com.cfwin.base.utils.DateUtil
import com.cfwin.base.utils.LogUtil
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.activity.user.ADD_IDENTIFY
import com.cfwin.cfwinblockchain.activity.user.EC_DIR
import com.cfwin.cfwinblockchain.activity.user.KEY_END_WITH
import com.cfwin.cfwinblockchain.activity.user.WALLET_DIR
import com.cfwin.cfwinblockchain.beans.AccountUseItem
import com.cfwin.cfwinblockchain.beans.UserBean
import com.cfwin.cfwinblockchain.beans.response.ScoreResponse
import com.cfwin.cfwinblockchain.http.VolleyListenerInterface
import com.cfwin.cfwinblockchain.http.VolleyRequestUtil
import com.cfwin.cfwinblockchain.utils.UrlSignUtil
import com.cfwin.cfwinblockchain.utils.UrlSignUtil.replacePlusAndSlash
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.CaptureActivity
import com.google.zxing.CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN
import org.web3j.crypto.Hash.sha256

/**
 * 转赠界面
 */
class PresentActivity :SubBaseActivity() {

    @BindView(R.id.score)lateinit var score: TextView
    @BindView(R.id.send_score)lateinit var sendScore: EditText
    @BindView(R.id.remark)lateinit var remark: EditText
    @BindView(R.id.to_account)lateinit var toAccount: EditText
    @BindView(R.id.gas)lateinit var gas: EditText
    private lateinit var pwdTxt: EditText

    private lateinit var item: UserBean
    private lateinit var host: String
    private var accountUse: AccountUseItem? = null
    private val transactionCmd = TransactionCmd()

    override fun getLayoutId(): Int {
        return R.layout.activity_present
    }

    override fun initView() {
        setTopNavBackground()
        topTitle.text = getString(R.string.change_score)
        topMenu.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_scan, 0)
    }

    override fun initData() {
        item = intent.getParcelableExtra("item")
        score.text = item.integral
        host = getServer(Constant.API.TYPE_SCORE)
        accountUse = intent.getParcelableExtra("bean")
        accountUse?.let {
            sendScore.setText(it.num)
            sendScore.setSelection(it.num.length)
            toAccount.setText(it.toAccount)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.toolbar_menu->{
                //扫描对方账户
                v.isEnabled = false
                startActivityForResult(Intent(this, CaptureActivity::class.java)
                        .putExtra("title", getString(R.string.scan_title)), 201)
            }
            R.id.sure->{
                //转赠
                checkInfo()
            }
            R.id.double_sure->{
                val pwd = pwdTxt.text.toString().trim()
                if(TextUtils.isEmpty(pwd)){
                    showToast(getString(R.string.input_pwd_hint))
                    return
                }
                try{
                    sign(pwd, "$filesDir$WALLET_DIR")
                }catch (e: Exception){
                    e.printStackTrace()
                    LogUtil.e(TAG!!, "转赠失败 e= ${e.localizedMessage}")
                }
            }
            else ->super.onClick(v)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 201 && Activity.RESULT_OK == resultCode){
            toAccount.setText(data?.getStringExtra(INTENT_EXTRA_KEY_QR_SCAN))
        }
        topMenu.isEnabled = true
    }

    private fun checkInfo(){
        val num = parseNum(sendScore.text.toString().trim())
        if(num <= 0){
            showToast(getString(R.string.send_score))
            return
        }
        val myNum = parseNum(score.text.toString().trim())
        if(num > myNum){
            showToast("赠送数量大于账户已有数量")
            return
        }
        val account = toAccount.text.toString().trim()
        if(TextUtils.isEmpty(account)){
            showToast("请输入对方账号！")
            return
        }
        val gas = gas.text.toString().trim()
        transactionCmd.Content.Init(item.address, account, "${(num * Math.pow(10.0, 8.0)).toLong()}", "${item.serial + 1}", DateUtil.getCurrentDateTime())
        transactionCmd.Commission.Init("", item.address, "${(gas.toDouble() * Math.pow(10.0, 8.0)).toLong()}", DateUtil.getCurrentDateTime())
        if(item.type == ADD_IDENTIFY){
            try{
                sign(null, "$filesDir$EC_DIR")
            }catch (e: Exception){
                e.printStackTrace()
                LogUtil.e(TAG!!, "转赠失败 e= ${e.localizedMessage}")
            }
        }else showDialog(title = "转赠积分", contentId = R.layout.show_alert_input)
    }

    override fun onAlertView(v: View) {
        pwdTxt = v.findViewById(R.id.input)
    }

    private fun sign(pwd: String?, dir: String){
        val sb = StringBuffer().append(transactionCmd.Content.from)
                .append(transactionCmd.Content.to)
                .append(transactionCmd.Content.serial)
                .append(transactionCmd.Content.balance)
                .append(transactionCmd.Content.timestamp)
        transactionCmd.SignContent.sign = UrlSignUtil.signTrans(pwd, dir, "${item.address}$KEY_END_WITH", sb.toString().toByteArray())
        val ss = sb.append(transactionCmd.SignContent.sign).toString()
        val hexStr = getHash(ss)
        transactionCmd.Commission.hashTransaction = hexStr
        val commission = StringBuffer().append(transactionCmd.Commission.hashTransaction)
                .append(transactionCmd.Commission.payer)
                .append(transactionCmd.Commission.gas)
                .append(transactionCmd.Commission.commissionTimestamp)
        transactionCmd.SignCommision.sign = UrlSignUtil.signTrans(pwd, dir, "${item.address}$KEY_END_WITH", commission.toString().toByteArray())
        val hashMap = HashMap<String, Any>()
        val contentMap = object2Map(transactionCmd.Content)
        val commissionMap = object2Map(transactionCmd.Commission)
        hashMap.putAll(contentMap)
        hashMap.putAll(commissionMap)
        hashMap["fromSignature"] = transactionCmd.SignContent.sign
        hashMap["payerSignature"] = transactionCmd.SignCommision.sign
        if(item.type == ADD_IDENTIFY){
            hashMap.remove("$"+"change")
            hashMap.remove("serialVersionUID")
        }
        transaction(hashMap)
    }

    /**
     * 生成hash
     * @param str
     * @return
     */
    private fun getHash(str: String): String {
        //生成 hash
        val bytes = str.toByteArray()
        //sha256
        val data = sha256(bytes)
        //base64
        val enc = BASE64Encoder()
        var hexStr = enc.encode(data)
        hexStr = replacePlusAndSlash(hexStr)
        return hexStr
    }

    /**
     * 实体对象转成Map
     * @param obj 实体对象
     * @return
     */
    private fun object2Map(obj: Any?): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        if (obj == null) {
            return map
        }
        val clazz = obj.javaClass
        val fields = clazz.declaredFields
        try {
            for (field in fields) {
                field.isAccessible = true
                map[field.name] = field.get(obj)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return map
    }

    private fun transaction(params: Map<String, Any>){
        VolleyRequestUtil.RequestPost(this,
                host+Constant.API.ACCOUNT_TRANS,
                "transaction",
                params,
                object : VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener){
                    override fun onMySuccess(result: String?) {
                        result?.let {
                            try{
                                val tmp = Gson().fromJson<ScoreResponse<Map<String, String>>>(it, object :TypeToken<ScoreResponse<Map<String, String>>>(){}.type)
                                if(tmp.code == 200 && !tmp.data!!.isEmpty()){
                                    showToast("转赠成功")
                                    finish()
                                }else showToast(tmp.result)
                            }catch (e: Exception){
                                e.printStackTrace()
                                LogUtil.e(TAG!!, "积分转赠 result= $result", true)
                            }
                        }
                    }

                    override fun onMyError(error: VolleyError?) {
                        LogUtil.e(TAG!!, "积分转赠失败 error = $error")
                    }
                }, false)
    }

    private fun parseNum(str: String): Double{
        return try{
            str.toDouble()
        }catch (e: NumberFormatException){
            LogUtil.e(TAG!!, "积分转换失败 e= ${e.localizedMessage}")
            0.0
        }
    }
}