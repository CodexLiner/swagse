package com.app.swagse.videoeditor.impl

import android.R
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.banuba.sdk.ve.effects.watermark.WatermarkProvider


class IntegrationAppWatermarkProvider : WatermarkProvider {

    val bitmapIcon = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_menu_add)
    override fun getWatermarkBitmap(): Bitmap? {

        return bitmapIcon
    }
}