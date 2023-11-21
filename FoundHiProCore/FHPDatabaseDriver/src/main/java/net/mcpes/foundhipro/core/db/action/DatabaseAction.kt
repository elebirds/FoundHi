package net.mcpes.foundhipro.core.db.action

import net.mcpes.foundhipro.core.db.callback.DatabaseCallback
import net.mcpes.foundhipro.core.db.io.FHPBaseIO

/**
 * FoundHiPro
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
abstract class DatabaseAction(baseDao: FHPBaseIO) {

    /**
     * Dao实例
     */
    var dao = baseDao

    /**
     * 执行指定数据库操作
     */
    abstract fun execute()

    /**
     * 注意这里的callBack 应该为 ActionCallBack + CustomCallBack
     * 不是说有CustomCallBack就可以不要定义ActionCallBack,这是很有必要的
     */
    open fun callback(dbCallback: DatabaseCallback?) {
        dbCallback?.callBack(this)
    }

}