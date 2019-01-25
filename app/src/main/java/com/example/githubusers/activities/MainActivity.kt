package com.example.githubusers.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubusers.*
import com.example.githubusers.fragments.UsersFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.root_view, UsersFragment())
                .commit()
        }
    }

    // Override back pressed from actionbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)     // hide back button in actionbar
    }

    // show back button in actionbar
    fun showSearchBtn(){
       supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}