package com.example.githubusers

import android.graphics.Rect
import android.os.Bundle
import android.transition.Explode
import android.transition.Slide
import android.transition.Transition
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusers.adapters.UsersAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_users.view.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class UsersFragment : Fragment(), UsersAdapter.OnItemClickListener {


    private val                 usersArrayList:          ArrayList<UserModel>    = ArrayList()
    private var                 disposable:     Disposable?             = null
    private lateinit var        usersAdapter:   UsersAdapter

    private lateinit var        rootView:       View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView    = inflater.inflate(R.layout.fragment_users, container, false)

        setAdapter()
        setRecyclerViewScrollListener()
        getUsers(0)                 // pass 0 for user's id to get first page with users

        // Inflate the layout for this fragment
        return rootView
    }

    private fun setAdapter(){

        usersAdapter                    = UsersAdapter(activity!!, usersArrayList, this)       // create adapter with empty list which will be notified when has received answer from server

        rootView.usersRecyclerView.layoutManager = LinearLayoutManager(activity)
        rootView.usersRecyclerView.adapter       = usersAdapter
    }

    // Add scroll listener to recyclerView,
    // Send new http request for next page users, when the before last item is visible,
    private fun setRecyclerViewScrollListener() {
        rootView.usersRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val linearLayoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager;

                if (usersArrayList.size > 0 && linearLayoutManager.findLastVisibleItemPosition() >= linearLayoutManager.itemCount - 2)             // check if the before last item is visible
                    getUsers(usersArrayList.get(linearLayoutManager.itemCount - 1).id)                                           // send another http request to server with user's id of the last item from ArrayList with UserModel.
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
        usersArrayList.addAll(usersArrayList)
        usersAdapter.notifyDataSetChanged()             // refresh adapter with new data in the ArrayList
    }

    private fun showError(errorMessage: String?){
        Log.d(Utils.TAG_HTTP_CONNECTION_FAILED_MESSAGE, errorMessage)                                  // log message in console if there is some problem after the http request
    }

    override fun onItemClick(viewHolder: UsersAdapter.ViewHolder, userModel: UserModel) {

        exitTransition  = getListFragmentExitTransition(viewHolder.itemView)
        enterTransition = getDetailsEnterTransition(viewHolder.itemView)

        val userDetailsFragment = UserDetailsFragment()
        val bundle              = Bundle()
        bundle.putString(Utils.INTENT_USER_LOGIN, userModel.login)
        userDetailsFragment.arguments = bundle

        userDetailsFragment.enterTransition = enterTransition

        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(com.example.githubusers.R.id.root_view, userDetailsFragment)
            .addToBackStack(null)
            .addSharedElement(viewHolder.userImg    , viewHolder.userImg.transitionName)
            .addSharedElement(viewHolder.userLogin  , viewHolder.userLogin.transitionName)
            .commit()

    }

    private fun getListFragmentExitTransition(itemView: View): Transition {
        val epicCenterRect = Rect()

        //itemView is the full-width inbox item's view
        itemView.getGlobalVisibleRect(epicCenterRect)

        // Set Epic center to a imaginary horizontal full width line under the clicked item, so the explosion happens vertically away from it
        epicCenterRect.top = epicCenterRect.bottom
        val exitTransition = Explode()

        exitTransition.setPropagation(null)

        exitTransition.epicenterCallback = object : Transition.EpicenterCallback() {
            override fun onGetEpicenter(transition: Transition): Rect {
                return epicCenterRect
            }
        }

        return exitTransition
    }

    private fun getDetailsEnterTransition(itemView: View): Transition{
        val epicCenterRect = Rect()
        //itemView is the full-width inbox item's view
        itemView.getGlobalVisibleRect(epicCenterRect)
        // Set Epic center to a imaginary horizontal full width line under the clicked item, so the explosion happens vertically away from it
        epicCenterRect.top = epicCenterRect.bottom
        val enterTransition = Slide()

        enterTransition.epicenterCallback = object : Transition.EpicenterCallback() {
            override fun onGetEpicenter(transition: Transition): Rect {
                return epicCenterRect
            }
        }

        return enterTransition
    }
}
