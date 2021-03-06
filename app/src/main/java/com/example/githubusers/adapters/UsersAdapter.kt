package com.example.githubusers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusers.globalClasses.DownloadImage
import com.example.githubusers.R
import com.example.githubusers.UserModel
import kotlinx.android.synthetic.main.item_user.view.*


class UsersAdapter(private val context:                 Context,
                   private val users :                  ArrayList<UserModel>,
                   private val onItemCLickListener:     OnItemClickListener) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    // Interface for item click event
    interface OnItemClickListener {
        fun onItemClick(viewHolder: ViewHolder, userModel: UserModel)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_user,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val userModel           = users.get(position)

        holder.userLogin?.text  = userModel.login;

        val downloadImage       = DownloadImage(
            context,
            R.mipmap.ic_launcher_round,
            R.mipmap.ic_launcher_round,
            true,
            holder.userImg
        )
        downloadImage.downloadImageFromUrl(userModel.avatar_url)

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onItemCLickListener.onItemClick(holder, users.get(position))
            }
        })
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val userLogin = view.userNameTxt
        val userImg   = view.userImg
    }
}