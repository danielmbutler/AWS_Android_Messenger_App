package com.dbtechprojects.awsmessenger.util

import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.User
import com.dbtechprojects.awsmessenger.database.entitites.LocalUserModel
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_settings.*
import org.json.JSONArray
import org.json.JSONObject

class Mapper {

    //using this as we cannot parcelize AWS Models (need to send via intent)

   fun UserToLocalUserModel(AwsUser: User) :LocalUserModel{

       val LocalUserModel = LocalUserModel(
           id = AwsUser.id,
           username = AwsUser.username,
           ProfilePhotoUrl = AwsUser.profilePhotoUrl,
           isProfileComplete = AwsUser.isProfileComplete,
           email = AwsUser.email
       )

       return LocalUserModel
   }

    fun LocalUserToAwsUser(LocalUser: LocalUserModel) :User{

        val user = User.builder()
            .id(LocalUser.id)
            .email(LocalUser.email)
            .isProfileComplete(LocalUser.isProfileComplete)
            .username(LocalUser.username)
            .profilePhotoUrl(LocalUser.ProfilePhotoUrl)
            .build()

        return user
    }


}