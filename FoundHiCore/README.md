# FoundHiCore
Summit服务器核心模块

##说明
包含两个模块:App与Plugin
<br/>
###App (API服务器独立程序):服务器中枢
####功能：
> 1.服务器的分配(最优大厅,游戏服务器,资源区等)
  <br/>
  2.包的接收,发送,处理
  <br/>
  3.服务器之间的协调
  <br/>
  4.数据的获取与修改
  <br/>
  5.实时监控

####Main
#####位置
net.mcpes.foundhi.core.app.FoundHiAPIMain

#####启动类:
```kotlin
package net.mcpes.foundhi.core.app

import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.utils.MainLogger

/**
 * FoundHi的启动类 包含`main`函数。<br></br>
 *
 * @author hhm Copyright (c) 2018/4/10
 * @version 1.0
 * @since FoundHi Dev Version
 */

object FoundHiAPIMain {
    val VERSION = "1.0.0-alpha.2"
    val CODENAME = "Dalao"
    val PATH = System.getProperty("user.dir") + "/"
    val DATA_PATH = System.getProperty("user.dir") + "/"
    val START_TIME = System.currentTimeMillis()
    var ANSI = true

    /**
     * FoundHi Main方法
     *
     * @author hhm
     */
    @JvmStatic
    fun main(strings: Array<String>) {
        ServerType.init(ServerType.API)
        for (string in strings) {
            when (string) {
                "disable-ansi" -> ANSI = false
            }
        }
        System.setProperty("jline.WindowsTerminal.directConsole", "false")
        val logger = MainLogger()
        try {
            APIFunction(logger, PATH, DATA_PATH)
        } catch (e: Exception) {
            logger.logException(e)
        }
    }
}

```

###Plugin (Nukkit插件端):协调APP与其他模块
####plugin.yml
```yaml
name: FoundHi
version: "1.0.0"
api: ["1.0.0"]
author: hhm
description: FoundHi
main: net.mcpes.foundhi.core.plugin.FoundHiPlugin
```
####插件Main
#####位置
net.mcpes.foundhi.core.plugin.FoundHiPlugin
#### 配置解析
#####foundhi.yml
```yaml
监听端口: 23333         #监听的端口
服务器密钥: '123456'    #服务器连接密钥
debug: true            #启用开发模式
更新best大厅时间: 1000  #更新最佳大厅时间 1s = 1000
服务器人数限制: -1      #服务器人数限制 -1为无限
```
