@echo off
echo ========================================
echo 音频播放器应用 - GitHub上传助手
echo ========================================
echo.

echo 此脚本将帮助您将项目上传到GitHub并触发自动编译
echo.

REM 检查Git是否安装
git --version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Git，请先安装Git
    echo 下载地址: https://git-scm.com/downloads
    pause
    exit /b 1
)

echo ✓ Git已安装
echo.

REM 检查是否在正确的目录
if not exist "app\src\main\java\com\example\audioplayerapp\MainActivity.kt" (
    echo 错误: 请在AudioPlayerApp目录下运行此脚本
    pause
    exit /b 1
)

echo ✓ 项目文件检查通过
echo.

REM 初始化Git仓库
if not exist ".git" (
    echo 正在初始化Git仓库...
    git init
    echo ✓ Git仓库初始化完成
) else (
    echo ✓ Git仓库已存在
)

echo.

REM 添加所有文件
echo 正在添加文件到Git...
git add .
echo ✓ 文件添加完成

echo.

REM 提交更改
echo 正在提交更改...
git commit -m "Initial commit: Audio Player App with GitHub Actions"
echo ✓ 更改提交完成

echo.

REM 获取GitHub仓库URL
set /p GITHUB_URL="请输入您的GitHub仓库URL (例如: https://github.com/用户名/AudioPlayerApp.git): "

if "%GITHUB_URL%"=="" (
    echo 错误: 请输入GitHub仓库URL
    pause
    exit /b 1
)

echo.

REM 添加远程仓库
echo 正在添加远程仓库...
git remote add origin %GITHUB_URL%
echo ✓ 远程仓库添加完成

echo.

REM 推送到GitHub
echo 正在推送到GitHub...
git branch -M main
git push -u origin main
echo ✓ 代码推送完成

echo.
echo ========================================
echo 🎉 上传完成！
echo ========================================
echo.
echo 接下来请：
echo 1. 访问您的GitHub仓库页面
echo 2. 点击 "Actions" 标签查看编译状态
echo 3. 等待编译完成后下载APK文件
echo.
echo GitHub仓库地址: %GITHUB_URL%
echo.

pause 