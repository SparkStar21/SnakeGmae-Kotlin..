package com.example.snakegmae_kotlin

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class MusicActivity :AppCompatActivity(), View.OnClickListener {
    private lateinit var saved: SharedPreferences
    private lateinit var editor: Editor
    private lateinit var on_button: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        saved = PreferenceManager.getDefaultSharedPreferences(this)
        on_button = findViewById<View>(R.id.button_on) as CheckBox
        var playMusic = saved.getBoolean("ifon", true)
        on_button.isChecked = playMusic
        on_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        editor = saved.edit()
        if (on_button.isChecked) {
            editor.putBoolean("ifon", true)
        } else {
            editor.putBoolean("ifon", false)
        }
        editor.commit()
    }
}