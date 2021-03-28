package com.dbtechprojects.awsmessenger.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.DataStoreException
import com.amplifyframework.datastore.DataStoreItemChange
import com.amplifyframework.datastore.generated.model.User
import com.dbtechprojects.awsmessenger.database.AmplifyAuth
import com.dbtechprojects.awsmessenger.databinding.FragmentSettingsBinding
import com.dbtechprojects.awsmessenger.ui.activities.LoginActivity
import com.dbtechprojects.awsmessenger.util.Constants
import com.dbtechprojects.awsmessenger.util.ImageUtils
import com.dbtechprojects.awsmessenger.util.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: FragmentSettingsBinding
    private var mImageUri: Uri? = null
    private lateinit var LoggedInUser: User


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        // get username from shared pref (populated from main activity on login)

        val pref = SharedPref(requireContext()).sharedPref
        val username = pref.getString("username", 0.toString())


        if(username != "0"){
            binding.SettingsEmailView.text = username
        }

        SetupProfileView()

        binding.SettingsLogoutBTN.setOnClickListener {
            Amplify.Auth.signOut(
                { Log.i("AuthQuickstart", "Signed out successfully") },
                { Log.e("AuthQuickstart", "Sign out failed", it) }
            )
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }


        binding.SettingsImgView.setOnClickListener {
            // check / confirm perms
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED

            )
            {
                showImageChooser()
            } else {
                /*Requests permissions to be granted to this application. These permissions
                 must be requested in your manifest, they should not be granted to your app,
                 and they should have protection level*/
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    3
                )
            }

        }

        binding.SettingsSaveBTN.setOnClickListener {
            Log.d("Settings", "Uri: $mImageUri")

            // safety check to ensure we have a user object (should be created during registration)

            if (!this::LoggedInUser.isInitialized){
                // create user
                if (username != null) {
                    createuser(username)
                    return@setOnClickListener
                }
            }

                // if username has changed we will update it
            if (LoggedInUser.username != SettingsTxtUsernameEditTXT.text.toString()){
                if (SettingsTxtUsernameEditTXT.text.toString().isNotEmpty()){
                    SettingsProgressBar.visibility = View.VISIBLE

                    updateUsername(LoggedInUser.id, SettingsTxtUsernameEditTXT.text.toString())
                }
            }

            // if image has changed we will update it
            if (mImageUri != null){
                val inputstream = activity?.contentResolver?.openInputStream(mImageUri!!)
                SettingsProgressBar.visibility = View.VISIBLE

                   viewModel.uploadFile(inputstream!!)
                   viewModel.getUploadedFileKey().observe(viewLifecycleOwner, Observer { key ->
                       if(key != "error"){
                           updateImage(key)
                       }

                   })
            }

        }


        return view
    }

   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                if (data != null) {
                    try {

                        // The uri of selected image from phone storage.
                        mImageUri = data.data!!

                        // load image into image view

                        ImageUtils().loadImagefromuri(requireContext(), binding.SettingsImgView,
                            mImageUri!!
                        )



                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    private fun showImageChooser() {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        this.startActivityForResult(galleryIntent, 2)
    }



    private  fun updateImage(ImageKey: String){
        val user = LoggedInUser


        Log.i("SettingsFragment", "matcheduser: ${user.id}")

            // call edit profile image and observe result
            viewModel.editProfileImage(LoggedInUser.id,ImageKey)

            viewModel.getProfileImageEditedValue().observe(requireActivity(), Observer { result ->
                if (result == true){
                    Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
                    SettingsProgressBar.visibility = View.GONE
                } else{
                    Toast.makeText(requireContext(), "Profile Update failed", Toast.LENGTH_SHORT).show()
                    SettingsProgressBar.visibility = View.GONE
                }
            })


    }



    private  fun updateUsername(userid: String, username : String){
        viewModel.edituser(userid,username)

           viewModel.isUsernameUpdated().observe(requireActivity(), Observer { result ->
                if (result == true){
                    Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
                    SettingsProgressBar.visibility = View.GONE

                        viewModel.queryLoggedInUserObject()
                        viewModel.getLoggedInUserObject().observe(viewLifecycleOwner, Observer { user ->
                            if (user.id != "error"){
                                LoggedInUser = user
                            } else {
                                Toast.makeText(requireContext(), "Profile Update failed", Toast.LENGTH_SHORT).show()
                                SettingsProgressBar.visibility = View.GONE
                            }
                        })
            }else {
                    Toast.makeText(requireContext(), "Profile Update failed", Toast.LENGTH_SHORT).show()
                    SettingsProgressBar.visibility = View.GONE
                }

            })
    }


    fun SetupProfileView(){

        // get logged in user object and setup profile from details

        viewModel.queryLoggedInUserObject()
        viewModel.getLoggedInUserObject().observe(viewLifecycleOwner, Observer { user ->
            if (user.id != "error"){
                LoggedInUser = user
                Log.d("settingsActivity", LoggedInUser.toString())
                binding.SettingsTxtUsernameEditTXT.setText(LoggedInUser.username)
                if (!LoggedInUser.profilePhotoUrl.isNullOrEmpty()){
                    ImageUtils().loadImage(requireContext(), binding.SettingsImgView,
                            Constants.S3_LINK + LoggedInUser.profilePhotoUrl
                    )
                }

            } else {
                Toast.makeText(requireContext(), "Profile Query Failed", Toast.LENGTH_SHORT).show()
                SettingsProgressBar.visibility = View.GONE
            }
        })
    }

    private fun createuser(username: String){
        if (username.isNotEmpty() && SettingsTxtUsernameEditTXT.text.toString().isNotEmpty()) {
            // run create user command from amplify auth and reserve results
            viewModel.createUser(username, SettingsTxtUsernameEditTXT.text.toString())

            viewModel.getUserCreatedValue().observe(viewLifecycleOwner, Observer { UserCreationresult ->
                if (UserCreationresult == true){
                    Toast.makeText(requireContext(), "Profile created", Toast.LENGTH_SHORT).show()
                    if (mImageUri != null){
                        val inputstream = activity?.contentResolver?.openInputStream(mImageUri!!)
                        SettingsProgressBar.visibility = View.VISIBLE

                        viewModel.uploadFile(inputstream!!)

                        viewModel.getUploadedFileKey().observe(viewLifecycleOwner, Observer { result ->
                            if (result != "error")
                            {
                                updateImage(result)
                            } else
                            {
                                Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()
                                RegisterProgressBar.visibility = View.INVISIBLE
                            }
                        })
                    }

                } else{
                    Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()
                    RegisterProgressBar.visibility = View.INVISIBLE
                }

            })
        } else {
            Toast.makeText(requireContext(), "please fill out form", Toast.LENGTH_SHORT).show()
        }
    }




}