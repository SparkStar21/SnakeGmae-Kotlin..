package com.example.snakegmae_kotlin

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService :Service() {
    private lateinit var mp : MediaPlayer
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onCreate() {
        super.onCreate()
        mp = MediaPlayer()
        mp = MediaPlayer.create(this@MusicService, R.raw.bgm1)
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        mp.setLooping(true) //循环播放
        mp.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mp.stop()
        mp.release()
    }
}