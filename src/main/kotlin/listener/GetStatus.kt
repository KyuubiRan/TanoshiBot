package listener

import LiveInfo
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.content
import util.ConfigManager
import util.ConfigManager.CFG_LIVE_INFO
import util.bot

class GetStatus {
    init {
        subscribeEvent()
    }

    private fun subscribeEvent() {
        bot.subscribeAlways<MessageEvent> {
            if (message.content == "/s") {
                val vtb = LiveInfo(ConfigManager.getString(CFG_LIVE_INFO))
                sender.sendMessage(
                    "爱抖露${vtb.uname}的信息如下:\n$vtb"
                )
            }
        }
    }
}