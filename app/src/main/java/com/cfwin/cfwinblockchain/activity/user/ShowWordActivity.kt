package com.cfwin.cfwinblockchain.activity.user

import android.content.Intent
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.activity.home.MainActivity

/**
 * 备份助记词 界面
 */
class ShowWordActivity : SubBaseActivity() {

    @BindView(R.id.login_word)lateinit var loginWord: TextView
    @BindView(R.id.wallet_word)lateinit var walletWord: TextView
    private var isShow = false

    override fun getLayoutId(): Int {
        return R.layout.activity_show_word
    }

    override fun initView() {
        topTitle.text = getString(R.string.show_word)
    }

    override fun initData() {
        val words = intent.getStringArrayExtra("words")
        loginWord.text = words[0]
        walletWord.text = words[1]
        isShow = intent.getBooleanExtra("isShow", false)
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.complete){
            if(!isShow){
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }else super.onClick(v)
    }
}