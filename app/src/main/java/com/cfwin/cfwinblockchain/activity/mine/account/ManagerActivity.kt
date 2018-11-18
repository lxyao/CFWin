package com.cfwin.cfwinblockchain.activity.mine.account

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.ListView
import butterknife.BindView
import butterknife.OnItemClick
import com.android.volley.VolleyError
import com.cfwin.base.utils.LogUtil
import com.cfwin.cfwinblockchain.Constant
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.activity.user.ADD_ACCOUNT
import com.cfwin.cfwinblockchain.activity.user.CreateUserActivity
import com.cfwin.cfwinblockchain.activity.user.ImportUserActivity
import com.cfwin.cfwinblockchain.adapter.account.ManagerAdapter
import com.cfwin.cfwinblockchain.beans.UserBean
import com.cfwin.cfwinblockchain.beans.response.ScoreResponse
import com.cfwin.cfwinblockchain.db.LocalDBManager
import com.cfwin.cfwinblockchain.db.tables.UserOperaDao
import com.cfwin.cfwinblockchain.http.VolleyListenerInterface
import com.cfwin.cfwinblockchain.http.VolleyRequestUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 账户管理界面
 */
class ManagerActivity :SubBaseActivity() {

    @BindView(android.R.id.list)
    lateinit var listview: ListView
    private lateinit var adapter: ManagerAdapter
    private lateinit var host: String

    override fun getLayoutId(): Int {
        return R.layout.activity_account_manager
    }

    override fun initView() {
        setTopNavBackground()
        topMenu.text = getString(R.string.add_account)
        topTitle.text = getString(R.string.account_manager)
    }

    override fun initData() {
        val accountData = getAccount()
        adapter = ManagerAdapter(this, accountData)
        listview.adapter = adapter
        host = getServer(Constant.API.TYPE_SCORE)+Constant.API.ACCOUNT_MONEY
        for(bean in accountData){
            getAccountMoney(bean.address)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == 201){
            val account = getAccount()
            adapter.addData(account, true)
            getAccountMoney(data?.getStringExtra("address")!!)
        }
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.toolbar_menu){
            //添加账户
            startActivityForResult(Intent(this, ImportUserActivity::class.java)
                    .putExtra("type", ADD_ACCOUNT), 201)
        }else super.onClick(v)
    }

    @OnItemClick(android.R.id.list)
    fun onItemClick(position: Int){
        startActivity(Intent(this, DetailActivity::class.java)
                .putExtra("item", listview.adapter.getItem(position) as UserBean))
    }

    private fun getAccount(): MutableList<UserBean>{
        return LocalDBManager(this).getTableOperation(UserOperaDao::class.java).queryUser("")
    }

    private fun getAccountMoney(address: String){
        VolleyRequestUtil.RequestPost(this,
                host,
                "queryBalance",
                mapOf("address" to address),
                object :VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener){
                    override fun onMySuccess(result: String?) {
                        LogUtil.e(TAG!!, "账户信息 $result", true)
                        result?.let {
                            val balance = Gson().fromJson(it, object :TypeToken<ScoreResponse<Map<String, String>>>(){}.type) as ScoreResponse<Map<String, String>>
                            if(balance.result == balance.STATE_SUCCESS){
                                var money = balance.data!!["balance"]
                                if(TextUtils.isEmpty(money))money = "0"
                                adapter.updateBalance(UserBean(userName = "",accountName = "", address = address, integral = money!!))
                                //修改本地数据库信息
                                LocalDBManager(mContext).getTableOperation(UserOperaDao::class.java).updateIntegral(money.toLong(), address)
                            }
                        }
                    }

                    override fun onMyError(error: VolleyError?) {
                        val index = address.length shr 1
                        LogUtil.e(TAG!!, "账户信息失败 e= ${error.toString()} address=${address.substring(index)}")
                    }
                },
                false)
    }
}