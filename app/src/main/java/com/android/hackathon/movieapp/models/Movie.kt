package com.android.hackathon.movieapp.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Movie(
        var movie_id: Int,
        var title: String?,
        var movie_overview: String?,
        var release_date: String?,
        var poster_path: String?,
        var vote_average: Float,
        var runtime: Int
//        var name: String,
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readFloat(),
                parcel.readInt()
        ) {}

        override fun describeContents(): Int {
                TODO("Not yet implemented")
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
                dest.writeInt(movie_id)
                dest.writeString(title)
                dest.writeString(movie_overview)
                dest.writeString(release_date)
                dest.writeString(poster_path)
                dest.writeFloat(vote_average)
                dest.writeInt(runtime)
        }

        companion object CREATOR : Parcelable.Creator<Movie> {
                override fun createFromParcel(parcel: Parcel): Movie {
                        return Movie(parcel)
                }

                override fun newArray(size: Int): Array<Movie?> {
                        return arrayOfNulls(size)
                }
        }
}
