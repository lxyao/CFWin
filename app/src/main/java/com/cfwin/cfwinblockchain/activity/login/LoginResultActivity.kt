package com.cfwin.cfwinblockchain.activity.login

import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity

/**
 * 登录结果显示界面
 */
class LoginResultActivity :SubBaseActivity() {

    @BindView(R.id.state_img)
    lateinit var stateImg: ImageView
    @BindView(R.id.login_result)
    lateinit var loginResult: TextView
    @BindView(R.id.login_sub_result)
    lateinit var subText: TextView
    @BindView(R.id.success_btn)
    lateinit var successClose: ImageView
    @BindView(R.id.fail_btn)
    lateinit var failBtn: TextView

    override fun getLayoutId(): Int {
        return R.layout.activity_login_result
    }

    override fun loadViewBefore() {
        window.addFlags(Window.FEATURE_NO_TITLE)
    }

    override fun initView() {
        val state = intent.getBooleanExtra("state", false)
        if(state){
            //成功
            stateImg.setImageResource(R.mipmap.ic_login_success)
            loginResult.text = getString(R.string.login_success)
            subText.visibility = View.GONE
            successClose.visibility = View.VISIBLE
            failBtn.visibility = View.GONE
        }else{
            //失败
            stateImg.setImageResource(R.mipmap.ic_login_failed)
            loginResult.text = getString(R.string.login_fail)
            subText.visibility = View.VISIBLE
            successClose.visibility = View.GONE
            failBtn.visibility = View.VISIBLE
        }
    }

    override fun initData() {}

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.success_btn-> finish()
            R.id.fail_btn-> finish()
            else ->super.onClick(v)
        }
    }
}