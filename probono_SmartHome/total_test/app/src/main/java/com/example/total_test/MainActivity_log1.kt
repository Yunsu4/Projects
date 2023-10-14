package com.example.total_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout

class MainActivity_log1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_table)
        settingButtonok()
    }
    fun settingButtonok(){
        val button = findViewById<RelativeLayout>(R.id.ghost_tab_2)
        button.setOnClickListener{
            val intent = Intent(this,MainActivity_log2::class.java)
            startActivity(intent)
        }
    }
}