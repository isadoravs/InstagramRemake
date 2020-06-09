package com.example.instagramremake.main.profile.presentation

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramremake.R
import com.example.instagramremake.commom.model.Post
import com.example.instagramremake.commom.view.AbstractFragment
import com.example.instagramremake.main.home.presentation.HomeFragment
import com.example.instagramremake.main.presentation.MainView
import com.example.instagramremake.register.presentation.RegisterPresenter
import kotlinx.android.synthetic.main.fragment_main_profile.*
import kotlinx.android.synthetic.main.fragment_main_profile.view.*
import kotlinx.android.synthetic.main.fragment_main_profile.view.profile_image_icon
import kotlinx.android.synthetic.main.fragment_register_photo.*
import kotlinx.android.synthetic.main.item_profile_grid.*
import kotlinx.android.synthetic.main.item_profile_grid.view.*
import kotlinx.android.synthetic.main.item_profile_grid.view.profile_image_grid

class ProfileFragment : AbstractFragment<ProfilePresenter>(), MainView.ProfileView {

    private lateinit var mainView: MainView
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_profile, container, false)

        postAdapter = PostAdapter()

        view.profile_recycler_view.adapter = postAdapter
        view.profile_recycler_view.layoutManager = GridLayoutManager(context, 3)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        profile_nav_tabs.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.menu_profile_grid -> {
                    profile_recycler_view.layoutManager = GridLayoutManager(context, 3)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_profile_list -> {
                    profile_recycler_view.layoutManager = LinearLayoutManager(context)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    private inner class PostAdapter : RecyclerView.Adapter<PostViewHolder>() {
        var posts = listOf<Post>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            return PostViewHolder(layoutInflater.inflate(R.layout.item_profile_grid, parent, false))
        }

        override fun getItemCount(): Int {
            return posts.size
        }

        override fun onBindViewHolder(holder: PostViewHolder, position: Int): Unit =
            holder.bind(posts[position])


    }

    private inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(post: Post): Unit {
            itemView.profile_image_grid.setImageURI(post.uri)
        }

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun showPhoto(photo: Uri) {
        context?.let{
            val bitmap =
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(it.contentResolver, photo))
                profile_image_icon.setImageBitmap(bitmap)
        }
    }

    override fun showData(
        name: String,
        following: String,
        followers: String,
        posts: String
//        editProfile: Boolean,
//        follow: Boolean
    ) {
        profile_text_view_username.text = name
        profile_text_view_following_count.text = following
        profile_text_view_post_count.text = posts
        profile_text_view_followers_count.text = followers

    }

    override fun showPosts(posts: List<Post>) {
        postAdapter.posts = posts
        postAdapter.notifyDataSetChanged()
    }

    override fun showProgressBar() {
        mainView.showProgressBar()
    }

    override fun hideProgressBar() {
        mainView.hideProgressBar()
    }

    override fun onResume() {
        super.onResume()
        presenter?.findUser()
    }

    companion object {
        fun newInstance(mainView: MainView, presenter: ProfilePresenter): ProfileFragment {
            val fragment = ProfileFragment()
            fragment.mainView = mainView
            fragment.presenter = presenter
            presenter.view = fragment
            return fragment
        }
    }
}
