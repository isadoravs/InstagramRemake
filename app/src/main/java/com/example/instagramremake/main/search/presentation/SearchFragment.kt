package com.example.instagramremake.main.search.presentation

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramremake.R
import com.example.instagramremake.commom.model.User
import com.example.instagramremake.commom.view.AbstractFragment
import com.example.instagramremake.main.presentation.MainView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_main_search.view.*
import kotlinx.android.synthetic.main.item_user_list.*
import kotlinx.android.synthetic.main.item_user_list.view.*
import kotlinx.android.synthetic.main.item_user_list.view.main_search_text_view_username

class SearchFragment : AbstractFragment<SearchPresenter>(), MainView.SearchView {
    private lateinit var mainView: MainView
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_search, container, false)
        userAdapter = UserAdapter()
        view.search_recycler_view.adapter = userAdapter
        view.search_recycler_view.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchItem?.let {
            val searchView = it.actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo((context as AppCompatActivity).componentName))
            searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
                Log.e(
                    "aqui",
                    "$hasFocus "
                )
            }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { text ->
                        presenter?.findUsers(text)
                    }
                    return false
                }
            })
            it.expandActionView()
        }
    }


    interface ItemClickListener {
        fun onClick(user: User);
    }

    private inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            itemView.main_search_text_view_username.text = user.name;
            Glide.with(this@SearchFragment).load(user.photoUrl).into(itemView.main_search_image_view_user)
            itemView.main_search_text_view_name.text = user.name
        }
    }

    private inner class UserAdapter : RecyclerView.Adapter<PostViewHolder>() {
        var users = listOf<User>()
        lateinit var listener: ItemClickListener;

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PostViewHolder {
            return PostViewHolder(
                layoutInflater.inflate(
                    R.layout.item_user_list,
                    viewGroup,
                    false
                )
            );
        }

        override fun onBindViewHolder(postViewHolder: PostViewHolder, position: Int) {
            postViewHolder.bind(users[position]);
            postViewHolder.itemView.setOnClickListener {
                listener.onClick(users[position]);
            };
        }

        override fun getItemCount(): Int {
            return users.size;
        }

    }

    override fun showUsers(users: List<User>) {
        userAdapter.users = users
        userAdapter.listener = (object : ItemClickListener {
            override fun onClick(user: User) {
                mainView.showProfile(user.uuid)
            }
        })
        userAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(mainView: MainView, presenter: SearchPresenter): SearchFragment {
            val fragment = SearchFragment()
            fragment.mainView = mainView
            fragment.presenter = presenter
            presenter.view = fragment
            return fragment
        }
    }
}
