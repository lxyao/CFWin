package com.cfwin.cfwinblockchain.activity.mine

import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseActivity
import com.cfwin.cfwinblockchain.activity.mine.fragment.PushFragment
import com.cfwin.cfwinblockchain.activity.mine.fragment.SysFragment
import com.cfwin.cfwinblockchain.adapter.HomePageAdapter

/**
 * 消息中心界面
 */
class MsgCenterActivity : SubBaseActivity(), ViewPager.OnPageChangeListener{

    @BindView(R.id.tv_sys)
    lateinit var tvSys: TextView
    @BindView(R.id.tv_push)
    lateinit var tvPush: TextView
    @BindView(R.id.sys_line)
    lateinit var sysLine: View
    @BindView(R.id.push_line)
    lateinit var pushLine: View
    @BindView(R.id.viewPager)
    lateinit var viewPager: ViewPager

    override fun getLayoutId(): Int {
        return R.layout.activity_msg_center
    }

    override fun initView() {
        setTopNavBackground()
        viewPager.addOnPageChangeListener(this)
        topTitle.text = getString(R.string.message_center)
    }

    override fun initData() {
        val tmp = listOf(SysFragment(), PushFragment())
        viewPager.adapter = HomePageAdapter(tmp, supportFragmentManager)
        controlShow(0)
    }

    override fun onPageScrollStateChanged(p0: Int) {}

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

    override fun onPageSelected(p0: Int) {
        controlShow(p0)
    }

    @OnClick(R.id.tv_sys, R.id.tv_push)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_sys -> {
                viewPager.currentItem = 0
                controlShow(viewPager.currentItem)
            }
            R.id.tv_push -> {
                viewPager.currentItem = 1
                controlShow(viewPager.currentItem)
            }
            else -> super.onClick(v)
        }
    }

    private fun controlShow(position :Int){
        if(position == 0){
            sysLine.visibility = View.VISIBLE
            pushLine.visibility = View.INVISIBLE
        }else{
            sysLine.visibility = View.INVISIBLE
            pushLine.visibility = View.VISIBLE
        }
    }
}