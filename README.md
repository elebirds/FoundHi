# FoundHi
Summit服务器核心
## 模块列表
- FoundHiCore 核心模块
- FoundHiPlugin 插件
  - 生存
    - SLand 地皮
  - 通用
    - SMultiWorld 多世界
    - SLogin 登录
    - SSettings 服务器GUI设置 未完成
    - SMessage 跨服消息同步
    - SItemMenu 物品目录
    - SMap 展示图片 未完成
    - SNPC NPC
    - STask 任务 未完成
    - STeam 小队 未完成
  - 大厅
    - SMusic 红石音乐
  - 游戏
    - SBedWars 起床战争
    - SSkyWars 空岛战争
    - SHotPotato 烫手的山芋 未完成
    - SHideAndSeek 动物躲猫猫 未完成
  - RPG
    - SRPG RPG
## 使用方法
  > 1.使用Maven打包Core模块。打包位置为项目根目录build文件夹的:<br/>FoundHiCore-1.0.0.jar。
    <br/>
    2.使用java -jar 命令运行Core模块,并将Core模块复制到每个Nukkit服务器文件夹
    <br/>
    3.打包其他要使用的模块,将他们复制到Nukkit的插件文件夹

## 配置项目
  > 1.从Coding [FoundHiProject](https://coding.net/t/mcpes/p/FoundHi) [Git](https://git.coding.net/mcpes/FoundHi.git) Clone代码
    <br/>
    2.使用IntelliJ IDEA 打开项目 导入模块与pom.xml文件
    <br/>
    3.在Run/Debug Configurations 新建Maven运行程序,工作目录为Core模块目录,运行命令为install,新建成功后运行
    <br/>
    4.Enjoy!

## 任务
  > SShop 商店 - Yui
    <br/>
    SRPG RPG - Yui
    <br/>
    STeam 小队 - hhm
    <br/>
    SGuild 公会(集成在Core模块) - Yui
    <br/>
    GameStage Stage - WetABQ
    <br/>
    FoundHiCore 核心 - hhm
    <br/>