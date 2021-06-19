package com.example.snakegmae_kotlin

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity

class DifficultyActivity :AppCompatActivity(), View.OnClickListener {
    private var saved: SharedPreferences?=null
    private var editor: Editor?=null

    var button_jiandan :RadioButton?=null
    var button_yiban : RadioButton?=null
    var button_kunnan :RadioButton?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_difficulty)
        saved = PreferenceManager.getDefaultSharedPreferences(this)
        val level = saved?.getInt("nandu", 500)
        //        boolean on1=saved.getBoolean("difficulty1",true);
//        boolean on2=saved.getBoolean("difficulty2",false);
//        boolean on3=saved.getBoolean("difficulty3",false);
        button_jiandan = findViewById<View>(R.id.button_difficulty1) as RadioButton?
        button_yiban = findViewById<View>(R.id.button_difficulty2) as RadioButton?
        button_kunnan = findViewById<View>(R.id.button_difficulty3) as RadioButton?


//        button_jiandan.setChecked(on1);
//        button_jiandan.setChecked(on2);
//        button_jiandan.setChecked(on3);
        button_jiandan?.setOnClickListener(this)
        button_yiban?.setOnClickListener(this)
        button_kunnan?.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        editor = saved?.edit()
        when (v!!.id) {
            R.id.button_difficulty1 -> if (button_jiandan!!.isChecked) {
                editor!!.putInt("nandu", 500)
                //                    editor.putBoolean("difficulty1",true);
//                    editor.putBoolean("difficulty2",false);
//                    editor.putBoolean("difficulty3",false);
            }
            R.id.button_difficulty2 -> if (button_yiban!!.isChecked) {
                editor?.putInt("nandu", 200)
                //                    editor.putBoolean("difficulty2",true);
//                    editor.putBoolean("difficulty1",false);
//                    editor.putBoolean("difficulty3",false);
            }
            R.id.button_difficulty3 -> if (button_kunnan!!.isChecked) {
                editor?.putInt("nandu", 100)
                //                    editor.putBoolean("difficulty3",true);
//                    editor.putBoolean("difficulty1",false);
//                    editor.putBoolean("difficulty2",false);
            }
        }
        editor?.commit()
    }
}