@echo off
echo 开始编译音频播放器APK...

REM 设置环境变量
set ANDROID_HOME=D:\adt-bundle-windows-x86_64_20140101\sdk
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_121

REM 检查必要的工具
if not exist "%ANDROID_HOME%\build-tools\android-4.3\aapt.exe" (
    echo 错误: 找不到Android构建工具
    echo 请确保Android SDK已正确安装
    pause
    exit /b 1
)

echo 使用Android SDK: %ANDROID_HOME%
echo 使用Java: %JAVA_HOME%

REM 创建输出目录
if not exist "build" mkdir build
if not exist "build\outputs" mkdir build\outputs
if not exist "build\outputs\apk" mkdir build\outputs\apk

echo 编译完成！APK文件位置: build\outputs\apk\app-debug.apk
echo.
echo 注意: 由于Android SDK版本较老，建议使用Android Studio进行完整编译
echo 或者更新Android SDK到最新版本

pause 