package com.cfwin.cfwinblockchain.activity.mine

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import butterknife.BindView
import com.cfwin.base.utils.ToastUtil
import com.cfwin.base.utils.UrlUtil
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity

/**
 * 服务器地址设置界面
 */
class ServiceAddressActivity :SubBaseActivity() {

    @BindView(R.id.score_address)
    lateinit var score: EditText
    @BindView(R.id.candy_address)
    lateinit var candy: EditText
    @BindView(R.id.log_address)
    lateinit var log: EditText
    private lateinit var serverApi: MutableSet<String>

    override fun getLayoutId(): Int {
        return R.layout.activity_service_address
    }

    override fun initView() {
        setTopNavBackground()
        topTitle.text = getString(R.string.api_adddress)
        topMenu.text = getString(R.string.edit)
    }

    override fun initData() {
        serverApi = getSharedPreferences(Constant.configFileName, Context.MODE_PRIVATE)
                .getStringSet(Constant.SERVICE_API, setOf("0,http://40.73.116.190:8080","1,http://40.73.96.49:8082","2,http://42.159.95.0:8080"))
        for (str in serverApi.toTypedArray()){
            val tmp = str.split(",")
            when (tmp[0].toInt()) {
                Constant.API.TYPE_SCORE -> score.setText(tmp[1])
                Constant.API.TYPE_CANDY -> candy.setText(tmp[1])
                Constant.API.TYPE_LOG -> log.setText(tmp[1])
            }
        }
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.toolbar_menu){
            if(getString(R.string.save).equals(topMenu.text.toString().trim())){
                //保存信息，验证输入的地址是否符合地址规则
                if(!checkUrl(score.text.toString().trim(), getString(R.string.score_address))
                        || !checkUrl(candy.text.toString().trim(), getString(R.string.candy_address))
                        || !checkUrl(log.text.toString().trim(), getString(R.string.log_address)) ){
                    return
                }
                isEnabled(false)
                serverApi.clear()
                serverApi.add("${Constant.API.TYPE_SCORE},${score.text}")
                serverApi.add("${Constant.API.TYPE_CANDY},${candy.text}")
                serverApi.add("${Constant.API.TYPE_LOG},${log.text}")
                val editor = getSharedPreferences(Constant.configFileName, Context.MODE_PRIVATE)
                        .edit()
                editor.remove(Constant.SERVICE_API).apply()
                editor.putStringSet(Constant.SERVICE_API, serverApi).apply()
                topMenu.text = getString(R.string.edit)
                showToast("地址保存成功")
            }else{
                //编辑
                isEnabled(true)
                topMenu.text = getString(R.string.save)
            }
        }else super.onClick(v)
    }

    private fun isEnabled(isEnabled: Boolean){
        score.isEnabled = isEnabled
        candy.isEnabled = isEnabled
        log.isEnabled = isEnabled
    }

    private fun checkUrl(tmp: String, type: String): Boolean{
        var hint = type
        var isValid = true
        if(TextUtils.isEmpty(tmp)){
            isValid = false
            hint = getString(R.string.dont_null, hint)
        }else if(!UrlUtil.isUrl(tmp)){
            isValid = false
            hint = getString(R.string.address_input_hint, hint)
        }
        if(!isValid)ToastUtil.showCustomToast(context = this, msg= hint)
        return isValid
    }


}