package net.mcpes.foundhi.core.stage

/**
 * FoundHi
 *
 * @author WetABQ Copyright (c) 2018.07
 * @version 1.0
 */
enum class StageState(private val mode: Int) {
    PRE_START(1),
    START(2),
    FINNISH(3),
    SCORE(4),
    STOP(5);

    fun toInt(): Int {
        return mode
    }

    companion object {
        fun toState(s: Int): StageState? {
            for (state in StageState.values()) {
                if (state.mode == s) {
                    return state
                }
            }
            return null
        }
    }
}