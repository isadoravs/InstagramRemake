package com.example.instagramremake.main.camera.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramremake.R
import com.example.instagramremake.commom.view.AbstractFragment
import kotlinx.android.synthetic.main.fragment_main_gallery.*
import kotlinx.android.synthetic.main.fragment_main_gallery.view.*
import kotlinx.android.synthetic.main.item_profile_grid.view.*


class GalleryFragment: AbstractFragment<GalleryPresenter>(), GalleryView {
    private lateinit var pictureAdapter: PictureAdapter
    private lateinit var view: AddView
    private lateinit var uri: Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_gallery, container, false)
        pictureAdapter = PictureAdapter()
        view.main_gallery_recycler_view.layoutManager = GridLayoutManager(context, 3)
        view.main_gallery_recycler_view.adapter = pictureAdapter
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context?.checkSelfPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("pedir permissao")
            requestPermissions(
                Array(1){Manifest.permission.READ_EXTERNAL_STORAGE}, 0)
        } else {
            presenter!!.findPictures(context!!)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //view.dispose()
        println("vem aqui")
        presenter?.findPictures(context!!)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_gallery, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_go -> view.onImageLoaded(uri)
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onPicturesLoaded(uris: List<Uri>) {
        if(uris.isNotEmpty()){
            println("loaded")
            println(uris)
            main_gallery_top.setImageURI(uris[0])
            this.uri = uris[0]
        }
        pictureAdapter.uris = uris
        pictureAdapter.listener = object : GalleryItemClickListener {
            override fun onClick(uri: Uri) {
                this@GalleryFragment.uri = uri
                main_gallery_top.setImageURI(uri)
                main_gallery_scroll_view.smoothScrollTo(0, 0)
            }
        }
    }

    private class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagePost: ImageView = itemView.profile_image_grid
        fun bind(uri: Uri?) {
            imagePost.setImageURI(uri)
        }
    }

    interface GalleryItemClickListener{
        fun onClick(uri: Uri)
    }

    private inner class PictureAdapter() : RecyclerView.Adapter<PictureViewHolder>() {
       // private var listener: GalleryItemClickListener? = null
        lateinit var listener: GalleryItemClickListener
        var uris: List<Uri> = ArrayList()
        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PictureViewHolder {
            return PictureViewHolder(layoutInflater.inflate(R.layout.item_profile_grid, viewGroup, false))
        }

        override fun onBindViewHolder(pictureViewHolder: PictureViewHolder, position: Int) {
            pictureViewHolder.bind(uris[position])
            pictureViewHolder.imagePost.setOnClickListener {
                val uri = uris[position]
                listener.onClick(uri)
            }
        }

        override fun getItemCount(): Int = uris.size

    }

    companion object {
        fun newInstance(addView: AddView, presenter: GalleryPresenter): GalleryFragment {
            val fragment = GalleryFragment()
            fragment.presenter = presenter
            presenter.view = fragment
            fragment.view = addView
            return fragment
        }
    }
}