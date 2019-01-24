package com.example.githubusers

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_user.view.*
import java.lang.Exception

class UsersAdapter(private val context: Context, private val users : ArrayList<UserModel>, private val onItemCLickListener: OnItemClickListener) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    // Interface for item click event
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val userModel = users.get(position)

        holder.userLogin?.text = userModel.login;

        // Download image from url and set in imageview
        Picasso.get()
            .load(userModel.avatar_url)                 // set url where the image will be downloaded
            .placeholder(R.mipmap.ic_launcher_round)    // set default image while the current image is downloading
            .error(R.mipmap.ic_launcher_round)          // set image when download is failed
            .fit()                                      // the image will fit the target (imageView)
            .into(holder.userImg, object: Callback {
                override fun onSuccess() {

                    // Create drawable from downloaded image
                    val bitmap                  = (holder.userImg.drawable as BitmapDrawable).bitmap;
                    val circleBitmapDrawable    = RoundedBitmapDrawableFactory.create(context.resources, bitmap);

                    circleBitmapDrawable.isCircular = true;         // set shape of drawable to be circle

                    holder.userImg.setImageDrawable(circleBitmapDrawable)
                }

                override fun onError(e: Exception?) {
                }
            })

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onItemCLickListener.onItemClick(position)
            }
        })
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val userLogin = view.userNameTxt
        val userImg   = view.userImg
    }
}