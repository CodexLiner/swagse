package com.app.swagse.videoeditor.impl
import com.banuba.sdk.core.data.OrderProvider
import com.banuba.sdk.core.domain.BackgroundSeparationActionDataProvider
import com.banuba.sdk.core.domain.ScannerActionDataProvider

class IntegrationAppMaskOrderProvider : OrderProvider {

    override fun provide(): List<String> =
        listOf(
            ScannerActionDataProvider.EFFECT_NAME,
            BackgroundSeparationActionDataProvider.EFFECT_NAME
        )
}