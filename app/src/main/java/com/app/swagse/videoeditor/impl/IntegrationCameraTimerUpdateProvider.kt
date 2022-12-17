package com.app.swagse.videoeditor.impl
import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.banuba.sdk.cameraui.data.CameraTimerUpdateProvider
import com.banuba.sdk.core.data.AudioPlayer
import com.banuba.sdk.core.ext.copyFile
import java.io.File

class IntegrationCameraTimerUpdateProvider(
    private val audioPlayer: AudioPlayer,
    context: Context
) : CameraTimerUpdateProvider {

    companion object {
        private const val SOUND_FILE_NAME = "countdown1.wav"
    }

    private val soundFile = File(context.filesDir, SOUND_FILE_NAME)

    init {
        try {
            context.assets.copyFile(SOUND_FILE_NAME, soundFile)
        }catch (e : Exception){
            Log.d("TAG", "CameraTimerUpdateException: "+e)
        }
    }

    override fun start() {
        audioPlayer.prepareTrack(soundFile.toUri())
        audioPlayer.play(false, 0L)
    }

    override fun update() {
        audioPlayer.play(false, 0L)
    }

    override fun finish() {
        audioPlayer.release()
    }
}