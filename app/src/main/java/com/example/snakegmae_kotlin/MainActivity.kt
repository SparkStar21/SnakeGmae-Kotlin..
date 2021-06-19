package com.example.snakegmae_kotlin

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity :AppCompatActivity(), View.OnClickListener {
    lateinit var button: Button
    lateinit var edit1: EditText
    lateinit var edit2:EditText
    lateinit var checkbox: CheckBox
    lateinit var bar: ProgressBar
    lateinit var pref: SharedPreferences
    lateinit var editor: Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById<View>(R.id.login_button) as Button
        edit1 = findViewById<View>(R.id.input1) as EditText
        edit2 = findViewById<View>(R.id.input2) as EditText
        checkbox = findViewById<View>(R.id.remember_button) as CheckBox
        bar = findViewById<View>(R.id.progress) as ProgressBar
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        val isRemember = pref.getBoolean("rem", false) //用于给是否保存密码赋值


        if (isRemember) {
            //将账号和密码设置到文本框中
            val account = pref.getString("account", "")
            val password = pref.getString("password", "")
            edit1.setText(account)
            edit2.setText(password)
            checkbox.isChecked = true
        }
        button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        Thread {
            for (i in 0..24) {
                var progress = bar.progress
                progress = progress + 10
                bar.progress = progress
            }
        }.start()

        val account = edit1.text.toString()
        val password = edit2.text.toString()
        if (account == "admin" && password == "123456") {
            editor = pref.edit()
            if (checkbox.isChecked) {
                editor.putBoolean("rem", true)
                editor.putString("account", account)
                editor.putString("password", password)
            } else {
                editor.clear()
            }
            editor.commit()
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "账号或用户名错误", Toast.LENGTH_SHORT).show()
        }

    }
}