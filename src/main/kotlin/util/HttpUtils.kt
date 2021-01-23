package util

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit


const val API = "https://api.vtbs.moe/v1/detail"

/**
 * 处理http请求的工具类
 */
object HttpUtils {
    private val client = OkHttpClient.Builder()
        .callTimeout(180, TimeUnit.SECONDS)
        .connectTimeout(180, TimeUnit.SECONDS)
        .readTimeout(180, TimeUnit.SECONDS)
        .writeTimeout(180, TimeUnit.SECONDS)
        .build()

    /**
     * @param request 传入请求
     * @return 返回响应
     */
    fun execNewCall(request: Request): Response? {
        return try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful)
                response
            else null
        } catch (e: Exception) {
            Log.e(e)
            null
        }
    }
}