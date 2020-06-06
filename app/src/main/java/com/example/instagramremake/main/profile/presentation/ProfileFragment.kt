package com.example.instagramremake.main.profile.presentation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramremake.R
import com.example.instagramremake.commom.view.AbstractFragment
import com.example.instagramremake.main.home.presentation.HomeFragment
import com.example.instagramremake.main.presentation.MainView
import com.example.instagramremake.register.presentation.RegisterPresenter
import kotlinx.android.synthetic.main.fragment_main_profile.view.*
import kotlinx.android.synthetic.main.item_profile_grid.view.*

class ProfileFragment : AbstractFragment<ProfilePresenter>() {

    private lateinit var mainView: MainView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_profile, container, false)
        view.profile_recycler_view.adapter = PostAdapter()
        view.profile_recycler_view.layoutManager = GridLayoutManager(context, 3)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    private inner class PostAdapter : RecyclerView.Adapter<PostViewHolder>() {
        private var images = arrayOf(
            R.drawable.ic_insta_add,
            R.drawable.ic_insta_add,
            R.drawable.ic_insta_add,
            R.drawable.ic_insta_add,
            R.drawable.ic_insta_add,
            R.drawable.ic_insta_add,
            R.drawable.ic_insta_add,
            R.drawable.ic_insta_add,
            R.drawable.ic_insta_add,
            R.drawable.ic_insta_add,
            R.drawable.ic_insta_add
        )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            return PostViewHolder(layoutInflater.inflate(R.layout.item_profile_grid, parent, false))
        }

        override fun getItemCount(): Int {
            return images.size
        }

        override fun onBindViewHolder(holder: PostViewHolder, position: Int): Unit =
            holder.bind(images[position])


    }

    private inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(image: Int): Unit {
            itemView.profile_image_grid.setImageResource(image)
        }

    }

    companion object {
        fun newInstance(mainView: MainView): ProfileFragment {
            val fragment = ProfileFragment()
            fragment.mainView = mainView
            return fragment
        }
    }
}
