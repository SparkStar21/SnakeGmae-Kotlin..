package com.example.snakegmae_kotlin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity :AppCompatActivity(), View.OnClickListener {
    var saved: SharedPreferences?=null
    var mSnakeView: SnakeView?=null
    var change_stop :ImageButton?=null
    var change_start : ImageButton?=null
    var change_quit :ImageButton?=null
    private var mLeft: ImageButton?=null
    private var mRight: ImageButton?=null
    private var mUp: ImageButton?=null
    private var mDown: ImageButton?=null
    companion object{
        private val ICICLE_KEY = "snake-view"
        private val UP = 1
        private val DOWN = 2
        private val RIGHT = 3
        private val LEFT = 4
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        mSnakeView = findViewById<View>(R.id.snake) as SnakeView?
        mSnakeView?.setTextView(findViewById<View>(R.id.text_show) as TextView)

        change_stop = findViewById<View>(R.id.game_stop) as ImageButton?
        change_start = findViewById<View>(R.id.game_start) as ImageButton?
        change_quit = findViewById<View>(R.id.game_quit) as ImageButton?

        mLeft = findViewById<View>(R.id.left) as ImageButton?
        mRight = findViewById<View>(R.id.right) as ImageButton?
        mUp = findViewById<View>(R.id.up) as ImageButton?
        mDown = findViewById<View>(R.id.down) as ImageButton?

        change_start = findViewById<View>(R.id.game_start) as ImageButton?
        change_stop = findViewById<View>(R.id.game_stop) as ImageButton?
        change_quit = findViewById<View>(R.id.game_quit) as ImageButton?

        saved = PreferenceManager.getDefaultSharedPreferences(this)
        val playMusic = saved?.getBoolean("ifon", true) //获取背景音乐开关的状态

        if (playMusic!!) {
            val intent_service = Intent(this, MusicService::class.java)
            startService(intent_service)
        }
        SnakeView.mMoveDelay = saved!!.getInt("nandu", 500)

        //判断是否有保存数据，没有的话就重新开始游戏

        //判断是否有保存数据，没有的话就重新开始游戏
        if (savedInstanceState == null) {
            mSnakeView?.setMode(SnakeView.READY)
        } else {
            //暂停后的恢复
            val map = savedInstanceState.getBundle(ICICLE_KEY)
            if (map != null) {
                mSnakeView?.restoreState(map)
            } else {
                mSnakeView?.setMode(SnakeView.PAUSE)
            }
        }

        mDown?.setOnClickListener(this)
        mUp?.setOnClickListener(this)
        mRight?.setOnClickListener(this)
        mLeft?.setOnClickListener(this)
        change_start?.setOnClickListener(this)
        change_stop?.setOnClickListener(this)
        change_quit?.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        saved = PreferenceManager.getDefaultSharedPreferences(this)
        val playMusic = saved?.getBoolean("ifon", true)
        if (playMusic!!) {
            val intent_service = Intent(this@GameActivity, MusicService::class.java)
            stopService(intent_service)
        }
    }

    override fun onPause() {
        mSnakeView?.setMode(SnakeView.PAUSE)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //保存游戏状态/
        outState.putBundle(ICICLE_KEY, mSnakeView?.saveState())
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.game_start -> {
                if (mSnakeView?.mMode === SnakeView.READY || mSnakeView?.mMode === SnakeView.LOSE) {
                    SnakeView.mMoveDelay = saved!!.getInt("nandu", 500)
                    mSnakeView?.initNewGame()
                    mSnakeView?.setMode(SnakeView.RUNNING)
                    mSnakeView?.update()
                }
                if (mSnakeView?.mMode === SnakeView.PAUSE) {
                    mSnakeView?.setMode(SnakeView.RUNNING)
                    mSnakeView?.update()
                }
            }
            R.id.game_stop -> if (mSnakeView?.mMode === SnakeView.RUNNING) {
                mSnakeView?.setMode(SnakeView.PAUSE)
            }
            R.id.game_quit -> {
                mSnakeView?.setMode(SnakeView.QUIT)
                finish()
            }
            R.id.left -> if (SnakeView.mDirection !== RIGHT) {
                SnakeView.mNextDirection = LEFT
            }
            R.id.right -> if (SnakeView.mDirection !== LEFT) {
                SnakeView.mNextDirection = RIGHT
            }
            R.id.up -> if (SnakeView.mDirection !== DOWN) {
                SnakeView.mNextDirection = UP
            }
            R.id.down -> if (SnakeView.mDirection !== UP) {
                SnakeView.mNextDirection = DOWN
            }
            else -> {
            }
        }
    }
}