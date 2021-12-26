package com.player.presentation

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.player.R
import com.player.data.database.Song
import com.player.data.database.SpotifyApiRepositoryProvider
import com.player.helpers.SongListAdapter
import com.player.viewmodels.UserViewModel
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), View.OnClickListener, MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {

    companion object {
        @JvmField
        var musicFiles : List<Song> = mutableListOf<Song>()
    }
    lateinit var mAudioManager:AudioManager

    private val userViewModel by viewModel<UserViewModel>()

    private val REQUEST_CODE = 1
    var recyclerView: RecyclerView? = null
    private val spotifyApiRepository = SpotifyApiRepositoryProvider.provideSpotifyApiRepository()

    lateinit var playbtnplay: ImageButton

    lateinit var playbtnstop: ImageButton
    lateinit var token : String

    lateinit var titleTextView: TextView
    lateinit var authorTextView: TextView
    lateinit var songImageView: ImageView

    var musicFiles2 : MutableList<Song> = mutableListOf<Song>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (!SongActivity.mediaPlayer.isPlaying)
        {
            mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        }
        permission()
        Log.d("LOG","sdfssdsfdasdsdhgshd")
        /*val r = spotifyApiRepository.Authorise(this.resources.getString(R.string.client_id),this.resources.getString(R.string.client_secret))
            .subscribeOn(Schedulers.io())
            .subscribe({
                    result ->
                    Log.d("LOG","YeSSSSS")
                }, { error ->
                error.printStackTrace()
                Log.e(this.javaClass.simpleName, "Error getDevices ", error)
                Log.d("LOG","AAAAAA")
                Log.d("LOG",error.message.toString())
            })*/
            /*val r = spotifyApiRepository.SongList(
                this.resources.getString(
                    R.string.music
                ), " ")
            .subscribeOn(Schedulers.io())
            .subscribe({
                result ->
                for (model in result.tracks) {
                    musicFiles2.add(Song(model.name,model.artists[0].name,model.previewURL))
                    Log.d("LOG","YeSSSSS")
                }}, { error ->
                error.printStackTrace()
                Log.e(this.javaClass.simpleName, "Error getDevices ", error)
                Log.d("LOG","AAAAAA")
                Log.d("LOG",error.message.toString())
            })*/
        val r = spotifyApiRepository.Song("0CcQNd8CINkwQfe1RDtGV6?si=b10c12a74c2a4180", " ")
            .subscribeOn(Schedulers.io())
            .subscribe({
                    result ->
                    musicFiles2.add(Song(result.name,result.artists[0].name,result.previewURL))
                    Log.d("LOG","YeSSSSS")
                       }, { error ->
                error.printStackTrace()
                Log.e(this.javaClass.simpleName, "Error getDevices ", error)
                Log.d("LOG", "AAAAAA")
                Log.d("LOG", error.message.toString())
            })
        /*.concatMap { post ->
            Log.d("LOG","ghjkh")
            return@concatMap spotifyApiRepository.SongList(
                this.resources.getString(
                    R.string.m
                    usic
                ), post
            )
        }*/



        Log.d("LOG","sdfssdsfd")
        setContentView(R.layout.main_layout)
        //musicFiles = musicFiles2
        recyclerView = findViewById(R.id.songRecyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(this)
            recyclerView?.adapter = SongListAdapter(this,applicationContext, musicFiles)

        titleTextView = findViewById(R.id.playsongName)
        authorTextView = findViewById(R.id.playsongAuthor)
        songImageView = findViewById(R.id.playimageViewSong)
        if (SongActivity.mediaPlayer.isPlaying)
        {
            titleTextView.text = musicFiles[SongActivity.position.toInt()].title
            authorTextView.text = musicFiles[SongActivity.position.toInt()].artist
            songImageView.setImageResource(R.drawable.img3)
            val playsong : LinearLayout = findViewById(R.id.playsong)
            playsong.visibility = View.VISIBLE
            val nextImageButton: ImageButton = findViewById(R.id.playbuttonnext)
            nextImageButton.setOnClickListener(this)

            val previosImageButton: ImageButton = findViewById(R.id.playbuttonprevious)
            previosImageButton.setOnClickListener(this)
            playbtnplay = findViewById(R.id.playbuttonStart)
            //playbtnstop = findViewById(R.id.playbuttonStop)
            playbtnplay.setOnClickListener(this)
            //playbtnstop.setOnClickListener(this)
        }

    }

    private fun permission()
    {
        if (ContextCompat.checkSelfPermission(applicationContext,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            val param : Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this,param,REQUEST_CODE);
        }
        else
        {
            musicFiles = getAllAudio()
        }
        if (ContextCompat.checkSelfPermission(applicationContext,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
        {
            val param : Array<String> = arrayOf(Manifest.permission.INTERNET)
            ActivityCompat.requestPermissions(this,param,REQUEST_CODE);
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT).show()
                musicFiles = getAllAudio()
            }
            else
            {
                val param : Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this,param,REQUEST_CODE);
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission Internet Granted", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val param : Array<String> = arrayOf(Manifest.permission.INTERNET)
                ActivityCompat.requestPermissions(this,param,REQUEST_CODE);
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                SongActivity.mediaPlayer.stop()
                SongActivity.mediaPlayer.reset()
                SongActivity.mediaPlayer.release()
                mAudioManager.abandonAudioFocus(this)
                userViewModel.loginStateRepository.saveLoginState(false)
                val intent = Intent(applicationContext, RegistrationActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getSongDataset(): List<String> {
        return this.resources.getStringArray(R.array.music_ids).toList()
    }

    private fun getAllAudio() : List<Song> {
        var tempAudioList: MutableList<Song> = mutableListOf<Song>()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection : Array<String> = arrayOf(
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ARTIST
        )
        val cursor : Cursor? = applicationContext.contentResolver.query(uri,projection,null,null,null)
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                val album : String = cursor.getString(0)
                val title : String = cursor.getString(1)
                val duration : String = cursor.getString(2)
                val path : String = cursor.getString(3)
                val artist : String = cursor.getString(4)
                val musicFiles : Song = Song(title,artist,path)
                tempAudioList.add(musicFiles)
            }
            cursor.close()
        }
        return tempAudioList
    }

    override fun onClick(p0: View?) {
        if (p0 is ImageButton) {
            if (p0.id == R.id.playbuttonStart) {
                if (SongActivity.mediaPlayer.isPlaying) {
                    SongActivity.mediaPlayer.pause()
                    playbtnplay.setImageResource(android.R.drawable.ic_media_play)
                }
                else
                {
                    playbtnplay.setImageResource(android.R.drawable.ic_media_pause)
                    SongActivity.mediaPlayer.start()
                }
            }
            if(p0.id == R.id.playbuttonnext)
            {
                prepareSong(SongActivity.position.toInt()+1)
            }
            if(p0.id == R.id.playbuttonprevious)
            {
                prepareSong(SongActivity.position.toInt()-1)
            }
        }

    }

    override fun onCompletion(p0: MediaPlayer?) {
        prepareSong(SongActivity.position.toInt()+1)
    }

    fun prepareSong(pos: Int)
    {
        var songPosition: Int = pos
        SongActivity.position = pos.toString()
        if (songPosition == musicFiles.size)
        {
            SongActivity.position = "0"
            songPosition = 0
        }
        if (songPosition < 0)
        {
            SongActivity.position = (musicFiles.size-1).toString()
            songPosition = musicFiles.size-1
        }
        titleTextView.text = musicFiles[songPosition].title
        authorTextView.text = musicFiles[songPosition].artist
        val uri = Uri.parse(musicFiles[songPosition].path)
        SongActivity.mediaPlayer.stop()
        SongActivity.mediaPlayer.release()
        SongActivity.mediaPlayer = MediaPlayer.create(applicationContext,uri)
        SongActivity.mediaPlayer.start()
        SongActivity.mediaPlayer.setOnCompletionListener(this)
    }

    override fun onAudioFocusChange(focus: Int) {
        Log.d("TAG","here is call")
        when(focus)
        {
            AudioManager.AUDIOFOCUS_LOSS ->
                SongActivity.mediaPlayer.pause()

            AudioManager.AUDIOFOCUS_GAIN ->
                SongActivity.mediaPlayer.start()
        }
        if (focus <= 0)
        {
            SongActivity.mediaPlayer.pause()
        } else{
            SongActivity.mediaPlayer.start()
        }
    }

}

