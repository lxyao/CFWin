package com.cfwin.cfwinblockchain.adapter.account

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import com.cfwin.base.adapter.ImplBaseAdapter
import com.cfwin.base.utils.LogUtil
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.adapter.SubBaseViewHolder
import com.cfwin.cfwinblockchain.beans.UserBean

/**
 * 账户管理数据
 */
class ManagerAdapter constructor(context: Context, data: MutableList<UserBean>)
    : ImplBaseAdapter<UserBean>(context, data) {

    override fun getLayoutId(position: Int): Int {
        return R.layout.list_item_account
    }

    fun updateBalance(bean: UserBean){
        for(user in data){
            if(bean.address == bean.address){
                user.integral = try {
                    "${(user.integral.toLong() + bean.integral.toLong())}"
                }catch (e: Exception){
                    LogUtil.e("ManagerAdapter", "金额转换异常 e= ${e.localizedMessage}")
                    "0"
                }
//                user.integral = DecimalFormat("#.00").format(user.integral)
                break
            }
        }
        notifyDataSetChanged()
    }

    override fun getHolder(position: Int, view: View?): BaseViewHolder? {
        var holder: ViewHolder? = null
        if(view != null){
            val tag = view.tag
            holder = tag as? ViewHolder ?: ViewHolder(view, getLayoutId(position))
        }
        return holder
    }

    override fun <B : BaseViewHolder> showView(position: Int, holder: B) {
        val tmpHolder = holder as ViewHolder
        val item = data[position]
        tmpHolder.title.text = item.accountName
        tmpHolder.score.text = item.integral
        val str = item.address
        tmpHolder.subTitle.text = "${str.substring(0..7)}...${str.substring(str.length - 8)}"
        if(item.type == 1){
            tmpHolder.icon.setImageResource(R.mipmap.ic_account_state_orange)
            tmpHolder.view.setBackgroundResource(R.mipmap.ic_account_orange_bg)
        }else{
            tmpHolder.icon.setImageResource(R.mipmap.ic_account_state_gray)
            tmpHolder.view.setBackgroundResource(R.mipmap.ic_account_gray_bg)
        }
    }

    open class ViewHolder constructor(view: View, resId: Int):SubBaseViewHolder(view, resId){
        @BindView(R.id.item_icon)lateinit var icon: ImageView
        @BindView(R.id.item_title)lateinit var title: TextView
        @BindView(R.id.item_subtitle)lateinit var subTitle: TextView
        @BindView(R.id.item_score)lateinit var score: TextView
        @BindView(R.id.item_bg)lateinit var itemBg: LinearLayout
    }
}