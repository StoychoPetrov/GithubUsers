package com.example.githubusers

import android.os.Parcel
import android.os.Parcelable

data class UserModel(val id:            Long,
                     val login:         String,
                     val avatar_url:    String,
                     val name:          String,
                     val company:       String,
                     val followers:     Int,
                     val blog:          String,
                     val location:      String,
                     val email:         String) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(login)
        parcel.writeString(avatar_url)
        parcel.writeString(name)
        parcel.writeString(company)
        parcel.writeInt(followers)
        parcel.writeString(blog)
        parcel.writeString(location)
        parcel.writeString(email)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }
}