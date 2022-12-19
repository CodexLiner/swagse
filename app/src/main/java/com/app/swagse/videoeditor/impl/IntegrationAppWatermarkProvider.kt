package com.app.swagse.videoeditor.impl

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.app.swagse.R
import com.banuba.sdk.ve.effects.watermark.WatermarkProvider


class IntegrationAppWatermarkProvider : WatermarkProvider {
    override fun getWatermarkBitmap(): Bitmap? {
        Log.d("TAG", "getBitmapFromUrl called: ")
        return BitmapFactory.decodeResource(Resources.getSystem() , R.drawable.mlogo)
    }


}