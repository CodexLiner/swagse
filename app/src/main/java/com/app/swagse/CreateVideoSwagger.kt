package com.app.swagse

import android.Manifest
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import codex.CodexPerms
import com.app.swagse.constants.Variables
import com.banuba.sdk.cameraui.data.PipConfig
import com.banuba.sdk.ve.flow.VideoCreationActivity
import kotlinx.android.synthetic.main.activity_create_video_swagger.*
import java.io.File
import java.util.*

class CreateVideoSwagger : AppCompatActivity() {
    lateinit var button_upload: Button;
    lateinit var button_create: Button;
    lateinit var vide0_view: VideoView
    lateinit var mediaPlayer: MediaPlayer
    lateinit var volume_control: ImageView
    var muted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_video_swagger)
        button_create = findViewById(R.id.create_button)
        button_upload = findViewById(R.id.upload_button)
        vide0_view = findViewById(R.id.video_view)
        volume_control = findViewById(R.id.volume_control)
        var uri: String? = null;
        Objects.requireNonNull(supportActionBar)?.title = "Drafts"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (!CodexPerms(this@CreateVideoSwagger).hasPermision(arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE))) {
            val file = _getFile();
            if (file.exists() && file.isFile) {
                vide0_view.setVideoURI(Uri.parse(file.path))
                vide0_view.start()
                vide0_view.setOnPreparedListener {
                    it.isLooping = true
                    mediaPlayer = it
                    it.start()

                    val videoRatio: Float = it.videoWidth / it.videoHeight.toFloat()
                    val screenRatio: Float =
                        vide0_view.width / vide0_view.height.toFloat()
                    val scale = videoRatio / screenRatio
                    if (scale >= 1f) vide0_view.scaleX = scale else vide0_view.scaleY = 1f / scale
                }

            } else {
                create_();
            }
        } else {
            CodexPerms(this@CreateVideoSwagger).requestPerms(arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE))
        }
        video_view.setOnClickListener {
            if (muted) {
                volume_control.setImageResource(R.drawable.ic_volume_on)
                muted = false
                mediaPlayer.setVolume(0.9f, 0.9f)
            } else {
                volume_control.setImageResource(R.drawable.ic_volume_off)
                muted = true
                mediaPlayer.setVolume(0f, 0f)
            }

        }

        button_create.setOnClickListener {
            create_()
            Variables.CreateButton = true
        }
        button_upload.setOnClickListener {
            val f = _getFile();
            Variables.output_filter_file = f.path;
            startActivity(Intent(this@CreateVideoSwagger, Post_Video_A::class.java))
        }
    }

    private fun _getFile(): File {
        return File((getExternalFilesDir(null)?.path ?: "") + "/export/export_default.mp4");
    }

    override fun onRestart() {
        if (_getFile().exists() && ! Variables.CreateButton) {
            val f = _getFile();
            Variables.output_filter_file = f.path;
            startActivity(Intent(this@CreateVideoSwagger, Post_Video_A::class.java))
        }else
            finish()
        super.onRestart()
    }


    private fun create_() {
        if (!CodexPerms(this@CreateVideoSwagger).hasPermision(arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE))) {
            val list = arrayOfNulls<Uri>(0)
            val videoCreationIntent = Intent(
                VideoCreationActivity.startFromCamera(
                    this, PipConfig(Uri.EMPTY, false, 0.9f), null, null
                )
            )
            startActivity(videoCreationIntent)
        } else {
            CodexPerms(this@CreateVideoSwagger).requestPerms(arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE))
        }
    }
}