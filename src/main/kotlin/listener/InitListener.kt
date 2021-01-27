package listener

class InitListener {
    init {
        //此处自定义Listener 不需要的可以注释掉
        //on/off指令
        TogglePush()
        // status 指令(/s)
        GetStatus()
        //帮助指令(/h)
        HelpMsg()
    }
}