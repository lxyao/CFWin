package com.cfwin.cfwinblockchain.adapter.mail

import android.content.Context
import android.view.View
import android.widget.CheckBox
import butterknife.BindView
import com.cfwin.base.adapter.ImplBaseAdapter
import com.cfwin.cfwinblockchain.R
import com.cfwin.cfwinblockchain.adapter.SubBaseViewHolder

class SecretAdapter(context: Context, data: MutableList<String>, private val checkedId: Int = -1): ImplBaseAdapter<String>(context, data) {

    override fun getLayoutId(position: Int): Int {
        return R.layout.item_list_secret
    }

    override fun getHolder(position: Int, view: View?): BaseViewHolder? {
        var holder: ViewHolder? = null
        if(view != null){
            val tag = view?.tag
            holder = tag as? ViewHolder ?: ViewHolder(view, getLayoutId(position))
        }
        return holder
    }

    override fun <B : BaseViewHolder> showView(position: Int, holder: B) {
        val tmp = holder as ViewHolder
        tmp.checked.text = data[position]
        tmp.checked.isChecked = (position == checkedId)
    }

    class ViewHolder(view: View, resId: Int): SubBaseViewHolder(view, resId){
        @BindView(R.id.item_txt)lateinit var checked: CheckBox
    }
}

