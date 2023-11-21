package net.mcpes.foundhipro.core.db.callback

import net.mcpes.foundhipro.core.db.action.DatabaseAction

/**
 * FoundHiPro
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
interface DatabaseCallback {

    fun callBack(action: DatabaseAction)

}