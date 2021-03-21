package com.dbtechprojects.awsmessenger.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.amplifyframework.datastore.generated.model.LatestMessage
import com.amplifyframework.datastore.generated.model.User
import com.dbtechprojects.awsmessenger.database.AmplifyAuth
import com.dbtechprojects.awsmessenger.database.DatabaseListener
import com.dbtechprojects.awsmessenger.databinding.FragmentMessagesBinding
import com.dbtechprojects.awsmessenger.ui.activities.ChatLogActivity
import com.dbtechprojects.awsmessenger.ui.activities.LoginActivity
import com.dbtechprojects.awsmessenger.ui.adapters.LatestMessageAdapter
import com.dbtechprojects.awsmessenger.util.Mapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.fragment_messages.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MessagesFragment : Fragment() {

    private val viewModel: MessagesViewModel by viewModels()
    private lateinit var binding: FragmentMessagesBinding
    private lateinit var User: User
    private val dbListener = DatabaseListener()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val view = binding.root
        GlobalScope.launch(Dispatchers.IO) {
            // check sign in from amplify auth class
            val check = checkSignIn()
            if (check == true){
                refreshview() // get list of latest messages, set up recyclerview
                ListenForMessages() // setup latest message listener

            } else{
                //if user not logged in then redirect
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }

        }



        return view
    }

    private suspend fun checkSignIn(): Boolean {

        val check = AmplifyAuth().checkSignIn()
        return check

    }

    private fun setuprv(list: List<LatestMessage>, user: User){

        val lm = LinearLayoutManager(requireContext())
        MessagesFragmentRV.layoutManager = lm

        // reverse list so newest messages are at the top

        val adapter = LatestMessageAdapter(list.reversed(),User,requireContext())
        adapter.setOnClickListener(object: LatestMessageAdapter.OnClickListener{
            override fun onClick(position: Int, model: User, message: LatestMessage,view: View) {
                Log.d("MessageActivity", "clicked")

                if (message.readReceipt == "unread"){
                    GlobalScope.launch(Dispatchers.IO) {
                        viewModel.setLatestMessageAsRead(message)
                    }
                }

                val parcelableuser = Mapper().UserToLocalUserModel(model)

                val intent = Intent(requireContext(), ChatLogActivity::class.java)
                intent.putExtra("user", parcelableuser)
                startActivity(intent)
            }

        })

        MessagesFragmentRV.adapter = adapter
        // add faintline under RV Item
        MessagesFragmentRV.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.IO) { checkSignIn() }
    }

     suspend fun ListenForMessages(){
         withContext(Dispatchers.Main){
             MessagesFragmentRV.invalidate()
             // setup latest message listener , for each new message refresh recyclerview
             dbListener.listenforLatestMessages(User)
             dbListener.latestmessage.observe(viewLifecycleOwner, Observer { message ->
                 GlobalScope.launch { refreshview() }
             })
         }
    }

    suspend fun getLatestMessages(user: User): List<LatestMessage>{
        val latestsmessages = viewModel.getLatestMessages(user.id)
        return latestsmessages
    }

    suspend fun refreshview(){

        withContext(Dispatchers.Main){
            User = viewModel.getLoggedInUserObject()

            // get latest messages for loggedin user
           val list = getLatestMessages(User)

            if (list.isNotEmpty()){
                setuprv(list, User)
            }
        }
    }


}