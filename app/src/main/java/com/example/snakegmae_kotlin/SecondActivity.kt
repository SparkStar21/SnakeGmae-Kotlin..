package com.example.snakegmae_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SecondActivity :AppCompatActivity(), View.OnClickListener {
    lateinit var button1 :ImageButton
    lateinit var button2 :ImageButton
    lateinit var button3 :ImageButton
    lateinit var button4 :ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        button1 = findViewById<View>(R.id.button_start) as ImageButton
        button2 = findViewById<View>(R.id.button_difficulty) as ImageButton
        button3 = findViewById<View>(R.id.button_music) as ImageButton
        button4 = findViewById<View>(R.id.button_about) as ImageButton
        button1.setOnClickListener(this)
        button4.setOnClickListener(this)
        button3.setOnClickListener(this)
        button2.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.button_about -> {
                val intent1 = Intent(this, AboutActivity::class.java)
                startActivity(intent1)
            }
            R.id.button_music -> {
                val intent2 = Intent(this, MusicActivity::class.java)
                startActivity(intent2)
            }
            R.id.button_difficulty -> {
                val intent3 = Intent(this, DifficultyActivity::class.java)
                startActivity(intent3)
            }
            R.id.button_start -> {
                val intent4 = Intent(this, GameActivity::class.java)
                startActivity(intent4)
            }
            else ->{

            }
        }
    }
}