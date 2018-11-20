package com.cfwin.cfwinblockchain.activity.mine.account

import android.content.Intent
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import butterknife.BindView
import butterknife.OnClick
import com.cfwin.base.utils.LogUtil
import com.cfwin.base.utils.encoded.EcKeyUtils
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.activity.mine.account.fragment.*
import com.cfwin.cfwinblockchain.activity.user.*
import com.cfwin.cfwinblockchain.adapter.HomePageAdapter
import com.cfwin.cfwinblockchain.beans.AccountItem
import com.cfwin.cfwinblockchain.beans.UserBean

/**
 * 账户记录详细信息 界面
 */
class DetailActivity :SubBaseActivity(), ViewPager.OnPageChangeListener{

    @BindView(R.id.viewPager)lateinit var viewPager: ViewPager
    @BindView(R.id.tabLine)lateinit var tabLine: View
    private lateinit var item: UserBean
    private var pwdTxt: EditText? = null

    /**
     * 下划线最大宽度
     */
    var maxWidth = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_account_detail
    }

    override fun getUser(): UserBean {
        return item
    }

    override fun initView() {
        setTopNavBackground()
        topMenu.text = getString(R.string.helper_word)
        item = intent.getParcelableExtra("item")
        topTitle.text = item.accountName
        topTitle.setSingleLine()
        viewPager.addOnPageChangeListener(this)
        maxWidth = resources.displayMetrics.widthPixels shr 2
        callocation(0)
    }

    override fun initData() {
        //, OutFragment(), InFragment(), FailedFragment()
        val tmp = listOf(AllFragment())
        viewPager.adapter = HomePageAdapter(tmp, supportFragmentManager)
    }

    override fun onPageScrollStateChanged(p0: Int) {}

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

    override fun onPageSelected(p0: Int) {
        callocation(p0)
    }

    override fun onAlertView(v: View) {
        pwdTxt = v.findViewById(R.id.input)
    }

    @OnClick(R.id.tabAll, R.id.tabOut, R.id.tabIn, R.id.tabFailed, R.id.show_account, R.id.change_score)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tabAll-> {
                viewPager.currentItem = 0
                callocation(viewPager.currentItem)
            }
            R.id.tabOut-> {
                viewPager.currentItem = 1
                callocation(viewPager.currentItem)
            }
            R.id.tabIn-> {
                viewPager.currentItem = 2
                callocation(viewPager.currentItem)
            }
            R.id.tabFailed-> {
                viewPager.currentItem = 3
                callocation(viewPager.currentItem)
            }
            R.id.show_account->{
                //显示账户
                startActivity(Intent(this, ShowAccountActivity::class.java)
                        .putExtra("item", item))
            }
            R.id.change_score-> {
                //转赠
                startActivityForResult(Intent(this, PresentActivity::class.java)
                        .putExtra("item", item), 202)
            }
            R.id.toolbar_menu-> {
                //显示助记词
                showDialog(title = "查看助记词", contentId = R.layout.show_alert_input, type = 1)
                        .setCanceledOnTouchOutside(false)
            }
            R.id.double_sure->{
                if(v.contentDescription.toString().toInt() == 1)
                    showWord()
                else super.onClick(v)
            }
            else ->super.onClick(v)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtil.e(TAG!!, "正常转赠 $requestCode $resultCode")
        baseFragment?.let {
            it.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showWord(){
        val pwd = pwdTxt?.text.toString().trim()
        if(TextUtils.isEmpty(pwd)){
            showToast(getString(R.string.input_pwd_hint))
            return
        }
        //确定显示
        val word = if(item.type == ADD_IDENTIFY)
            EcKeyUtils.getMnemonic(pwd,"$filesDir$EC_DIR" , "${item.address}$WORD_END_WITH")
        else{
            EcKeyUtils.getMnemonic(pwd,"$filesDir$WALLET_DIR" , "${item.address}$WORD_END_WITH")
        }
        if(TextUtils.isEmpty(word)){
            showToast("密码输入错误！")
            return
        }
        val words = if(item.type == ADD_IDENTIFY)arrayOf(word, "") else arrayOf("", word)
        startActivity(Intent(this, ShowWordActivity::class.java)
                .putExtra("words", words)
                .putExtra("isShow", true))
    }

    private fun callocation(position: Int){
        val lp = tabLine.layoutParams as ViewGroup.MarginLayoutParams
        lp.leftMargin = position * maxWidth
        lp.width = maxWidth
        tabLine.layoutParams = lp
    }
}
