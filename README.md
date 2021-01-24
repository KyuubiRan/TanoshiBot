# TanoshiBot
单V|多群 提醒单推bot

## 它是什么
它是一个QQ群机器人，可以推送V直播，使用的api为[vtbs.moe](https://vtbs.moe)，请确保需要推送的v已在观测网站里。

## 它能干什么
多群推送，精确到分钟，当然你也可以改延迟，在AutoPush的delay(60000L)改成任意时间，强烈不建议调用时间过短，请务必保证至少也要在一分钟以上(60000毫秒)

## 如何使用
1.需要一个机器人账号

2.修改AutoPush中的LiveInfo.get(mid) 将mid替换成需要推送的V的uid 注意 是uid 不是房间号 不要搞错了！！

3.shadowJar build生成一份jar 然后在命令行里输入java -jar xxxx.jar运行

4.输入账号/密码并确认

5.拉机器人进群 并且在需要推送的群里输入/on打开推送（此指令需要管理员/群主权限） 帮助菜单/h

## 为什么登录失败
请看mirai官方文档——[登录](https://github.com/mamoe/mirai/blob/dev/docs/Bots.md#2-%E7%99%BB%E5%BD%95)
