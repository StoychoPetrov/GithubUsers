package com.example.githubusers.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.githubusers.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_details.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class UserDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        setSupportActionBar(toolbarLayout)          // set toolbar to current activity

        supportActionBar?.setDisplayHomeAsUpEnabled(true)       // show standard navigation back button

        getUserDetails(intent.getStringExtra(Utils.INTENT_USER_LOGIN));     // send GET http request to GitHub about user's details with "login", which  is selected from previous activity
    }

    override fun onSupportNavigateUp(): Boolean {    // click event for back button in toolbar
        onBackPressed()
        return true
    }

    private fun getUserDetails(login: String){
        val requestInterface    = Retrofit.Builder()
            .baseUrl(Utils.GITHUB_GET_USERS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(RestfulAPi::class.java)

        var disposable = requestInterface.getGitHubUserDetails(login)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> processGetUserDetails(result) },
                { error -> showError(error.message) }
            )
    }

    private fun processGetUserDetails(userModel: UserModel){            // receive data for user's details from restFul API and data is stored in UserModel

        val downloadImage = DownloadImage(this, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, true, userImg)
        downloadImage.downloadImageFromUrl(userModel.avatar_url)

        // Set data from model in views
        userLoginTxt.text   =   userModel.login
        nameTxt.text        =   userModel.name
        emailTxt.text       =   userModel.email
        companyTxt.text     =   userModel.company
        followersTxt.text   =   userModel.followers.toString()
        blogTxt.text        =   userModel.blog
        locationTxt.text    =   userModel.location
    }

    private fun showError(message: String?){
        Log.d(Utils.TAG_HTTP_CONNECTION_FAILED_MESSAGE, message)                                  // log message in console if there is some problem after the http request
        Toast.makeText(this, getString(R.string.connection_failed), Toast.LENGTH_SHORT).show()      // show Toast message in UI when connection problem was occur
    }
}