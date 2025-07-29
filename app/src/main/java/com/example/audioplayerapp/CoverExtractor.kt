package com.example.audioplayerapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CoverExtractor(private val context: Context) {
    
    companion object {
        private const val TAG = "CoverExtractor"
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
                return cacheFile.absolutePath
            }
            
            // 尝试从音频文件提取封面
            val coverBitmap = extractCoverFromAudio(audioPath)
            
            if (coverBitmap != null) {
                // 保存到缓存
                saveCoverToCache(coverBitmap, cacheFile)
                return cacheFile.absolutePath
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
        try {
            // 这里可以集成第三方库来提取音频封面
            // 例如使用FFmpeg或其他音频处理库
            
            // 暂时返回null，需要集成具体的音频处理库
            Log.d(TAG, "暂未实现音频封面提取功能")
            return null
            
        } catch (e: Exception) {
            Log.e(TAG, "提取音频封面失败: ${e.message}")
            return null
        }
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
} 