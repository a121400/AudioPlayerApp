package com.example.audioplayerapp

data class AudioFile(
    val id: Long,
    val title: String,
    val path: String,
    val duration: Long,
    val artist: String = "",
    val album: String = "",
    val coverPath: String? = null
) 