# 音频播放器应用

一个功能完整的Android音频播放器应用，支持播放音频文件并显示封面图片。

## 功能特点

### 🎵 音频播放功能
- 支持MP3格式音频文件播放
- 播放/暂停控制
- 音频列表管理
- 播放状态指示

### 🖼️ 图片显示功能
- 自动加载音频文件内嵌封面
- 支持外部封面文件
- 图片缓存机制
- 默认音乐图标占位

### 📱 用户界面
- Material Design 3 设计风格
- 响应式布局
- 卡片式音频列表
- 浮动播放按钮

## 项目结构

```
AudioPlayerApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/audioplayerapp/
│   │   │   ├── MainActivity.kt          # 主Activity
│   │   │   ├── AudioPlayer.kt           # 音频播放器
│   │   │   ├── AudioFile.kt             # 音频文件数据类
│   │   │   ├── AudioAdapter.kt          # 音频列表适配器
│   │   │   └── CoverExtractor.kt        # 封面提取器
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml    # 主界面布局
│   │   │   │   └── item_audio.xml       # 音频项布局
│   │   │   ├── drawable/                # 图标资源
│   │   │   ├── values/
│   │   │   │   ├── colors.xml           # 颜色定义
│   │   │   │   ├── strings.xml          # 字符串资源
│   │   │   │   └── themes.xml           # 主题定义
│   │   │   └── AndroidManifest.xml      # 应用清单
│   │   └── build.gradle                 # 模块构建配置
├── build.gradle                         # 项目构建配置
├── settings.gradle                      # 项目设置
└── README.md                           # 项目说明
```

## 技术栈

- **语言**: Kotlin
- **UI框架**: Android Jetpack Compose (Material Design 3)
- **图片加载**: Glide
- **音频播放**: MediaPlayer
- **权限管理**: Android Runtime Permissions

## 使用方法

### 1. 编译和运行

```bash
# 克隆项目
git clone <repository-url>
cd AudioPlayerApp

# 使用Android Studio打开项目
# 或者使用命令行编译
./gradlew assembleDebug
```

### 2. 准备音频文件

将音频文件放在设备的以下路径：
```
/storage/emulated/0/Android/data/com.example.audioplayerapp/files/ZA_SXD_CN/
```

### 3. 应用功能

1. **启动应用**: 应用会自动扫描ZA_SXD_CN文件夹中的MP3文件
2. **播放音频**: 点击列表中的音频项开始播放
3. **控制播放**: 使用浮动按钮控制播放/暂停
4. **查看封面**: 音频列表会显示封面图片（如果有的话）

## 权限说明

应用需要以下权限：
- `READ_EXTERNAL_STORAGE`: 读取外部存储中的音频文件
- `WRITE_EXTERNAL_STORAGE`: 写入封面缓存文件

## 开发说明

### 音频播放实现

```kotlin
// 创建音频播放器
val audioPlayer = AudioPlayer(context)

// 播放音频
audioPlayer.play(audioPath)

// 暂停播放
audioPlayer.pause()

// 恢复播放
audioPlayer.resume()
```

### 封面提取实现

```kotlin
// 创建封面提取器
val coverExtractor = CoverExtractor(context)

// 提取封面
val coverPath = coverExtractor.extractCover(audioPath)
```

### 图片加载实现

```kotlin
// 使用Glide加载图片
Glide.with(context)
    .load(coverPath)
    .placeholder(R.drawable.ic_music_note)
    .error(R.drawable.ic_music_note)
    .into(imageView)
```

## 扩展功能

### 1. 支持更多音频格式
可以在`AudioPlayer`中添加对其他格式的支持：
- FLAC
- WAV
- OGG
- M4A

### 2. 增强封面提取
可以集成第三方库来提取音频封面：
- FFmpeg
- ExoPlayer
- 自定义音频解析器

### 3. 播放列表功能
- 创建播放列表
- 随机播放
- 循环播放
- 播放历史

### 4. 音频可视化
- 频谱显示
- 波形图
- 音频分析

## 注意事项

1. **权限请求**: 应用首次启动时会请求存储权限
2. **文件路径**: 确保音频文件放在正确的目录中
3. **内存管理**: 大量音频文件可能影响性能，建议分批加载
4. **缓存清理**: 定期清理封面缓存以节省存储空间

## 许可证

本项目采用 MIT 许可证。 