package com.example.audioplayerapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EnhancedCoverExtractor(private val context: Context) {
    
    companion object {
        private const val TAG = "EnhancedCoverExtractor"
        private const val COVER_CACHE_DIR = "audio_covers"
    }
    
    private val cacheDir = File(context.cacheDir, COVER_CACHE_DIR).apply {
        if (!exists()) {
            mkdirs()
        }
    }
    
    /**
     * 从音频文件中提取封面
     */
    fun extractCover(audioPath: String): String? {
        try {
            val audioFile = File(audioPath)
            if (!audioFile.exists()) {
                Log.w(TAG, "音频文件不存在: $audioPath")
                return null
            }
            
            // 生成缓存文件名
            val cacheFileName = "${audioFile.nameWithoutExtension}_cover.jpg"
            val cacheFile = File(cacheDir, cacheFileName)
            
            // 如果缓存文件存在，直接返回
            if (cacheFile.exists()) {
                Log.d(TAG, "使用缓存封面: ${cacheFile.absolutePath}")
                return cacheFile.absolutePath
            }
            
            // 尝试从音频文件提取封面
            val coverBitmap = extractCoverFromAudio(audioPath)
            
            if (coverBitmap != null) {
                // 保存到缓存
                saveCoverToCache(coverBitmap, cacheFile)
                Log.d(TAG, "成功提取并缓存封面: ${cacheFile.absolutePath}")
                return cacheFile.absolutePath
            } else {
                Log.d(TAG, "未找到音频封面")
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "提取封面失败: ${e.message}")
        }
        
        return null
    }
    
    /**
     * 从音频文件中提取封面图片
     */
    private fun extractCoverFromAudio(audioPath: String): Bitmap? {
        val retriever = MediaMetadataRetriever()
        
        try {
            retriever.setDataSource(audioPath)
            
            // 尝试获取内嵌封面
            val embeddedPicture = retriever.embeddedPicture
            
            if (embeddedPicture != null) {
                Log.d(TAG, "找到内嵌封面，大小: ${embeddedPicture.size} bytes")
                return BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.size)
            }
            
            // 如果没有内嵌封面，尝试从元数据获取
            val albumArt = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMART)
            if (albumArt != null) {
                Log.d(TAG, "从元数据获取到专辑封面信息")
                // 这里可以进一步处理专辑封面
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "提取音频封面失败: ${e.message}")
        } finally {
            retriever.release()
        }
        
        return null
    }
    
    /**
     * 保存封面到缓存
     */
    private fun saveCoverToCache(bitmap: Bitmap, cacheFile: File) {
        try {
            FileOutputStream(cacheFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
            }
            Log.d(TAG, "封面已保存到缓存: ${cacheFile.absolutePath}")
        } catch (e: IOException) {
            Log.e(TAG, "保存封面到缓存失败: ${e.message}")
        }
    }
    
    /**
     * 批量提取封面
     */
    fun extractCoversForDirectory(directoryPath: String): Map<String, String> {
        val results = mutableMapOf<String, String>()
        val directory = File(directoryPath)
        
        if (!directory.exists() || !directory.isDirectory) {
            Log.w(TAG, "目录不存在或不是目录: $directoryPath")
            return results
        }
        
        val audioFiles = directory.listFiles { file ->
            file.isFile && file.extension.lowercase() in listOf("mp3", "flac", "m4a", "ogg")
        } ?: emptyArray()
        
        Log.d(TAG, "找到 ${audioFiles.size} 个音频文件")
        
        audioFiles.forEach { audioFile ->
            val coverPath = extractCover(audioFile.absolutePath)
            if (coverPath != null) {
                results[audioFile.absolutePath] = coverPath
                Log.d(TAG, "成功提取封面: ${audioFile.name} -> $coverPath")
            } else {
                Log.d(TAG, "未找到封面: ${audioFile.name}")
            }
        }
        
        return results
    }
    
    /**
     * 清理缓存
     */
    fun clearCache() {
        try {
            cacheDir.listFiles()?.forEach { file ->
                if (file.isFile) {
                    file.delete()
                }
            }
            Log.d(TAG, "封面缓存已清理")
        } catch (e: Exception) {
            Log.e(TAG, "清理缓存失败: ${e.message}")
        }
    }
    
    /**
     * 获取缓存大小
     */
    fun getCacheSize(): Long {
        return try {
            cacheDir.listFiles()?.sumOf { it.length() } ?: 0L
        } catch (e: Exception) {
            Log.e(TAG, "获取缓存大小失败: ${e.message}")
            0L
        }
    }
    
    /**
     * 获取缓存文件数量
     */
    fun getCacheFileCount(): Int {
        return try {
            cacheDir.listFiles()?.count { it.isFile } ?: 0
        } catch (e: Exception) {
            Log.e(TAG, "获取缓存文件数量失败: ${e.message}")
            0
        }
    }
    
    /**
     * 检查音频文件是否有封面
     */
    fun hasCover(audioPath: String): Boolean {
        val audioFile = File(audioPath)
        if (!audioFile.exists()) {
            return false
        }
        
        // 检查缓存
        val cacheFileName = "${audioFile.nameWithoutExtension}_cover.jpg"
        val cacheFile = File(cacheDir, cacheFileName)
        if (cacheFile.exists()) {
            return true
        }
        
        // 检查内嵌封面
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(audioPath)
            val embeddedPicture = retriever.embeddedPicture
            return embeddedPicture != null
        } catch (e: Exception) {
            Log.e(TAG, "检查封面失败: ${e.message}")
            return false
        } finally {
            retriever.release()
        }
    }
} 