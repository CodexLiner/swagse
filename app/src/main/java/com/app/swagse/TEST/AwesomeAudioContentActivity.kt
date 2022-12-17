package com.app.swagse.TEST

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.banuba.sdk.core.data.TrackData
import com.banuba.sdk.core.domain.ProvideTrackContract
import java.io.File
import java.util.*


class AwesomeAudioContentActivity : AppCompatActivity() {

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
        val btn = findViewById<TextView>(com.app.swagse.R.id.btn);
        btn.setOnClickListener {
            Toast.makeText(this , "working" , Toast.LENGTH_LONG).show()
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "file_example_mp3_700KB.mp3")
            val uri = Uri.fromFile(file)
            val trackData = TrackData(
                UUID.randomUUID(),
                "My awesome track",
                 uri,
                "Awesome Artist"
            )

            val trackToApply: TrackData = trackData
            val resultIntent = Intent().apply { putExtra( ProvideTrackContract.EXTRA_RESULT_TRACK_DATA, trackToApply)}
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }


    }

}