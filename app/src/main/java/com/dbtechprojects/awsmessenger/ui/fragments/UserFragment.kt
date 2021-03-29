package com.dbtechprojects.awsmessenger.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.amplifyframework.datastore.generated.model.User
import com.dbtechprojects.awsmessenger.databinding.FragmentUserBinding
import com.dbtechprojects.awsmessenger.ui.activities.ChatLogActivity
import com.dbtechprojects.awsmessenger.ui.adapters.UserAdapter
import com.dbtechprojects.awsmessenger.ui.viewmodels.UsersViewModel
import com.dbtechprojects.awsmessenger.util.Mapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user.*


@AndroidEntryPoint
class UserFragment : Fragment() {

    private val viewModel: UsersViewModel by viewModels()
    private lateinit var binding: FragmentUserBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root


        viewModel.fetchusers()
        viewModel.getUserList().observe(viewLifecycleOwner, Observer { users ->
            setuprv(users)
        })

        return view
    }



    private fun setuprv(list: List<User>){
        UsersFragmentRV.invalidate()
        val lm = LinearLayoutManager(requireContext())
        UsersFragmentRV.layoutManager = lm
        val adapter = UserAdapter(list, requireContext())

        adapter.setOnClickListener(object : UserAdapter.OnClickListener {

            override fun onClick(position: Int, model: User, view: View) {

                Log.d("useractvitiy", "clicked")

                val parcelableuser = Mapper().UserToLocalUserModel(model)

                val intent = Intent(requireContext(), ChatLogActivity::class.java)
                intent.putExtra("user", parcelableuser)
                startActivity(intent)

            }
        })

        UsersFragmentRV.adapter = adapter
    }




}