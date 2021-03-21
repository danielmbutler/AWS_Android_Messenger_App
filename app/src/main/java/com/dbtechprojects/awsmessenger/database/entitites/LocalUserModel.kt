package com.dbtechprojects.awsmessenger.database.entitites

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

// used for passing details in between intents from messages screen to chatlog activity
data class LocalUserModel(
    val id: String?,
    val username: String?,
    val ProfilePhotoUrl: String?,
    val isProfileComplete: Boolean,
    val email: String

): Parcelable
