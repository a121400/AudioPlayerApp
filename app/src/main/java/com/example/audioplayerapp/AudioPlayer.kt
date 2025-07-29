package com.example.audioplayerapp

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import java.io.IOException

class AudioPlayer(private val context: Context) {
    
    private var mediaPlayer: MediaPlayer? = null
    private var currentAudioPath: String? = null
    private var onPlaybackStateChangeListener: OnPlaybackStateChangeListener? = null
    
    interface OnPlaybackStateChangeListener {
        fun onPlaybackStateChanged(isPlaying: Boolean)
        fun onAudioCompleted()
    }
    
    fun setOnPlaybackStateChangeListener(listener: OnPlaybackStateChangeListener) {
        this.onPlaybackStateChangeListener = listener
    }
    
    fun play(audioPath: String) {
        try {
            // 如果正在播放相同的音频，则继续播放
            if (currentAudioPath == audioPath && mediaPlayer?.isPlaying == true) {
                return
            }
            
            // 停止当前播放
            stop()
            
            // 创建新的MediaPlayer
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioPath)
                prepare()
                start()
                
                setOnCompletionListener {
                    onPlaybackStateChangeListener?.onAudioCompleted()
                    onPlaybackStateChangeListener?.onPlaybackStateChanged(false)
                }
                
                setOnPreparedListener {
                    onPlaybackStateChangeListener?.onPlaybackStateChanged(true)
                }
                
                setOnErrorListener { _, what, extra ->
                    Log.e("AudioPlayer", "播放错误: what=$what, extra=$extra")
                    onPlaybackStateChangeListener?.onPlaybackStateChanged(false)
                    true
                }
            }
            
            currentAudioPath = audioPath
            
        } catch (e: IOException) {
            Log.e("AudioPlayer", "播放音频失败: ${e.message}")
            onPlaybackStateChangeListener?.onPlaybackStateChanged(false)
        } catch (e: Exception) {
            Log.e("AudioPlayer", "播放音频时发生未知错误: ${e.message}")
            onPlaybackStateChangeListener?.onPlaybackStateChanged(false)
        }
    }
    
    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                onPlaybackStateChangeListener?.onPlaybackStateChanged(false)
            }
        }
    }
    
    fun resume() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                onPlaybackStateChangeListener?.onPlaybackStateChanged(true)
            }
        }
    }
    
    fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
        currentAudioPath = null
        onPlaybackStateChangeListener?.onPlaybackStateChanged(false)
    }
    
    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }
    
    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }
    
    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }
    
    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }
    
    fun release() {
        stop()
    }
} 