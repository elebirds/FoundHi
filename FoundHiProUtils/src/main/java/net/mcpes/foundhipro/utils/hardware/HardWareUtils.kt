package net.mcpes.foundhipro.utils.hardware

import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.InputStreamReader
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

object HardWareUtils {
    /**
     * 主板序列号
     */
    val motherboardSN: String
        get() {
            val result = StringBuilder()
            try {
                val file = File.createTempFile("realhowto", ".vbs")
                file.deleteOnExit()
                val fw = FileWriter(file)
                val vbs = ("Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                        + "Set colItems = objWMIService.ExecQuery _ \n"
                        + "   (\"Select * from Win32_BaseBoard\") \n"
                        + "For Each objItem in colItems \n"
                        + "    Wscript.Echo objItem.SerialNumber \n"
                        + "    exit for  ' do the first cpu only! \n" + "Next \n")

                fw.write(vbs)
                fw.close()
                val path = file.path.replace("%20", " ")
                val p = Runtime.getRuntime().exec(
                        "cscript //NoLogo $path")
                val input = BufferedReader(InputStreamReader(
                        p.inputStream))
                var line = input.readLine()
                while (line != null) {
                    result.append(line)
                    line = input.readLine()
                }
                input.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return result.toString().trim { it <= ' ' }
        }

    /**
     * CPU序列号
     */
    val cpuSerial: String
        get() {
            var result = StringBuilder()
            try {
                val file = File.createTempFile("tmp", ".vbs")
                file.deleteOnExit()
                val fw = java.io.FileWriter(file)
                val vbs = ("Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                        + "Set colItems = objWMIService.ExecQuery _ \n"
                        + "   (\"Select * from Win32_Processor\") \n"
                        + "For Each objItem in colItems \n"
                        + "    Wscript.Echo objItem.ProcessorId \n"
                        + "    exit for  ' do the first cpu only! \n" + "Next \n")
                fw.write(vbs)
                fw.close()
                val path = file.path.replace("%20", " ")
                val p = Runtime.getRuntime().exec(
                        "cscript //NoLogo $path")
                val input = BufferedReader(InputStreamReader(
                        p.inputStream))
                var line = input.readLine()
                while (line != null) {
                    result.append(line)
                    line = input.readLine()
                }
                input.close()
                file.delete()
            } catch (e: Exception) {
                e.fillInStackTrace()
            }

            if (result.toString().trim { it <= ' ' }.isEmpty()) {
                result = StringBuilder("无CPU_ID被读取")
            }
            return result.toString().trim { it <= ' ' }
        }

    val localHostLANAddress: List<String>
        @Throws(SocketException::class)
        get() {
            val ips = ArrayList<String>()
            val interfs = NetworkInterface.getNetworkInterfaces()
            while (interfs.hasMoreElements()) {
                val interf = interfs.nextElement()
                val addres = interf.inetAddresses
                while (addres.hasMoreElements()) {
                    val `in` = addres.nextElement()
                    if (`in` is Inet4Address) {
                        if ("127.0.0.1" != `in`.getHostAddress()) {
                            ips.add(`in`.getHostAddress())
                        }
                    }
                }
            }
            return ips
        }

    /**
     * MAC
     * 通过jdk自带的方法,先获取本机所有的ip,然后通过NetworkInterface获取mac地址
     */
    val mac: List<String>
        get() {
            val ms = ArrayList<String>()
            try {
                val ls = localHostLANAddress
                for (str in ls) {
                    val ia = InetAddress.getByName(str)
                    val mac = NetworkInterface.getByInetAddress(ia).hardwareAddress
                    val sb = StringBuilder()
                    for (i in mac.indices) {
                        if (i != 0) {
                            sb.append("-")
                        }
                        val s = Integer.toHexString(mac[i].toInt())
                        sb.append(if (s.length == 1) 0.toString() + s else s)
                    }
                    ms.add(sb.toString().toUpperCase())
                }
                return ms
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return ms
        }

    /**
     * 获取CPUID、硬盘序列号、MAC地址、主板序列号
     */
    val all: Map<String, Any>
        get() {
            var os = System.getProperty("os.name")
            os = os.toUpperCase()
            val snVo = HashMap<String, Any>()
            if ("LINUX" == os) {
                val cpuid = getSerialNumber("dmidecode -t processor | grep 'ID'", "ID", ":")
                val mainboardNumber = getSerialNumber("dmidecode |grep 'Serial Number'", "Serial Number", ":")
                val diskNumber = getSerialNumber("fdisk -l", "Disk identifier", ":")
                val mac = getSerialNumber("ifconfig -a", "ether", " ")
                snVo["cpuid"] = cpuid.toUpperCase().replace(" ", "")
                snVo["diskid"] = diskNumber.toUpperCase().replace(" ", "")
                snVo["mac"] = mac.toUpperCase().replace(" ", "")
                snVo["mainboard"] = mainboardNumber.toUpperCase().replace(" ", "")
            } else {
                val cpuid = cpuSerial
                val mainboard = motherboardSN
                val disk = getHardDiskSN("c")
                val mac = mac
                snVo["cpuid"] = cpuid.toUpperCase().replace(" ", "")
                snVo["diskid"] = disk.toUpperCase().replace(" ", "")
                snVo["mac"] = mac
                snVo["mainboard"] = mainboard.toUpperCase().replace(" ", "")
            }
            return snVo
        }

    /**
     * 获取硬盘序列号(该方法获取的是 盘符的逻辑序列号,并不是硬盘本身的序列号)
     * 硬盘序列号还在研究中
     * @param drive 盘符
     */
    fun getHardDiskSN(drive: String): String {
        val result = StringBuilder()
        try {
            val file = File.createTempFile("realhowto", ".vbs")
            file.deleteOnExit()
            val fw = FileWriter(file)
            val vbs = ("Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    + "Set colDrives = objFSO.Drives\n"
                    + "Set objDrive = colDrives.item(\""
                    + drive
                    + "\")\n"
                    + "Wscript.Echo objDrive.SerialNumber") // see note
            fw.write(vbs)
            fw.close()
            val path = file.path.replace("%20", " ")
            val p = Runtime.getRuntime().exec(
                    "cscript //NoLogo $path")
            val input = BufferedReader(InputStreamReader(
                    p.inputStream))
            var line = input.readLine()
            while (line != null) {
                result.append(line)
                line = input.readLine()
            }
            input.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result.toString().trim { it <= ' ' }
    }

    /***************************linux */
    fun executeLinuxCmd(cmd: String): String {
        try {
            val run = Runtime.getRuntime()
            val process: Process
            process = run.exec(cmd)
            val `in` = process.inputStream
            val out = StringBuilder()
            val b = ByteArray(8192)
            var n = `in`.read(b)
            while (n != -1) {
                out.append(String(b, 0, n))
                n = `in`.read(b)
            }
            `in`.close()
            process.destroy()
            return out.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     *
     * @param cmd 命令语句
     * @param record 要查看的字段
     * @param symbol 分隔符
     */
    fun getSerialNumber(cmd: String, record: String, symbol: String): String {
        val execResult = executeLinuxCmd(cmd)
        val infos = execResult.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var i: String
        for (info in infos) {
            i = info.trim { it <= ' ' }
            if (i.contains(record)) {
                i = i.replace(" ", "")
                val sn = i.split(symbol.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                return sn[1]
            }
        }
        return ""
    }
}