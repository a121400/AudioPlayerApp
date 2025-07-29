# 音频播放器应用使用示例

## 快速开始

### 1. 编译应用

```bash
cd AudioPlayerApp
./gradlew assembleDebug
```

### 2. 安装应用

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 3. 准备音频文件

将ZA_SXD_CN文件夹中的音频文件复制到设备：

```bash
# 创建目标目录
adb shell mkdir -p /storage/emulated/0/Android/data/com.example.audioplayerapp/files/ZA_SXD_CN

# 复制音频文件
adb push ZA_SXD_CN/*.mp3 /storage/emulated/0/Android/data/com.example.audioplayerapp/files/ZA_SXD_CN/
```

### 4. 运行应用

```bash
adb shell am start -n com.example.audioplayerapp/.MainActivity
```

## 功能演示

### 音频播放功能

1. **启动应用**: 应用会自动扫描ZA_SXD_CN文件夹中的MP3文件
2. **查看列表**: 音频列表显示所有找到的MP3文件
3. **播放音频**: 点击列表中的任意音频项开始播放
4. **控制播放**: 使用右下角的浮动按钮控制播放/暂停

### 封面显示功能

1. **自动提取**: 应用会自动从音频文件中提取内嵌封面
2. **缓存机制**: 提取的封面会缓存到本地，提高加载速度
3. **默认图标**: 如果没有封面，会显示默认的音乐图标

## 代码示例

### 基本使用

```kotlin
// 创建音频播放器
val audioPlayer = AudioPlayer(context)

// 播放音频
audioPlayer.play("/path/to/audio.mp3")

// 暂停播放
audioPlayer.pause()

// 恢复播放
audioPlayer.resume()

// 停止播放
audioPlayer.stop()
```

### 封面提取

```kotlin
// 创建封面提取器
val coverExtractor = EnhancedCoverExtractor(context)

// 提取单个音频文件的封面
val coverPath = coverExtractor.extractCover("/path/to/audio.mp3")

// 批量提取目录中所有音频文件的封面
val covers = coverExtractor.extractCoversForDirectory("/path/to/audio/directory")

// 检查音频文件是否有封面
val hasCover = coverExtractor.hasCover("/path/to/audio.mp3")
```

### 图片加载

```kotlin
// 使用Glide加载封面图片
Glide.with(context)
    .load(coverPath)
    .placeholder(R.drawable.ic_music_note)
    .error(R.drawable.ic_music_note)
    .into(imageView)
```

## 高级功能

### 1. 自定义音频播放器

```kotlin
class CustomAudioPlayer(context: Context) : AudioPlayer(context) {
    
    override fun play(audioPath: String) {
        // 自定义播放逻辑
        super.play(audioPath)
        
        // 添加播放统计
        logPlayback(audioPath)
    }
    
    private fun logPlayback(audioPath: String) {
        // 记录播放历史
        Log.d("CustomAudioPlayer", "播放音频: $audioPath")
    }
}
```

### 2. 自定义封面提取器

```kotlin
class CustomCoverExtractor(context: Context) : EnhancedCoverExtractor(context) {
    
    override fun extractCover(audioPath: String): String? {
        // 首先尝试从网络获取封面
        val networkCover = getCoverFromNetwork(audioPath)
        if (networkCover != null) {
            return networkCover
        }
        
        // 然后尝试从本地提取
        return super.extractCover(audioPath)
    }
    
    private fun getCoverFromNetwork(audioPath: String): String? {
        // 实现网络封面获取逻辑
        return null
    }
}
```

### 3. 播放列表管理

```kotlin
class PlaylistManager {
    private val playlists = mutableMapOf<String, MutableList<AudioFile>>()
    
    fun createPlaylist(name: String, audioFiles: List<AudioFile>) {
        playlists[name] = audioFiles.toMutableList()
    }
    
    fun getPlaylist(name: String): List<AudioFile>? {
        return playlists[name]
    }
    
    fun addToPlaylist(playlistName: String, audioFile: AudioFile) {
        playlists[playlistName]?.add(audioFile)
    }
}
```

## 性能优化

### 1. 异步加载

```kotlin
// 在后台线程中提取封面
CoroutineScope(Dispatchers.IO).launch {
    val coverPath = coverExtractor.extractCover(audioPath)
    
    withContext(Dispatchers.Main) {
        // 在主线程中更新UI
        loadCoverImage(coverPath)
    }
}
```

### 2. 内存管理

```kotlin
// 清理不需要的缓存
fun cleanupCache() {
    val coverExtractor = EnhancedCoverExtractor(context)
    val cacheSize = coverExtractor.getCacheSize()
    
    if (cacheSize > MAX_CACHE_SIZE) {
        coverExtractor.clearCache()
    }
}
```

### 3. 批量处理

```kotlin
// 批量提取封面
fun extractAllCovers(audioDirectory: String) {
    val coverExtractor = EnhancedCoverExtractor(context)
    
    CoroutineScope(Dispatchers.IO).launch {
        val covers = coverExtractor.extractCoversForDirectory(audioDirectory)
        
        withContext(Dispatchers.Main) {
            // 更新UI显示提取结果
            updateCoverDisplay(covers)
        }
    }
}
```

## 故障排除

### 常见问题

1. **权限问题**: 确保应用有读取外部存储的权限
2. **文件路径**: 确保音频文件在正确的目录中
3. **内存不足**: 大量音频文件可能导致内存不足，建议分批处理
4. **缓存问题**: 如果封面显示异常，可以清理缓存重新提取

### 调试技巧

```kotlin
// 启用详细日志
Log.d("AudioPlayer", "播放音频: $audioPath")
Log.d("CoverExtractor", "提取封面: $audioPath")

// 检查文件存在性
val file = File(audioPath)
if (!file.exists()) {
    Log.e("AudioPlayer", "音频文件不存在: $audioPath")
}
```

## 扩展开发

### 1. 添加新音频格式支持

```kotlin
// 在AudioPlayer中添加对新格式的支持
fun playAudio(audioPath: String) {
    val extension = audioPath.substringAfterLast('.', "").lowercase()
    
    when (extension) {
        "mp3" -> playMp3(audioPath)
        "flac" -> playFlac(audioPath)
        "m4a" -> playM4a(audioPath)
        else -> Log.w("AudioPlayer", "不支持的音频格式: $extension")
    }
}
```

### 2. 添加音频可视化

```kotlin
// 音频可视化组件
class AudioVisualizerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    fun updateVisualization(audioData: ByteArray) {
        // 实现音频可视化逻辑
        invalidate()
    }
}
```

### 3. 添加播放统计

```kotlin
// 播放统计管理器
class PlaybackStatsManager {
    private val playCount = mutableMapOf<String, Int>()
    
    fun recordPlayback(audioPath: String) {
        playCount[audioPath] = (playCount[audioPath] ?: 0) + 1
    }
    
    fun getPlayCount(audioPath: String): Int {
        return playCount[audioPath] ?: 0
    }
}
``` 