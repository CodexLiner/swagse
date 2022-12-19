package com.app.swagse.videoeditor.impl

import com.app.swagse.R
import com.banuba.sdk.cameraui.data.CameraTimerStateProvider
import com.banuba.sdk.cameraui.data.TimerEntry

internal class IntegrationTimerStateProvider : CameraTimerStateProvider {
    override val timerStates = listOf(
        TimerEntry(
            durationMs = 0
        ),
        TimerEntry(
            durationMs = 3000
        )
    )
}