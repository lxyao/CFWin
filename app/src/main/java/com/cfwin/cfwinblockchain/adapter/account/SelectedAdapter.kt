package com.cfwin.cfwinblockchain.adapter.account

import android.content.Context
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import butterknife.BindView
import com.cfwin.base.adapter.ImplBaseAdapter
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.beans.UserBean

/**
 * 账户选择数据列表
 */
class SelectedAdapter constructor(context: Context, data: MutableList<UserBean>) : ImplBaseAdapter<UserBean>(context, data) {

    private var selectedId: Int = -1

    override fun getLayoutId(position: Int): Int {
        return R.layout.list_item_account_selected
    }

    override fun getHolder(position: Int, view: View?): BaseViewHolder? {
        var holder: ViewHolder? = null
        if(view != null){
            val tag = view.tag
            holder = tag as? ViewHolder ?: ViewHolder(view, getLayoutId(position))
        }
        return holder
    }

    fun selected(position: Int){
        this.selectedId = position
        notifyDataSetChanged()
    }

    override fun <B : BaseViewHolder> showView(position: Int, tmp: B) {
        val holder = tmp as ViewHolder
        holder.cb.isChecked = (position == selectedId)
        val bean = data[position]
        if(bean.type == 1){
            //账户
            holder.title.text = bean.accountName
            holder.itemBg.setBackgroundResource(R.mipmap.ic_account_orange_bg)
            holder.icon.setImageResource(R.mipmap.ic_account_state_orange)
            holder.subTitle.text = ""
        } else{
            //身份
            holder.title.text = bean.userName
            holder.itemBg.setBackgroundResource(R.mipmap.ic_account_gray_bg)
            holder.icon.setImageResource(R.mipmap.ic_account_state_gray)
            holder.subTitle.text = context.getString(R.string.login_account_hint)
        }
        holder.score.text = bean.integral
    }

    class ViewHolder(view: View, resId: Int): ManagerAdapter.ViewHolder(view, resId){
        @BindView(R.id.item_cb)lateinit var cb: CheckBox
    }
}