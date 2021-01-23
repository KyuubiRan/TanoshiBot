package listener

import com.alibaba.fastjson.JSONArray
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.content
import util.ConfigManager
import util.ConfigManager.CFG_PUSH_LIST
import util.bot
import util.hasObject

class TogglePush {
    init {
        subscribeEvent()
    }

    private fun subscribeEvent() {
        bot.subscribeAlways<GroupMessageEvent> {
            if (sender.permission.level >= 1) {
                when (message.content) {
                    "/on" -> {
                        val lists = (ConfigManager.getOrNull(CFG_PUSH_LIST) ?: JSONArray()) as JSONArray
                        val id = group.id.toString()
                        if (lists.hasObject(id)) {
                            group.sendMessage("本群推送已存在!")
                        } else {
                            lists.add(id)
                            ConfigManager.put(CFG_PUSH_LIST, lists)
                            group.sendMessage("添加本群推送成功!")
                        }
                    }
                    "/off" -> {
                        val lists = (ConfigManager.getOrNull(CFG_PUSH_LIST) ?: JSONArray()) as JSONArray
                        val id = group.id.toString()
                        if (lists.hasObject(id)) {
                            lists.remove(id)
                            ConfigManager.put(CFG_PUSH_LIST, lists)
                            group.sendMessage("移除本群推送成功!")
                        } else {
                            group.sendMessage("本群并没有添加推送!")
                        }
                    }
                }
            }
        }
    }
}