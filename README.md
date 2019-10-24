# RikkaMusic
Android 仿网易云音乐App

# Blog地址：
https://blog.csdn.net/rikkatheworld/article/details/102700463

# 效果展示
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191024161112604.gif)   ![在这里插入图片描述](https://img-blog.csdnimg.cn/20191024161224642.gif)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191024161707183.gif)     ![在这里插入图片描述](https://img-blog.csdnimg.cn/2019102416212642.gif)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191024162346269.gif)  ![在这里插入图片描述](https://img-blog.csdnimg.cn/2019102416244951.gif)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20191024162635280.gif)  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20191024162730454.gif)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20191024163055923.gif)

# App介绍
Android仿网易云音乐App。是非常适合Android初级使用的App。
根据Api 实现如下功能：

 1. 听歌功能
 包括 在线歌曲、FM电台、本地歌曲。
 2.  搜索功能
 通过输入关键词，搜索对应的 歌曲/专辑/歌手/用户/歌单/电台等。
 3. 查看信息功能
 可以查看 歌曲信息/歌曲评论/用户动态/用户信息等等。
 
 

# 使用的技术/框架
你将学到下面的技术和框架:
 - **Api来源**：[GitHub万star项目 **NeteaseCloudMusicApi**](https://github.com/Binaryify/NeteaseCloudMusicApi)
功能非常全的Api，大赞
 - **播放歌曲的媒体框架**： [**StarrySky** 一个丰富的音乐播放封装库，针对快速集成音频播放功能，减少大家搬砖的时间，你值得拥有。](https://github.com/EspoirX/StarrySky)
 - **MVP+RxJava+Retrofit**
本项目的整体架构
附学习blog：[Android 深入Http（5）从Retrofit源码来看Http](https://blog.csdn.net/rikkatheworld/article/details/94831328)
[写给Rikka自己的RxJava2说明书](https://blog.csdn.net/rikkatheworld/article/details/94315510)
 - **GreenDao**
（1）存储更新日推的时间
（2）存储 搜索历史 
 -  **ButterKnife**
 - **EventBus**
 - **Glide**
使用到了一些Glide的用法，比如缓存机制/淡入加载/模糊加载
 - **沉侵式状态栏**
 - **RecyclerView万能适配器Adapter**
 一套BaseAdapter解决大部分 recyclerview的展示
 - **几个简单优秀的自定义View**
 比如 歌单广场的歌单显示、歌手信息/电台信息的 CoordinatorLayout的上拉模糊下拉放大的效果、歌词展示的效果。

# 没有完成的地方
没有完成的地方太多了，记下一些以后慢慢完善：

 - 视频模块
 - 所有的 评论只能看，不能点赞和自己评论
 - 云盘
 - 歌单的添加/修改
 - 没有做付费的内容（因为我不是VIP）
