package com.cfwin.cfwinblockchain.adapter

import android.view.View
import butterknife.ButterKnife
import com.cfwin.base.adapter.ImplBaseAdapter.BaseViewHolder

open class SubBaseViewHolder : BaseViewHolder {

    constructor(view: View, resId: Int):super(view, resId){
        ButterKnife.bind(this, view)
    }
}