package com.example.audioplayerapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class MainActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AudioAdapter
    private lateinit var playButton: FloatingActionButton
    private lateinit var audioPlayer: AudioPlayer
    private var audioFiles = mutableListOf<AudioFile>()
    
    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
        private const val AUDIO_FOLDER = "ZA_SXD_CN"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initViews()
        initAudioPlayer()
        checkPermissions()
    }
    
    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        playButton = findViewById(R.id.playButton)
        
        adapter = AudioAdapter(audioFiles) { audioFile ->
            playAudio(audioFile)
        }
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        
        playButton.setOnClickListener {
            if (audioPlayer.isPlaying()) {
                audioPlayer.pause()
                playButton.setImageResource(R.drawable.ic_play)
            } else {
                audioPlayer.resume()
                playButton.setImageResource(R.drawable.ic_pause)
            }
        }
    }
    
    private fun initAudioPlayer() {
        audioPlayer = AudioPlayer(this)
        audioPlayer.setOnPlaybackStateChangeListener(object : AudioPlayer.OnPlaybackStateChangeListener {
            override fun onPlaybackStateChanged(isPlaying: Boolean) {
                runOnUiThread {
                    playButton.setImageResource(
                        if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                    )
                }
            }
            
            override fun onAudioCompleted() {
                runOnUiThread {
                    playButton.setImageResource(R.drawable.ic_play)
                }
            }
        })
    }
    
    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            loadAudioFiles()
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadAudioFiles()
                } else {
                    Toast.makeText(this, "需要存储权限来访问音频文件", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    
    private fun loadAudioFiles() {
        val audioFolder = File(getExternalFilesDir(null), AUDIO_FOLDER)
        
        if (!audioFolder.exists()) {
            Toast.makeText(this, "未找到音频文件夹: $AUDIO_FOLDER", Toast.LENGTH_LONG).show()
            return
        }
        
        val mp3Files = audioFolder.listFiles { file ->
            file.isFile && file.extension.lowercase() == "mp3"
        }?.sortedBy { it.name } ?: emptyArray()
        
        audioFiles.clear()
        mp3Files.forEach { file ->
            val audioFile = AudioFile(
                id = file.absolutePath.hashCode().toLong(),
                title = file.nameWithoutExtension,
                path = file.absolutePath,
                duration = 0L
            )
            audioFiles.add(audioFile)
        }
        
        adapter.notifyDataSetChanged()
        
        if (audioFiles.isEmpty()) {
            Toast.makeText(this, "未找到MP3文件", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "找到 ${audioFiles.size} 个音频文件", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun playAudio(audioFile: AudioFile) {
        audioPlayer.play(audioFile.path)
        playButton.setImageResource(R.drawable.ic_pause)
        
        // 更新当前播放项
        adapter.setCurrentPlayingItem(audioFile.id)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        audioPlayer.release()
    }
} 