package com.android.hackathon.movieapp.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movie")
data class Movie(
        @PrimaryKey
        var id: Int,
        var name: String?,
        var overview: String?,
        var release_date: String?,
        var poster_path: String?,
        var vote_average: Float,
        var runtime: Int,
        var title: String?,
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readFloat(),
                parcel.readInt(),
                parcel.readString()
        ) {}

        override fun describeContents(): Int {
                TODO("Not yet implemented")
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
                dest.writeInt(id)
                dest.writeString(name)
                dest.writeString(overview)
                dest.writeString(release_date)
                dest.writeString(poster_path)
                dest.writeFloat(vote_average)
                dest.writeInt(runtime)
                dest.writeString(title)
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
