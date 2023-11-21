import com.xxmicloxx.NoteBlockAPI.NBSDecoder
import java.io.File

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/3
 * version 1.0
 */
fun main(s: Array<String>): Unit {
    val nbs = NBSDecoder.parse(File("C:\\Users\\cheng\\Desktop\\F.nbs"), "test")
    println(nbs)
}