package com.example.githubusers.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusers.*
import com.example.githubusers.adapters.UsersAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), UsersAdapter.OnItemClickListener {

    private val                 users:          ArrayList<UserModel>    = ArrayList()
    private var                 disposable:     Disposable?             = null
    private lateinit var        usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)


        setAdapter()
        setRecyclerViewScrollListener()
        getUsers(0)                 // pass 0 for user's id to get first page with users
    }

    private fun setAdapter(){

        usersAdapter                    = UsersAdapter(this, users, this)   // create adapter with empty list which will be notified when has received answer from server

        usersRecyclerView.layoutManager = LinearLayoutManager(this)
        usersRecyclerView.adapter       = usersAdapter
    }

    // Add scroll listener to recyclerView,
    // Send new http request for next page users, when the before last item is visible,
    private fun setRecyclerViewScrollListener() {
        usersRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val linearLayoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager;

                if (linearLayoutManager.findLastVisibleItemPosition() >= linearLayoutManager.itemCount - 2)             // check if the before last item is visible
                    getUsers(users.get(linearLayoutManager.itemCount - 1).id)                                           // send another http request to server with user's id of the last item from ArrayList with UserModel.
            }
        })
    }


    // Get page with users with ids after lastUserId
    private fun getUsers(lastUserId: Long){
        val requestInterface    = Retrofit.Builder()
            .baseUrl(Utils.GITHUB_GET_USERS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(RestfulAPi::class.java)

        disposable = requestInterface.getGitHubUsers("$lastUserId>")
                    .subscribeOn(Schedulers.io())                           // fetch data in background
                    .observeOn(AndroidSchedulers.mainThread())              // result to be in main thread, because it have to be set in views.
                    .subscribe(
                        { result -> processGetUsersResponse(result) },      // successful fetching data
                        { error -> showError(error.message) }               // error was occur when was fetching data
                    )
    }

    // Process response from Http request for getting users
    private  fun processGetUsersResponse(usersArrayList: ArrayList<UserModel>){
        users.addAll(usersArrayList)
        usersAdapter.notifyDataSetChanged()             // refresh adapter with new data in the ArrayList
    }

    private fun showError(errorMessage: String?){
        Log.d(Utils.TAG_HTTP_CONNECTION_FAILED_MESSAGE, errorMessage)                                  // log message in console if there is some problem after the http request
    }

    // Start new activity for user's details
    private fun startUserDetailsActivity(login: String) {
        val intent = Intent(this, UserDetailsActivity::class.java)
        intent.putExtra(Utils.INTENT_USER_LOGIN, login)
        startActivity(intent)
    }

    override fun onItemClick(position: Int) {
        startUserDetailsActivity(users.get(position).login)     // handle event for item from recyclerView is clicked
    }
}