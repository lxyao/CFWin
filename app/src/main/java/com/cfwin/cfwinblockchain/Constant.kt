package com.cfwin.cfwinblockchain

object Constant {
    /**
     * 配置文件名
     */
    const val configFileName = "appConfig"
    /**
     * 是否第一次使用app
     */
    const val isFirstUse = "isFirstUse"
    /**
     * 当前登录账户
     */
    const val CURRENT_ACCOUNT = "account"
    /**
     * 服务api地址
     */
    const val SERVICE_API = "serverApi"
    /**
     * 分页条数
     */
    const val PAGE_SIZE = 20
    /**
     * 数据库名称
     */
    const val DB_NAME = "db_block_data"
    /**
     * 数据库版本号
     */
    const val DB_VERSION = 4

    object API{
        /**
         * 糖果列表
         */
        const val CANDY_LIST = "/api/Candy/GetCandies?addr="
        /**
         * 糖果详情
         */
        const val CANDY_DETAIL = "/Home/CandyDetail?id="
        /**
         * 日志随机数
         */
        const val LOG_RAND = "/token/option_previous"
        /**
         * 日志列表
         */
        const val LOG_LIST = "/loginrecord/queryLogByAddress"
        /**
         * 账户积分
         */
        const val ACCOUNT_MONEY = "/api/querybalance"
        /**
         * 账户历史记录
         */
        const val ACCOUNT_HISTORY = "/api/queryhistory"
        /**
         * 账户交易明细
         */
        const val ACCOUNT_TRANS_DETAIL = "/api/querytx"
        /**
         * 账户交易
         */
        const val ACCOUNT_TRANS = "/api/transaction"
        /**
         * 服务器地址类型 积分
         */
        const val TYPE_SCORE = 0
        /**
         * 服务器地址类型 糖果
         */
        const val TYPE_CANDY = 1
        /**
         * 服务器地址类型 日志
         */
        const val TYPE_LOG = 2
    }
}