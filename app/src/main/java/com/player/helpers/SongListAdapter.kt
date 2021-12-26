package com.player.helpers

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import android.app.Activity
import com.player.R
import com.player.data.database.Song
import com.player.presentation.SongActivity


class SongListAdapter(private val activity: Activity, val context: Context,private val songDataset: List<Song>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.songitem_layout,
            viewGroup, false
        )
        return ItemViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder)
        {
            holder.nameTextView?.text=songDataset[position].title
            holder.authorTextView?.text=songDataset[position].artist
            holder.songImageView?.setImageResource(R.drawable.img3)

            holder.nameTextView?.setOnClickListener(holder)
            holder.authorTextView?.setOnClickListener(holder)
            //holder.songImageView?.setImageURI(songDataset[position].imageURI)
        }
    }



    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        var songImageView: ImageView? = null
        var nameTextView: TextView? = null
        var authorTextView: TextView? = null
        var downloadImageButton: ImageButton? = null

        init {
            songImageView = itemView.findViewById(R.id.imageViewSong)
            nameTextView = itemView.findViewById(R.id.songName)
            authorTextView = itemView.findViewById(R.id.songAuthor)
            downloadImageButton = itemView.findViewById(R.id.download)
        }

        override fun onClick(p0: View?) {
            val intent = Intent(context, SongActivity::class.java)
            intent.putExtra("position",position.toString())
            Log.d("LOG",position.toString())
            intent.putExtra("title",nameTextView?.text)
            intent.putExtra( "author",authorTextView?.text)
            activity.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return songDataset.size
    }
}