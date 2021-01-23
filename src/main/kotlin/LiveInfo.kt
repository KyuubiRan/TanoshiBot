import com.alibaba.fastjson.JSONObject
import okhttp3.Request
import util.API
import util.ConfigManager
import util.HttpUtils
import java.text.SimpleDateFormat

class LiveInfo(jsonStr: String) {
    companion object {
        fun get(mid: String): LiveInfo? {
            val request = Request.Builder()
                .url("$API/$mid")
                .build()
            val res = HttpUtils.execNewCall(request)
            return try {
                LiveInfo(res?.body?.string().toString())
            } catch (e: Exception) {
                null
            }
        }
    }

    var mid = 0
        private set

    var uname = ""
        private set

    var roomid = 0
        private set

    var liveStatus = 0
        private set

    var follower = 0
        private set

    lateinit var lastLive: JSONObject
        private set

    var online = 0
        private set

    var title = ""
        private set

    var time = 0L
        private set

    val lastLiveTime by lazy {
        lastLive.getLongValue("time")
    }
    val lastLiveOnline by lazy {
        lastLive.getInteger("online")
    }

    init {
        val obj = JSONObject.parseObject(jsonStr)
        mid = obj.getInteger("mid")
        uname = obj.getString("uname")
        roomid = obj.getInteger("roomid")
        liveStatus = obj.getInteger("liveStatus")
        follower = obj.getInteger("follower")
        lastLive = obj.getJSONObject("lastLive")
        online = obj.getInteger("online")
        title = obj.getString("title")
        time = obj.getLongValue("time")
    }

    fun toJSONObject(): JSONObject {
        return JSONObject.parseObject(
            """
            {
                "mid": $mid,
                "uname": "$uname",
                "roomid": $roomid,
                "liveStatus": $liveStatus,
                "follower": $follower,
                "lastLive": ${lastLive.toJSONString()},
                "online": $online,
                "title": "$title",
                "time": $time
            }
        """.trimIndent()
        )
    }

    override fun toString(): String {
        val sb = StringBuilder()
        val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        if (liveStatus != 1) {
            sb.append("状态:未开播\n")
                .append("标题:$title\n")
                .append("上次直播日期:${fmt.format(lastLiveTime)}\n")
//                .append("上次直播人气值:$lastLiveOnline\n")
        } else {
            sb.append("状态:直播中\n")
                .append("标题:$title\n")
                .append("时间:${fmt.format(time)}\n")
//                .append("人气值:$online\n")
        }
//        sb.append("当前粉丝数:$follower\n")
        sb.append("链接:https://live.bilibili.com/$roomid")
        return sb.toString()
    }
}