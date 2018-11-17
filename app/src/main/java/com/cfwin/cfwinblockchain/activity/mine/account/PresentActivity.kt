package com.cfwin.cfwinblockchain.activity.mine.account

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import com.cfwin.base.utils.LogUtil
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.beans.UserBean
import com.google.zxing.CaptureActivity
import com.google.zxing.CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN

/**
 * 转赠界面
 */
class PresentActivity :SubBaseActivity() {

    @BindView(R.id.score)lateinit var score: TextView
    @BindView(R.id.send_score)lateinit var sendScore: EditText
    @BindView(R.id.remark)lateinit var remark: EditText
    @BindView(R.id.to_account)lateinit var toAccount: EditText
    @BindView(R.id.gas)lateinit var gas: EditText

    private lateinit var item: UserBean

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

    }

    private fun parseNum(str: String): Long{
        return try{
            str.toLong()
        }catch (e: NumberFormatException){
            LogUtil.e(TAG!!, "积分转换失败 e= ${e.localizedMessage}")
            0L
        }
    }
}