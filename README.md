# 功能  
1. 登录注册
2. 数据库同步
3. 收纳整理（包括图像识别和扫码识别）
4. 文章搭配爬取
5. 时间提醒
6. 展示（增删改查）

# 配置
1. **/TheFirst/app/src/main/java/com/example/administrator/thefirst**为项目主目录
2. 修改主目录下**Service\WebService.java**中的变量**HOST**为运行服务器代码的服务器IP地址
3. 服务器代码位于仓库[Socket](https://github.com/muronglove/Socket)
4. 如果图像识别部分有问题，那么需要在微软的[Azure](https://azure.microsoft.com/zh-cn/try/cognitive-services/?api=face-api)中选择**计算机影像识别**，通过github或者微软等账号登录后，会有几天的试用期，因为服务比较贵，不推荐购买；获得试用资格之后，微软会提供密钥和终结点，分别填入主目录下NewCamera.java文件中`client = new VisionServiceRestClient("eda5ed39055740d2b42dc964e276076b","https://westcentralus.api.cognitive.microsoft.com/vision/v1.0");
`的相应位置即可
5. 服务器运行代码之后，安装此apk文件

# 演示视频
位于项目根目录