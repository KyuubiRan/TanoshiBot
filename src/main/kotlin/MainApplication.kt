import listener.InitListener
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.join
import sender.InitSender
import util.ConfigManager
import util.ConfigManager.CFG_QQ_ACCOUNT
import util.ConfigManager.CFG_QQ_PASSWORD
import util.bot

suspend fun main() {
    val qqId = ConfigManager.getLong(CFG_QQ_ACCOUNT)
    val password = ConfigManager.getString(CFG_QQ_PASSWORD)
    val miraiBot = Bot(qqId, password) {
        fileBasedDeviceInfo()
        inheritCoroutineContext()
    }
    miraiBot.alsoLogin()
    botInit(miraiBot)
    miraiBot.join()
}

private fun botInit(miraiBot: Bot) {
    bot = miraiBot
    InitSender()
    InitListener()
}
