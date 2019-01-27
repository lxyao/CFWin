package com.cfwin.cfwinblockchain.activity.mail.fragment.abs

/**
 * 邮箱配置数据是否改变
 */
interface IDataChangeCallback {
    fun isChanged(): Boolean{ return false }

    fun setCancel(isCancel: Boolean)

    fun saveData(type: Int)
}