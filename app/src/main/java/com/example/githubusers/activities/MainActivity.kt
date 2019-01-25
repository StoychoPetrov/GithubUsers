package com.example.githubusers.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubusers.*
import com.example.githubusers.adapters.UsersAdapter
import io.reactivex.disposables.Disposable
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


}