package com.cfwin.cfwinblockchain.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * 主界面 底部菜单栏 导航切换
 */
class HomePageAdapter constructor(private val data: List<Fragment>, fm :FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return data.get(position)
    }

    override fun getCount(): Int {
        return data.size
    }
}