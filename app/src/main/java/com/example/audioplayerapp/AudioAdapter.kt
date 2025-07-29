package com.example.audioplayerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

class AudioAdapter(
    private val audioFiles: List<AudioFile>,
    private val onItemClick: (AudioFile) -> Unit
) : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {
    
    private var currentPlayingItemId: Long = -1
    
    inner class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val coverImageView: ImageView = itemView.findViewById(R.id.coverImageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val artistTextView: TextView = itemView.findViewById(R.id.artistTextView)
        private val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)
        private val playIndicator: ImageView = itemView.findViewById(R.id.playIndicator)
        
        fun bind(audioFile: AudioFile) {
            titleTextView.text = audioFile.title
            artistTextView.text = audioFile.artist.ifEmpty { "未知艺术家" }
            
            // 格式化时长显示
            val duration = if (audioFile.duration > 0) {
                val minutes = audioFile.duration / 60000
                val seconds = (audioFile.duration % 60000) / 1000
                String.format("%02d:%02d", minutes, seconds)
            } else {
                "未知时长"
            }
            durationTextView.text = duration
            
            // 加载封面图片
            loadCoverImage(audioFile)
            
            // 设置播放指示器
            playIndicator.visibility = if (audioFile.id == currentPlayingItemId) {
                View.VISIBLE
            } else {
                View.GONE
            }
            
            // 设置点击事件
            itemView.setOnClickListener {
                onItemClick(audioFile)
            }
        }
        
        private fun loadCoverImage(audioFile: AudioFile) {
            // 首先尝试加载内嵌封面
            val coverPath = audioFile.coverPath
            
            if (coverPath != null && File(coverPath).exists()) {
                // 加载外部封面文件
                Glide.with(itemView.context)
                    .load(coverPath)
                    .placeholder(R.drawable.ic_music_note)
                    .error(R.drawable.ic_music_note)
                    .into(coverImageView)
            } else {
                // 尝试从音频文件提取封面
                loadEmbeddedCover(audioFile)
            }
        }
        
        private fun loadEmbeddedCover(audioFile: AudioFile) {
            // 使用增强版封面提取器
            val coverExtractor = EnhancedCoverExtractor(itemView.context)
            val coverPath = coverExtractor.extractCover(audioFile.path)
            
            if (coverPath != null) {
                // 加载提取的封面
                Glide.with(itemView.context)
                    .load(coverPath)
                    .placeholder(R.drawable.ic_music_note)
                    .error(R.drawable.ic_music_note)
                    .into(coverImageView)
            } else {
                // 使用默认图标
                coverImageView.setImageResource(R.drawable.ic_music_note)
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_audio, parent, false)
        return AudioViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val audioFile = audioFiles[position]
        holder.bind(audioFile)
    }
    
    override fun getItemCount(): Int = audioFiles.size
    
    fun setCurrentPlayingItem(itemId: Long) {
        val previousItemId = currentPlayingItemId
        currentPlayingItemId = itemId
        
        // 通知适配器更新UI
        notifyDataSetChanged()
    }
} 