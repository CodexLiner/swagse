package com.app.swagse.TEST

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import codex.CodexPerms
import com.app.swagse.R
import com.app.swagse.SimpleClasses.Functions
import com.app.swagse.adapter.AudioListAdapter
import com.app.swagse.model.SongsData
import com.app.swagse.network.Api
import com.app.swagse.network.RetrofitClient
import com.banuba.sdk.core.data.TrackData
import com.banuba.sdk.core.domain.ProvideTrackContract
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.util.*


class AwesomeAudioContentActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: AudioListAdapter
    var apiInterface: Api? = null
    var progress_bar: ProgressBar? = null
    private var itemsList = ArrayList<SongsData>()
    lateinit var mediaPlayer : MediaPlayer;

    companion object {
        fun buildPickMusicResourceIntent(
            context: Context,
            extras: Bundle
        ) =
            Intent(context, AwesomeAudioContentActivity::class.java).apply {
                putExtras(extras)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.app.swagse.R.layout.activity_awesome_audio_content)
        progress_bar = findViewById(R.id.progress_bar)
        apiInterface = RetrofitClient.getInstance().api

        recyclerView = findViewById(com.app.swagse.R.id.music_rec)

        recyclerView.layoutManager = LinearLayoutManager(this)

        getSongs()

    }

    fun TrackDataSetter( url : String , title :  String ) {
        val newtitle = title + ".mp3"
            if (CodexPerms(this@AwesomeAudioContentActivity).hasPermision(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))) {
                val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/SwagSe/.swagger")
                Functions.show_determinent_loader(this@AwesomeAudioContentActivity, false, false)
                PRDownloader.initialize(applicationContext)
                val prDownloader = PRDownloader.download(url, file.path, newtitle)
                    .build()
                    .setOnStartOrResumeListener { }
                    .setOnPauseListener { }
                    .setOnCancelListener { }
                    .setOnProgressListener { progress ->
                        val prog = (progress.currentBytes * 100 / progress.totalBytes).toInt()
                        Functions.show_loading_progress(prog)
                    }
                prDownloader.start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        Functions.cancel_determinent_loader()
                        finalStep(file , newtitle);
                    }

                    override fun onError(error: Error) {
                        Functions.cancel_determinent_loader()
                    }
                })
            } else {
                CodexPerms(this@AwesomeAudioContentActivity).requestPerms(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            }

    }

    private fun finalStep(file: File?, title: String) {
        val file_new = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/SwagSe/.swagger"), title)
        val uri = Uri.fromFile(file_new)
        val trackData = TrackData(
            UUID.randomUUID(),
            title,
            uri,
            title
        )
        val trackToApply: TrackData = trackData
        val resultIntent = Intent().apply { putExtra(ProvideTrackContract.EXTRA_RESULT_TRACK_DATA, trackToApply) }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getSongs() {
        progress_bar?.setVisibility(View.VISIBLE)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://swagse.fusionapp.site/beautindia/api/songs")
            .build()

        client.newCall(request).enqueue(object : com.squareup.okhttp.Callback {
            override fun onFailure(request: Request?, e: IOException?) {
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext , "Please Try Again ", Toast.LENGTH_LONG).show()
                        progress_bar?.setVisibility(View.GONE)

                    }
                }
            }
            override fun onResponse(response: com.squareup.okhttp.Response?) {
                if (response != null) {
                    try {
                        val jsonObject = JSONObject(response.body().string())
                        val groupListType: Type =
                            object : TypeToken<ArrayList<SongsData?>?>() {}.getType()
                        itemsList = Gson().fromJson(jsonObject.getString("data"), groupListType)

                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                adapter = AudioListAdapter(this@AwesomeAudioContentActivity, itemsList)
                                recyclerView.adapter = adapter
                                adapter.notifyDataSetChanged()
                                progress_bar?.setVisibility(View.GONE)
                            }
                        }
                    } catch (_: Exception) { }
                }
            }

        })

//        val responseCall: Call<SongsResponse> = apiInterface?.songs as Call<SongsResponse>
//        responseCall.enqueue(object : Callback<SongsResponse> {
//            override fun onResponse(
//                call: Call<SongsResponse>,
//                response: Response<SongsResponse>
//            ) {
//                Log.d("TAG", "songsresponse : $response")
////                progress_bar.setVisibility(View.GONE)
//                if (response.code() == 200) {
//                    if (response.body()!!.success == true) {
//                        Log.d("TAG", "songsresponse: ")
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<SongsResponse>, t: Throwable) {
////                progress_bar.visibility = View.GONE
//                Log.d("TAG", "songsresponse: $t")
//            }
//        })
    }
    public fun playAudio( uri: String){
        mediaPlayer = MediaPlayer.create(applicationContext, Uri.parse(uri))
        if (mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }else{
            mediaPlayer.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}