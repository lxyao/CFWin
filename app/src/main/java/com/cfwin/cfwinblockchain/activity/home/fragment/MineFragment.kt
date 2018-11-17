package com.cfwin.cfwinblockchain.activity.home.fragment

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.AbsParentBaseActivity
import com.cfwin.cfwinblockchain.activity.SubBaseFragment
import com.cfwin.cfwinblockchain.activity.mine.AboutUsActivity
import com.cfwin.cfwinblockchain.activity.mine.MsgCenterActivity
import com.cfwin.cfwinblockchain.activity.mine.SettingsActivity
import com.cfwin.cfwinblockchain.beans.UserBean

/**
 * 主界面 - 我布局
 */
class MineFragment : SubBaseFragment() {

    @BindView(R.id.userIcon)
    lateinit var userIcon: ImageView
    @BindView(R.id.userName)
    lateinit var userName: TextView
    @BindView(R.id.mine_msg)
    lateinit var msgCenter: TextView
    @BindView(R.id.mine_us)
    lateinit var aboutUs: TextView
    @BindView(R.id.mine_settings)
    lateinit var settings: TextView
    private lateinit var user: UserBean

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initView() {
        super.initView()
        msgCenter.text = "  ${msgCenter.text}"
        aboutUs.text = "  ${aboutUs.text}"
        settings.text = "  ${settings.text}"
        initData()
    }

    override fun initData() {
        if(mContext is AbsParentBaseActivity){
            user = (mContext as AbsParentBaseActivity).getUser()
        }
    }

    override fun onResume() {
        super.onResume()
        userName.text = user.userName
    }

    @OnClick(R.id.mine_msg, R.id.mine_us, R.id.mine_settings, R.id.ll_msg, R.id.ll_about, R.id.ll_set)
    override fun onClick(v: View?) {
        when(v?.id){
            in arrayOf(R.id.mine_msg, R.id.ll_msg)->{
                startActivity(Intent(mContext, MsgCenterActivity::class.java))
            }
            in arrayOf(R.id.mine_us, R.id.ll_about)->{
                startActivity(Intent(mContext, AboutUsActivity::class.java))
            }
            in arrayOf(R.id.mine_settings, R.id.ll_set)->{
                startActivity(Intent(mContext, SettingsActivity::class.java))
            }
            else -> super.onClick(v)
        }
    }
}