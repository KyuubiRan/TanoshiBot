package sender

import LiveInfo
import com.alibaba.fastjson.JSONArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.mamoe.mirai.message.data.AtAll
import net.mamoe.mirai.message.data.MessageChainBuilder
import util.ConfigManager
import util.ConfigManager.CFG_LIVE_INFO
import util.ConfigManager.CFG_NEEDS_PUSH
import util.ConfigManager.CFG_TARGET_V
import util.Log
import util.bot

class AutoPush {
    init {
        bot.launch {
            while (true) {
                try {
                    //此处修改查询延迟 强烈建议不小于60秒!!!
                    delay(60000L)
                    Log.i("Get live info...")
                    val pushList = (ConfigManager.getOrNull(ConfigManager.CFG_PUSH_LIST) ?: JSONArray()) as JSONArray
                    if (pushList.isEmpty()) {
                        ConfigManager.putBool(CFG_NEEDS_PUSH, true)
                        continue
                    }
                    val mid = ConfigManager.getString(CFG_TARGET_V)
                    //此处修改vtb的uid 注意是uid!!不是房间号!!! 也可以修改配置文件(config.json)中的target_v
                    val vtb = LiveInfo.get(if (mid.isNotEmpty()) mid else "39267739")
                    ConfigManager.put(CFG_LIVE_INFO, vtb?.toJSONObject())
                    when {
                        vtb?.liveStatus != 1 -> {
                            ConfigManager.putBool(CFG_NEEDS_PUSH, true)
                        }
                        ConfigManager.getBool(CFG_NEEDS_PUSH) && vtb.liveStatus == 1 -> {
                            sendMsg(vtb)
                            ConfigManager.putBool(CFG_NEEDS_PUSH, false)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(e)
                }
            }
        }
    }

    private suspend fun sendMsg(vtb: LiveInfo?) = withContext(Dispatchers.IO) {
        val pushList = (ConfigManager.getOrNull(ConfigManager.CFG_PUSH_LIST) ?: JSONArray()) as JSONArray
        if (pushList.isEmpty() || !ConfigManager.getBool(CFG_NEEDS_PUSH)) return@withContext
        if (vtb == null) {
            Log.i("Vtb info is null,will not send msg.")
            return@withContext
        }
        for (group in pushList) {
            try {
                val msg = MessageChainBuilder()
                //此处会@全体成员 不需要请注释掉下面AtAll那一行 并且去掉第二行的\n换行符
                msg.add(AtAll)
                msg.add("\n大家的爱抖露${vtb.uname}开播啦~\n$vtb")
                bot.getGroup(group.toString().toLong()).sendMessage(msg.build())
                Log.i("Now push in $group")
            } catch (e: Exception) {
                Log.e(e, "Send message error!")
            }
            //此处修改群发间隔 同样不建议太小 容易被ban
            delay(3000L)
        }
    }
}