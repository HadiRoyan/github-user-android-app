package com.hadroy.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hadroy.githubuser.data.remote.response.FollowsResponseItem
import com.hadroy.githubuser.databinding.FragmentFollowBinding
import com.hadroy.githubuser.ui.adapter.FollowsAdapter
import com.hadroy.githubuser.viewmodel.FollowViewModel

class FollowFragment : Fragment() {

    private val viewModels by viewModels<FollowViewModel>()

    private lateinit var binding: FragmentFollowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_USERNAME)

        viewModels.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setProgressBar(isLoading)
        }

        if (index == 1) {
            viewModels.getFollowers(username!!)
            viewModels.detailFollowers.observe(viewLifecycleOwner) { followers ->
                setRecyclerView(followers)
            }
        } else if (index == 2) {
            viewModels.getFollowing(username!!)
            viewModels.detailFollowing.observe(viewLifecycleOwner) { following ->
                setRecyclerView(following)
            }
        }

    }

    private fun setRecyclerView(listData: ArrayList<FollowsResponseItem?>) {
        val followsAdapter = FollowsAdapter(listData)
        val rvFollows: RecyclerView = binding.rvFollows
        followsAdapter.setOnItemClickCallback(object : FollowsAdapter.OnItemClickCallback {
            override fun onItemClicked(user: FollowsResponseItem) {
                val intent = Intent(requireContext(), DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
                startActivity(intent)
            }
        })
        rvFollows.layoutManager = LinearLayoutManager(binding.root.context)
        rvFollows.adapter = followsAdapter
    }

    private fun setProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
        private const val TAG = "FollowFragment"
    }
}