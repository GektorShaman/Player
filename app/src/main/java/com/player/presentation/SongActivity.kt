package com.player.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.telephony.PhoneStateListener
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.player.R
import com.player.data.database.Song
import com.player.viewmodels.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SongActivity : AppCompatActivity(), View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener{
    companion object {
        @JvmField
        var mediaPlayer : MediaPlayer = MediaPlayer()


        @JvmField
        var position : String = "0"
    }


    private val userViewModel by viewModel<UserViewModel>()

    var listSongs : List<Song> = MainActivity.musicFiles
    lateinit var uri : Uri
    private var handler: Handler = Handler()
    //private lateinit var position: String

    lateinit var btnplay: ImageButton
    lateinit var btnstop: ImageButton
    private lateinit var titleTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var durationTextView: TextView
    private lateinit var seekBar: SeekBar


    private lateinit var flow:Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.song_layout)
        position = intent.getStringExtra("position").toString()

        if (position.toInt() == listSongs.size)
        {
            position = "0"
        }
        if (position.toInt() < 0)
        {
            position = (listSongs.size-1).toString()
        }
        val mImageView: ImageView = findViewById(R.id.imageView1)
        mImageView.setImageResource(R.drawable.img3)

        val nextImageButton: ImageButton = findViewById(R.id.button2)
        nextImageButton.setOnClickListener(this)

        val previosImageButton: ImageButton = findViewById(R.id.button1)
        previosImageButton.setOnClickListener(this)

        btnplay = findViewById(R.id.buttonStart)
        btnstop = findViewById(R.id.buttonStop)
        titleTextView = findViewById(R.id.titletextView)
        authorTextView = findViewById(R.id.authortextView)
        durationTextView = findViewById(R.id.textView3)
        seekBar = findViewById(R.id.seekBar)

        titleTextView.text = listSongs[position.toInt()].title
        authorTextView.text = listSongs[position.toInt()].artist

        btnplay.setOnClickListener(this)
        btnstop.setOnClickListener(this)

        val time: TextView = findViewById(R.id.starttextView)

        seekBar.progress = 0


        if (listSongs != null)
        {
            if (position != null) {
                uri = Uri.parse(listSongs[position.toInt()].path)
            }
        }

        if (mediaPlayer != null)
        {
            Log.d("LOG","stop")
            mediaPlayer.stop()
            mediaPlayer.release()
            mediaPlayer = MediaPlayer.create(applicationContext,uri)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener(this)
            Log.d("LOG","start")
        }

        durationTextView.text = formattedTime(mediaPlayer.duration / 1000)
        seekBar.max = mediaPlayer.duration / 1000
        seekBar.setOnSeekBarChangeListener(this)
        flow = object :Runnable{
            override fun run()
            {
                var mCurrentPosition : Int = mediaPlayer.currentPosition /1000
                seekBar.progress = mCurrentPosition
                time.text = formattedTime(mCurrentPosition)
                handler.postDelayed(this,1000)
            }
        }
        this@SongActivity.runOnUiThread(flow)

    }

    private fun formattedTime(mCurrentPosition : Int) : String
    {

        val seconds : String = (mCurrentPosition % 60).toString()
        val minutes : String = (mCurrentPosition / 60).toString()
        val totalout = "$minutes:$seconds"
        val totalNew = "$minutes:0$seconds"
        if (seconds.length ==1)
        {
            return totalNew
        }
        else
        {
            return totalout
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.song_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                userViewModel.loginStateRepository.saveLoginState(false)
                mediaPlayer.stop()
                mediaPlayer.release()
                handler.removeCallbacks(flow)
                val intent = Intent(applicationContext, RegistrationActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.back -> {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(p0: View?) {
        if (p0 is ImageButton) {
            if (p0.id == R.id.buttonStart) {
                mediaPlayer.start()
                btnplay.visibility = View.INVISIBLE
                btnstop.visibility = View.VISIBLE
            }
            if(p0.id == R.id.buttonStop)
            {
                mediaPlayer.pause()
                btnplay.visibility = View.VISIBLE
                btnstop.visibility = View.INVISIBLE
            }
            if (p0.id == R.id.button2) {
                prepareSong(position.toInt()+1)
            }
            if (p0.id == R.id.button1) {
                /*val intent = Intent(applicationContext, SongActivity::class.java)
                intent.putExtra("position",(position.toInt()-1).toString())
                this.startActivity(intent)*/
                prepareSong(position.toInt()-1)
            }
        }

    }

    override fun onProgressChanged(seekBar: SeekBar?, progres: Int, fromUser: Boolean) {
        if (fromUser)
        {
            mediaPlayer.seekTo(progres * 1000)
        }

    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar?) {

    }

    override fun onCompletion(mp: MediaPlayer?) {
        prepareSong(position.toInt()+1)
    }

    fun prepareSong(pos: Int)
    {
        var songPosition: Int = pos
        position = pos.toString()
        if (songPosition == listSongs.size)
        {
            position = "0"
            songPosition = 0
        }
        if (songPosition < 0)
        {
            position = (listSongs.size-1).toString()
            songPosition = listSongs.size-1
        }
        titleTextView.text = listSongs[songPosition].title
        authorTextView.text = listSongs[songPosition].artist
        seekBar.progress = 0
        uri = Uri.parse(listSongs[songPosition].path)
        mediaPlayer.stop()
        mediaPlayer.release()
        mediaPlayer = MediaPlayer.create(applicationContext,uri)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener(this)
        durationTextView.text = formattedTime(mediaPlayer.duration / 1000)
        seekBar.max = mediaPlayer.duration / 1000
    }
}