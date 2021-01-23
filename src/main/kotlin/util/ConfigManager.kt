package util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import java.io.File

object ConfigManager {
    const val CFG_QQ_ACCOUNT = "account"               //T:Long        D:0L
    const val CFG_QQ_PASSWORD = "password"             //T:String      D:""
    const val CFG_PUSH_LIST = "push_list"              //T:Array       D:[]
    const val CFG_NEEDS_PUSH = "needs_push"            //T:Boolean     D:false
    const val CFG_LIVE_INFO = "live_info"              //T:JSONObject  D:""

    private val cfgFile = File("${FILE_PATH}config.json")
    private lateinit var config: JSONObject

    init {
        configInit()
    }

    //初始化
    private fun configInit() {
        if (!cfgFile.exists()) {
            cfgFile.createNewFile()
            save(JSONObject().toJSONString())
        }
        load()
        fun input(msg: String): String {
            while (true) {
                Log.i("Please enter your$msg:")
                val str = readLine().toString()
                if (str.isEmpty()) {
                    Log.i("Content must not be null!! Please re-input:")
                    continue
                }
                Log.i("Confirm your input:$str Is it right?y/n")
                while (true) {
                    val confirm = readLine().toString().toLowerCase()
                    if (confirm == "y" || confirm == "yes") {
                        return str
                    } else if (confirm == "n" || confirm == "no") {
                        Log.i("Please re-input:")
                        break
                    } else {
                        Log.i("Please input yes/y or no/n!")
                    }
                }
            }
        }
        if (getLong(CFG_QQ_ACCOUNT) == 0L) {
            Log.i("Bot account not found,please input now.")
            while (true) {
                try {
                    val str = input("Account")
                    if (str.length in 6..10) {
                        putLong(CFG_QQ_ACCOUNT, str.toLong())
                        break
                    } else {
                        Log.i("Wrong account!Please check and re-input!")
                    }
                } catch (e: Exception) {
                    Log.i("Error!")
                }
            }
            val password = input("password")
            putString(CFG_QQ_PASSWORD, password)
        }
        save()
    }

    /**
     * @param key 键值 要拿什么
     * @param defValue 指如果不存在 默认返回什么
     * @return 返回config中的key值
     */

    fun getIntOrDef(key: String, defValue: Int): Int {
        return try {
            config.getInteger(key)
        } catch (e: Exception) {
            defValue
        }
    }

    fun getLongOrDef(key: String, defValue: Long): Long {
        return try {
            config.getLong(key)
        } catch (e: Exception) {
            defValue
        }
    }

    fun getBoolOrDef(key: String, defValue: Boolean): Boolean {
        return try {
            config.getBoolean(key)
        } catch (e: Exception) {
            defValue
        }
    }

    fun getStringOrDef(key: String, defValue: String): String {
        return try {
            config.getString(key)
        } catch (e: Exception) {
            defValue
        }
    }

    fun getOrNull(key: String): Any? {
        return config[key]
    }

    fun getInt(key: String): Int {
        return getIntOrDef(key, 0)
    }

    fun getLong(key: String): Long {
        return getLongOrDef(key, 0L)
    }

    fun getBool(key: String): Boolean {
        return getBoolOrDef(key, false)
    }

    fun getString(key: String): String {
        return getStringOrDef(key, "")
    }

    /**
     * @param key 键值 要设置什么
     * @param value 要设置的值
     */

    fun <T> put(key: String, value: T) {
        config[key] = value
        save()
    }

    fun putBool(key: String, value: Boolean) {
        config[key] = value
        save()
    }

    fun putInt(key: String, value: Int) {
        config[key] = value
        save()
    }

    fun putLong(key: String, value: Long) {
        config[key] = value
        save()
    }

    fun putString(key: String, value: String) {
        config[key] = value
        save()
    }

    private fun load() {
        try {
            config = JSON.parseObject(cfgFile.readText())
        } catch (e: Exception) {
            Log.e(e)
        }
    }

    fun reload() {
        load()
    }

    private fun save() {
        save(config.toJSONString())
    }

    private fun save(text: String) {
        try {
            cfgFile.writeText(text)
        } catch (e: Exception) {
            Log.e(e)
        }
    }
}