package com.app.swagse.activity

import android.Manifest
import android.R
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.swagse.adapter.*
import com.app.swagse.constants.Constants
import com.app.swagse.controller.App
import com.app.swagse.databinding.ActivitySwagTubeDetailsNewBinding
import com.app.swagse.deeplinking.DLinkingActivity
import com.app.swagse.helper.OwnerGlobal
import com.app.swagse.model.swagTube.SwagTubeResponse
import com.app.swagse.sharedpreferences.PrefConnect
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import kotlin.properties.Delegates

class SwagTubeDetailsActivityNew : AppCompatActivity() {
    private lateinit var binding: ActivitySwagTubeDetailsNewBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var player: ExoPlayer
    private lateinit var simpleExoPlayerView: PlayerView
    var result by Delegates.notNull<Boolean>()
    var postId: String? = null
    var url: kotlin.String? = null
    lateinit var swagDetailRecyclerView: RecyclerView
    var channelSubscriberViews: AppCompatTextView? = null
    var swagDetailChannelName: AppCompatTextView? = null
    var swagDetailViews: AppCompatTextView? = null
    var swagDetailComment: AppCompatTextView? = null
    var swagDetailCommentCount: AppCompatTextView? = null
    var swagDetailSave: AppCompatTextView? = null
    var swagDetailDownload: AppCompatTextView? = null
    var swagDetailShare: AppCompatTextView? = null
    var swagDetailDisLike: AppCompatTextView? = null
    var swagDetailLike: AppCompatTextView? = null
    var swagDetailTimeago: AppCompatTextView? = null
    var swagDetailTitle: AppCompatTextView? = null
    lateinit var swagTubeDetailFollow: AppCompatButton
    lateinit var swagTubeDetailProgress: ProgressBar
    lateinit var swagDetailUserPic: CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwagTubeDetailsNewBinding.inflate(layoutInflater)
        progressDialog = ProgressDialog(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val trackSelector = DefaultTrackSelector(this)
        player = ExoPlayer.Builder(this).setTrackSelector(trackSelector).build()
        //Initialize simpleExoPlayerView
        simpleExoPlayerView = binding.exoplayer
        simpleExoPlayerView.player = player

        val intent = intent

        if (intent != null) {
            if (intent.hasExtra(PlayerViewHolder::class.java.simpleName)) {
                postId = intent.extras!!.getString(PlayerViewHolder::class.java.simpleName)
            } else if (intent.hasExtra(NavWatchAdapter::class.java.simpleName)) {
                postId = intent.extras!!.getString(NavWatchAdapter::class.java.simpleName)
            } else if (intent.hasExtra(TrendingViewHolder::class.java.simpleName)) {
                postId = intent.extras!!.getString(TrendingViewHolder::class.java.simpleName)
            } else if (intent.hasExtra(DLinkingActivity::class.java.simpleName)) {
                postId = intent.extras!!.getString(DLinkingActivity::class.java.simpleName)
            } else if (intent.hasExtra(FollowVideoRecyclerViewAdapter::class.java.simpleName)) {
                postId =
                    intent.extras!!.getString(FollowVideoRecyclerViewAdapter::class.java.simpleName)
            } else if (intent.hasExtra(SearchRecyclerViewAdapter::class.java.simpleName)) {
                postId = intent.extras!!.getString(SearchRecyclerViewAdapter::class.java.simpleName)
            } else if (intent.hasExtra(NotificationRecyclerViewAdapter::class.java.simpleName)) {
                postId =
                    intent.extras!!.getString(NotificationRecyclerViewAdapter::class.java.simpleName)
            }
        }

        swagDetailRecyclerView = binding.swagDetailRecyclerView
        swagDetailRecyclerView.setHasFixedSize(true)
        swagDetailRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        swagTubeDetailProgress = binding.swagTubeDetailProgress
        swagTubeDetailFollow = binding.swagTubeDetailFollow
        swagDetailComment = binding.swagDetailComment
        swagDetailCommentCount = binding.swagDetailCommentCount
        swagDetailTimeago = binding.swagDetailTimeago
        swagDetailLike = binding.swagDetailLike
        swagDetailViews = binding.swagDetailViews
        swagDetailDownload = binding.swagDetailDownload
        swagDetailSave = binding.swagDetailSave
        swagDetailShare = binding.swagDetailShare
        swagDetailTitle = binding.swagDetailTitle
        swagDetailUserPic = binding.swagDetailUserPic
        swagDetailViews = binding.swagDetailViews
        swagDetailChannelName = binding.swagDetailChannelName
        channelSubscriberViews = binding.channelSubscriberViews




    }

}