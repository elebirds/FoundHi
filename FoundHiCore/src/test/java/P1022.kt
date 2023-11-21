/**
 * NOIP竞赛练习
 *
 * @author hhm Copyright (c) 2018/7/29
 * version 1.0
 */
fun main(array: Array<String>) {
    val equation = readLine()!!
    val t = equation.split("=")//分为左右两部分
    //然后要把数都移右边去，左边只保留未知数与他的系数
    val left = t[0]
    val right = t[1]
    val eq = arrayListOf<String>()
    val nums = arrayListOf<String>()
    var i = 0
    left.forEach a@{
        if (i != 0) {
            if (it in 'a'..'z') {
                var s = it.toString()
                l@ for (j in i..0) {
                    if (left[j] in '0'..'9') {
                        s = left[j] + s
                    } else if (left[j] == '+' || left[j] == '-') {
                        s = left[j] + s
                        return@l
                    }
                }
                eq.add(s)
            } else if (it in '0'..'9') {
                var s = it.toString()
                val j = i - 1
                if (left[j] == '+' || left[j] == '-') {
                    s = left[j] + s
                } else {
                    return@a
                }
                l@ for (h in i + 1..left.length) {
                    if (left[h] in '0'..'9') {
                        s += left[h]
                    } else {
                        return@l
                    }
                }
                nums.add(s)
            }
        } else {
            when (it) {
                in 'a'..'z' -> {
                    val s = it.toString()
                    eq.add(s)
                }
                in '0'..'9' -> {
                    var s = "+" + it.toString()
                    l@ for (h in i + 1..left.length) {
                        when {
                            left[h] in '0'..'9' -> s += left[h]
                            it in 'a'..'z' -> {
                                s += it
                                eq.add(s)
                                return@a
                            }
                            else -> return@l
                        }
                    }
                    nums.add(s)
                }
                else -> {
                    var s = it.toString()
                    l@ for (h in i + 1..left.length) {
                        when {
                            left[h] in '0'..'9' -> s += left[h]
                            it in 'a'..'z' -> {
                                s += it
                                eq.add(s)
                                return@a
                            }
                            else -> return@l
                        }
                    }
                    nums.add(s)
                }
            }
        }
        i++
    }

    print("f")
}