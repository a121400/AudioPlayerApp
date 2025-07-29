# GitHub Actions 自动编译APK指南

## 🚀 快速开始

### 步骤1：创建GitHub仓库

1. **登录GitHub**
   - 访问 https://github.com
   - 登录您的GitHub账号

2. **创建新仓库**
   - 点击右上角的 "+" 号
   - 选择 "New repository"
   - 仓库名称：`AudioPlayerApp`
   - 描述：`Android音频播放器应用`
   - 选择 "Public"（公开）
   - 不要勾选 "Add a README file"
   - 点击 "Create repository"

### 步骤2：上传项目文件

#### 方法一：使用Git命令行
```bash
# 在AudioPlayerApp目录下执行
git init
git add .
git commit -m "Initial commit: Audio Player App"
git branch -M main
git remote add origin https://github.com/您的用户名/AudioPlayerApp.git
git push -u origin main
```

#### 方法二：使用GitHub Desktop
1. 下载并安装GitHub Desktop
2. 添加本地仓库（选择AudioPlayerApp文件夹）
3. 发布到GitHub

#### 方法三：直接上传文件
1. 在GitHub仓库页面点击 "uploading an existing file"
2. 将所有项目文件拖拽上传
3. 提交更改

### 步骤3：触发自动编译

上传完成后，GitHub Actions会自动开始编译：

1. **查看编译状态**
   - 在仓库页面点击 "Actions" 标签
   - 查看最新的工作流运行状态

2. **手动触发编译**
   - 点击 "Actions" → "Build Audio Player APK"
   - 点击 "Run workflow"
   - 选择分支和构建类型
   - 点击 "Run workflow"

## 📱 下载APK文件

### 方法一：从Actions页面下载
1. 进入 "Actions" 页面
2. 点击最新的成功运行
3. 在 "Artifacts" 部分下载：
   - `AudioPlayerApp-debug` (调试版本)
   - `AudioPlayerApp-release` (发布版本)

### 方法二：从Releases页面下载
1. 点击仓库页面的 "Releases"
2. 下载最新版本的APK文件

## 🔧 工作流配置说明

### 触发条件
- **自动触发**：推送到main/master/develop分支
- **手动触发**：通过GitHub界面手动运行
- **PR触发**：创建Pull Request时

### 构建环境
- **操作系统**：Ubuntu Latest
- **Java版本**：JDK 11
- **Android SDK**：API 34
- **构建工具**：34.0.0

### 缓存优化
- **Gradle缓存**：加速依赖下载
- **构建缓存**：提高编译速度

## 📊 编译结果

### 成功编译后您将获得：
1. **Debug APK**：用于测试和调试
2. **Release APK**：用于正式发布
3. **编译日志**：详细的构建信息
4. **APK信息**：文件大小和基本信息

### 预期文件大小：
- Debug APK：约 15-25 MB
- Release APK：约 10-20 MB

## 🛠️ 故障排除

### 常见问题

#### 1. 编译失败：Gradle错误
**解决方案**：
- 检查网络连接
- 确保所有文件都已上传
- 查看详细错误日志

#### 2. 编译失败：依赖下载失败
**解决方案**：
- 等待一段时间后重试
- 检查build.gradle文件配置
- 使用国内镜像源（如需要）

#### 3. 编译失败：权限问题
**解决方案**：
- 确保仓库是公开的
- 检查GitHub Actions权限设置

### 调试步骤
1. **查看Actions日志**
   - 点击失败的运行
   - 查看详细的错误信息

2. **检查文件完整性**
   - 确保所有源代码文件已上传
   - 检查配置文件是否正确

3. **本地测试**
   - 在本地环境测试编译
   - 修复发现的问题

## 📋 使用APK

### 安装步骤
1. **下载APK文件**
   - 从GitHub Actions或Releases页面下载
   - 选择Debug或Release版本

2. **安装到设备**
   ```bash
   adb install AudioPlayerApp-debug.apk
   ```

3. **准备音频文件**
   ```bash
   # 创建目录
   adb shell mkdir -p /storage/emulated/0/Android/data/com.example.audioplayerapp/files/ZA_SXD_CN
   
   # 复制音频文件
   adb push ZA_SXD_CN/*.mp3 /storage/emulated/0/Android/data/com.example.audioplayerapp/files/ZA_SXD_CN/
   ```

4. **运行应用**
   ```bash
   adb shell am start -n com.example.audioplayerapp/.MainActivity
   ```

## 🔄 持续集成

### 自动更新
- 每次推送代码都会自动触发编译
- 新的APK文件会自动生成
- 可以设置自动发布到Releases

### 版本管理
- 使用Git标签管理版本
- 自动创建Release页面
- 包含详细的更新说明

## 📈 监控和统计

### 编译统计
- 编译成功率
- 编译时间
- APK文件大小变化

### 性能优化
- 缓存命中率
- 依赖下载速度
- 构建时间优化

## 🎯 最佳实践

### 1. 代码管理
- 使用有意义的提交信息
- 定期推送代码更新
- 保持分支整洁

### 2. 版本控制
- 使用语义化版本号
- 创建详细的Release说明
- 标记重要的里程碑

### 3. 质量保证
- 在本地测试后再推送
- 检查编译日志
- 验证APK功能

## 📞 技术支持

### 获取帮助
1. **查看Actions日志**：了解详细错误信息
2. **检查GitHub文档**：https://docs.github.com/en/actions
3. **社区支持**：GitHub Discussions或Issues

### 联系信息
- **项目Issues**：在GitHub仓库创建Issue
- **文档更新**：提交Pull Request改进文档
- **功能建议**：通过Issues提出建议

---

**注意**：GitHub Actions提供每月2000分钟的免费构建时间，对于个人项目通常足够使用。如果超出限制，可以考虑升级到付费计划或使用其他CI/CD服务。 