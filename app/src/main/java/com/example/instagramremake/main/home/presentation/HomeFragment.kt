package com.example.instagramremake.main.home.presentation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramremake.R
import com.example.instagramremake.commom.model.Feed
import com.example.instagramremake.commom.view.AbstractFragment
import com.example.instagramremake.main.presentation.MainView
import kotlinx.android.synthetic.main.fragment_main_home.view.*
import kotlinx.android.synthetic.main.item_home_list.view.*

class HomeFragment() : AbstractFragment<HomePresenter>(), MainView.HomeView {

    private lateinit var mainView: MainView
    private lateinit var feedAdapter: FeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_home, container, false)
        feedAdapter = FeedAdapter()
        view.home_recycler_view.adapter = feedAdapter
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


    private inner class FeedAdapter() : RecyclerView.Adapter<FeedViewHolder>() {
        var feed = listOf<Feed>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
            return FeedViewHolder(layoutInflater.inflate(R.layout.item_home_list, parent, false))
        }

        override fun getItemCount(): Int {
            return feed.size
        }

        override fun onBindViewHolder(holder: FeedViewHolder, position: Int): Unit =
            holder.bind(feed[position])


    }

    private inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(feed: Feed): Unit {
            itemView.home_image_list.setImageURI(feed.post.uri)
            itemView.home_container_user_caption.text = feed.post.caption
            itemView.home_container_user_photo.setImageURI(feed.publisher.uri)
            itemView.home_container_user_username.text = feed.publisher.name
        }

    }

    override fun showProgressBar() {
        mainView.showProgressBar()
    }

    override fun hideProgressBar() {
        mainView.hideProgressBar()
    }

    override fun showFeed(response: List<Feed>) {
        feedAdapter.feed = response
        feedAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        presenter?.findFeed()
    }

    companion object {
        fun newInstance(mainView: MainView, presenter: HomePresenter): HomeFragment {
            val fragment = HomeFragment()
            fragment.mainView = mainView
            fragment.presenter = presenter
            presenter.view = fragment
            return fragment
        }
    }

}
