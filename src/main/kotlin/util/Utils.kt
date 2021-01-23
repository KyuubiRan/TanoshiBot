package util

import com.alibaba.fastjson.JSONArray
import net.mamoe.mirai.Bot

lateinit var bot: Bot

//指定配置文件目录 打包时可更改
const val FILE_PATH = ""

fun JSONArray.hasObject(obj: Any): Boolean {
    for (o in this) {
        if (obj == o) return true
    }
    return false
}