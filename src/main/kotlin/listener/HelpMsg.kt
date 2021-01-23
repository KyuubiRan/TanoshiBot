package listener

import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.content
import util.Log
import util.bot

class HelpMsg {
    init {
        subscribeEvent()
    }

    private val help = """
        [帮助菜单]
        /on 打开本群的推送
        /off 关闭本群的推送
        /s 直播间状态
        /h 帮助
    """.trimIndent()

    private fun subscribeEvent() {
        bot.subscribeAlways<MessageEvent> {
            if (message.content == "/h") {
                sender.sendMessage(help)
            }
        }
    }
}