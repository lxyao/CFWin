package com.cfwin.cfwinblockchain.activity.user

import android.content.Intent
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ScrollView
import butterknife.BindView
import butterknife.OnCheckedChanged
import com.cfwin.base.utils.AddressUtil
import com.cfwin.base.utils.PatternUtil
import com.cfwin.base.weak.AbsWeakContext
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.beans.UserBean
import com.cfwin.cfwinblockchain.db.LocalDBManager
import com.cfwin.cfwinblockchain.db.tables.UserOperaDao

/**
 * 身份目录
 */
const val EC_DIR = com.cfwin.base.utils.EC_DIR //"/identify/"
/**
 * 钱包目录
 */
const val WALLET_DIR = com.cfwin.base.utils.WALLET_DIR //"/wallet/"
/**
 * 后缀名
 */
const val KEY_END_WITH = com.cfwin.base.utils.KEY_END_WITH //".prik"
/**
 * 助记词后缀
 */
const val WORD_END_WITH = com.cfwin.base.utils.WORD_END_WITH //".mcw"

/**
 * 创建身份界面
 */
class CreateUserActivity : SubBaseActivity() {

    @BindView(R.id.scrollView)
    lateinit var scrollView: ScrollView
    @BindView(R.id.userName)
    lateinit var userName: EditText
    @BindView(R.id.userAccount)
    lateinit var userAccount: EditText
    @BindView(R.id.userPwd)
    lateinit var userPwd: EditText

    override fun getLayoutId(): Int {
        return R.layout.activity_createuser
    }

    override fun initView() {
        topTitle.text = getString(R.string.create_user)
        topTitle.setTextColor(resources.getColor(android.R.color.white))
        scrollView.isFillViewport = !scrollView.arrowScroll(View.FOCUS_UP)
    }

    override fun initData() {}

    @OnCheckedChanged(R.id.toggle_pwd)
    fun onChecked(isChecked: Boolean) {
        if (isChecked) {
            userPwd.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else userPwd.inputType = (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
        userPwd.setSelection(userPwd.text.toString().length)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.next) {
            checkInput()
        } else super.onClick(v)
    }

    private fun checkInput() {
        val pwd = userPwd.text.toString().trim()
        if (TextUtils.isEmpty(pwd)) {
            showToast("密码不能为空")
            return
        } else if (pwd.length < 8) {
            showToast("密码强度不够，至少8位")
            return
        } else if (PatternUtil.isDoubleChar(pwd)) {
            showToast("密码不能含有中文字符")
            return
        }
        var name = userName.text.toString().trim()
        if (TextUtils.isEmpty(name)) name = "MYID01"
        var account = userAccount.text.toString().trim()
        if (TextUtils.isEmpty(account)) account = "MYACCOUNT01"
        Thread(object : Runnable, AbsWeakContext<CreateUserActivity>(this) {
            override fun run() {
                var words = arrayOfNulls<String>(2)
                val addressUtil = AddressUtil(getWeakContext()?.filesDir.toString())
                words[0] = addressUtil.genIdentify(addressUtil.createWord(), pwd)
                words[1] = " "//genWallet(pwd)
                runOnUiThread(Runnable {
                    if (TextUtils.isEmpty(words[0])) {
                        getWeakContext()?.showToast("身份助记词生成失败")
                        return@Runnable
                    }
                    if (TextUtils.isEmpty(words[1])) {
                        getWeakContext()?.showToast("钱包助记词生成失败")
                        return@Runnable
                    }
                    getWeakContext()?.let {
                        var bean = UserBean(userName = name, accountName = account, address = addressUtil.address[0]!!)
                        val userDao = LocalDBManager(it).getTableOperation(UserOperaDao::class.java)
                        userDao.insertUser(bean)
//                        bean.address = addressUtil.address[1]!!
//                        bean.type = 1
//                        userDao.insertUser(bean)
                        getSharedPreferences(Constant.configFileName, MODE_PRIVATE)
                                .edit()
                                .putBoolean(Constant.isFirstUse, false)
                                .apply()
                        it.startActivity(Intent(it, ShowWordActivity::class.java)
                                .putExtra("words", words))
                        it.finish()
                    }
                })
            }
        }).start()
    }
}