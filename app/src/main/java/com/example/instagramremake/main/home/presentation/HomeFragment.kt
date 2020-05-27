package com.example.instagramremake.main.home.presentation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramremake.R
import kotlinx.android.synthetic.main.fragment_main_home.view.*
import kotlinx.android.synthetic.main.item_home_list.view.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_home, container, false)
        view.home_recycler_view.adapter = PostAdapter()
        view.home_recycler_view.layoutManager = LinearLayoutManager(context)
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
            return PostViewHolder(layoutInflater.inflate(R.layout.item_home_list, parent, false))
        }

        override fun getItemCount(): Int {
            return images.size
        }

        override fun onBindViewHolder(holder: PostViewHolder, position: Int): Unit =
            holder.bind(images[position])


    }

    private inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(image: Int): Unit {
            itemView.home_image_list.setImageResource(image)
        }

    }
}
