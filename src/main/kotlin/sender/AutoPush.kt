package sender

import LiveInfo
import com.alibaba.fastjson.JSONArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.mamoe.mirai.message.data.AtAll
import util.ConfigManager
import util.ConfigManager.CFG_LIVE_INFO
import util.ConfigManager.CFG_NEEDS_PUSH
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
                    //此处修改vtb的uid 注意是uid!!不是房间号!!!
                    val vtb = LiveInfo.get("39267739")
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
            Log.e(null, "Vtb info is null!!!")
            return@withContext
        }
        for (group in pushList) {
            try {
                bot.getGroup(group.toString().toLong()).sendMessage("${AtAll}\n大家的爱抖露${vtb.uname}开播啦~\n$vtb")
                Log.i("Now push in $group")
            } catch (e: Exception) {
                Log.e(e, "Send message error!")
            }
            //此处修改群发间隔 同样不建议太小 容易被ban
            delay(3000L)
        }
    }
}