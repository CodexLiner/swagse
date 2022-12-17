package com.app.swagse.videoeditor.impl

import android.net.Uri

interface MediaNavigationProcessor {
    /**
     * Callback for handling content received from camera or gallery screens.
     *
     *@param mediaList the list of uris for images and videos
     *@return true - open next video editor screen,
    false - open your custom screen
     */
    fun process(mediaList: List<Uri>): Boolean
}