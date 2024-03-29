package com.app.swagse.TEST

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.banuba.sdk.core.data.TrackData
import com.banuba.sdk.core.domain.ProvideTrackContract
import com.banuba.sdk.core.ui.ContentFeatureProvider
import java.lang.ref.WeakReference

class AwesomeActivityMusicProvider : ContentFeatureProvider<TrackData, Fragment> {

    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null

    private val activityResultCallback: (TrackData?) -> Unit = {
        activityResultCallbackInternal(it)
    }
    private var activityResultCallbackInternal: (TrackData?) -> Unit = {}

    override fun init(hostComponent: WeakReference<Fragment>) {
        activityResultLauncher = hostComponent.get()?.registerForActivityResult(
            ProvideTrackContract(),
            activityResultCallback
        )
    }

    override fun requestContent(
        context: Context,
        extras: Bundle
    ): ContentFeatureProvider.Result<TrackData> = ContentFeatureProvider.Result.RequestUi(
        intent = AwesomeAudioContentActivity.buildPickMusicResourceIntent(
            context,
            extras
        )
    )

    override fun handleResult(
        hostFragment: WeakReference<Fragment>,
        intent: Intent,
        block: (TrackData?) -> Unit
    ) {
        activityResultCallbackInternal = block
        activityResultLauncher?.launch(intent)
    }
}