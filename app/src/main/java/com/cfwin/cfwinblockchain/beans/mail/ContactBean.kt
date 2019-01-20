package com.cfwin.cfwinblockchain.beans.mail

/**
 * 联系人实体对象
 */
data class ContactBean(var id: Int = -1,
                       var nickName: String,
                       var mail: String,
                       var friendId: String = "",
                       var pgpKey: String= "",
                       var indexChar: String="") {
}