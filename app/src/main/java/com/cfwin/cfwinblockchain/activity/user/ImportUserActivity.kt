package com.cfwin.cfwinblockchain.activity.user

import android.app.Activity
import android.content.Intent
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import butterknife.BindView
import butterknife.OnCheckedChanged
import com.cfwin.base.utils.PatternUtil
import com.cfwin.base.weak.AbsWeakContext
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.activity.home.MainActivity
import com.cfwin.cfwinblockchain.beans.UserBean
import com.cfwin.cfwinblockchain.db.LocalDBManager
import com.cfwin.cfwinblockchain.db.tables.UserOperaDao
import com.cfwin.cfwinblockchain.utils.AddressUtil

/**
 * 导入身份
 */
const val ADD_IDENTIFY = 0
/**
 * 添加账户
 */
const val ADD_ACCOUNT = 1

/**
 * 恢复身份 界面
 */
class ImportUserActivity : SubBaseActivity() {

    @BindView(R.id.login_word)lateinit var loginWord:EditText
    @BindView(R.id.userPwd)lateinit var userPwd:EditText

    private var wordType: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_import_user
    }

    override fun initView() {
        topTitle.text = getString(R.string.import_user)
    }

    override fun initData() {
        wordType = intent.getIntExtra("type", 0)
    }

    @OnCheckedChanged(R.id.toggle_pwd)
    fun onChecked(isChecked: Boolean){
        if(isChecked){
            userPwd.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }else userPwd.inputType = (InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT)
        userPwd.setSelection(userPwd.text.toString().length)
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.next){
            checkInput()
        }else super.onClick(v)
    }

    private fun checkInput(){
        val word = loginWord.text.toString().trim()
        if(TextUtils.isEmpty(word)){
            showToast("请输入助记词")
            return
        }
        val pwd = userPwd.text.toString().trim()
        if(TextUtils.isEmpty(pwd)){
            showToast("密码不能为空")
            return
        }else if(pwd.length < 8){
            showToast("密码强度不够，至少8位")
            return
        }else if(PatternUtil.isDoubleChar(pwd)){
            showToast("密码不能含有中文字符")
            return
        }
        create(word, pwd, filesDir.absolutePath)
    }

    private fun create(word: String, pwd: String, filesDir: String){
        Thread(object: Runnable, AbsWeakContext<ImportUserActivity>(this) {
            override fun run() {
                val util = AddressUtil(filesDir)
                if(wordType == ADD_IDENTIFY)util.genIdentify(word, pwd)
                else if(wordType == ADD_ACCOUNT)util.genWallet(word, pwd)
                val address = util.address[wordType]
                runOnUiThread(Runnable {
                    if(TextUtils.isEmpty(address)){
                        getWeakContext()?.showToast(if(wordType == 0)"恢复失败" else "添加失败")
                        return@Runnable
                    }
                    getWeakContext()?.let {
                        var bean = UserBean(userName = "MY01", accountName = "MYACCOUNT01", address = address!!, type = wordType)
                        val userDao= LocalDBManager(it).getTableOperation(UserOperaDao::class.java)
                        userDao.insertUser(bean)
                        if(wordType == ADD_IDENTIFY){
                            getSharedPreferences(Constant.configFileName, MODE_PRIVATE)
                                    .edit()
                                    .putBoolean(Constant.isFirstUse, false)
                                    .apply()
                            it.startActivity(Intent(it, MainActivity::class.java))
                        }
                        else if(wordType == ADD_ACCOUNT){
                            it.setResult(Activity.RESULT_OK, Intent()
                                    .putExtra("user", bean))
                        }
                        it.finish()
                    }
                })
            }
        }).start()
    }
}