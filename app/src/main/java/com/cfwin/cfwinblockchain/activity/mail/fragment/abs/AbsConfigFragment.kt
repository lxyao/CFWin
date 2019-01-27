package com.cfwin.cfwinblockchain.activity.mail.fragment.abs

import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.activity.SubBaseFragment
import com.cfwin.cfwinblockchain.activity.mail.fragment.imap.InFragment
import com.cfwin.cfwinblockchain.activity.mail.fragment.imap.OutFragment

/**
 * 抽象配置文件
 */
abstract class AbsConfigFragment: SubBaseFragment(), IDataChangeCallback {

    var configName = ""
    lateinit var inFragment: InFragment
    lateinit var outFragment: OutFragment

    override fun initData() {
        inFragment = (childFragmentManager?.findFragmentById(R.id.in_fragment) as InFragment?)!!
        outFragment = (childFragmentManager?.findFragmentById(R.id.out_fragment) as OutFragment?)!!
        inFragment.setConfig(configName, 0)
        outFragment.setConfig(configName, 1)
    }

    override fun saveData(type: Int) {
        inFragment.saveData(0)
        outFragment.saveData(1)
    }

    override fun isChanged(): Boolean {
        return inFragment.isChanged() || outFragment.isChanged()
    }

    override fun setCancel(isCancel: Boolean) {
        inFragment.setCancel(isCancel)
        outFragment.setCancel(isCancel)
    }
}