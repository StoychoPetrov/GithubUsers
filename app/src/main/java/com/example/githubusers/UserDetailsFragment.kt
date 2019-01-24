package com.example.githubusers

import android.os.Bundle
import androidx.fragment.app.Fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_details.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class UserDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val userLogin = arguments!!.getString(Utils.INTENT_USER_LOGIN)
        getUserDetails(userLogin)        // send GET http request to GitHub about user's details with "login", which  is selected from previous activity

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_details, container, false)
    }

    private fun getUserDetails(login: String?){
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

        val downloadImage = DownloadImage(activity!!, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, true, userImg)
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
        Log.d(Utils.TAG_HTTP_CONNECTION_FAILED_MESSAGE, message)                                        // log message in console if there is some problem after the http request
        Toast.makeText(activity, getString(R.string.connection_failed), Toast.LENGTH_SHORT).show()      // show Toast message in UI when connection problem was occur
    }
}
