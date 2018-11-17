package com.cfwin.cfwinblockchain.activity.home

import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.widget.RadioButton
import android.widget.RadioGroup
import com.cfwin.base.activity.BaseActivity
import com.cfwin.base.utils.LogUtil
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.AbsParentBaseActivity
import com.cfwin.cfwinblockchain.activity.home.fragment.CandyFragment
import com.cfwin.cfwinblockchain.activity.home.fragment.HomeFragment
import com.cfwin.cfwinblockchain.activity.home.fragment.LoginLogFragment
import com.cfwin.cfwinblockchain.activity.home.fragment.MineFragment
import com.cfwin.cfwinblockchain.adapter.HomePageAdapter

/**
 * 应用主界面
 */
class MainActivity :AbsParentBaseActivity(), ViewPager.OnPageChangeListener {

    lateinit var viewPager: ViewPager
    lateinit var homeNav: RadioGroup
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        viewPager = findViewById(R.id.home_vp)
        viewPager.addOnPageChangeListener(this)
        val data = listOf<Fragment>(HomeFragment(), CandyFragment(), LoginLogFragment(), MineFragment())
        viewPager.adapter = HomePageAdapter(data, supportFragmentManager)
        homeNav = findViewById(R.id.home_nav)
        homeNav.setOnCheckedChangeListener { radioGroup, id ->
            when(id){
                R.id.rb_home->viewPager.currentItem = 0
                R.id.rb_candy->viewPager.currentItem = 1
                R.id.rb_loginLog->viewPager.currentItem = 2
                R.id.rb_mine->viewPager.currentItem = 3
            }
        }
    }

    override fun initData() {
    }

    override fun onPageScrollStateChanged(p0: Int) {}

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

    override fun onPageSelected(position: Int) {
        val radio = homeNav.getChildAt(position) as RadioButton
        radio.isChecked = true
    }
}