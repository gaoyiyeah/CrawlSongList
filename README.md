# CrawlSongList

CrawlSongList可以从网易云，QQ音乐，酷狗音乐中爬取歌单。

QQ音乐和酷狗音乐测试时只用了PC端WEB歌单URL，因为没在真机测试，也没法测试手机端歌单分享的URL，不过没关系，不管是手机端还是WEB端，肯定会存在歌单的ID，只需要修改MainActivity中的`importIt`方法就行。