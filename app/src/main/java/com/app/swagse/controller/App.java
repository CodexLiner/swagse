package com.app.swagse.controller;

import static org.koin.android.ext.koin.KoinExtKt.androidContext;
import static org.koin.core.context.DefaultContextExtKt.startKoin;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.app.swagse.LiveStreaming.Constants;
import com.app.swagse.LiveStreaming.rtc.AgoraEventHandler;
import com.app.swagse.LiveStreaming.rtc.EngineConfig;
import com.app.swagse.LiveStreaming.rtc.EventHandler;
import com.app.swagse.LiveStreaming.stats.StatsManager;
import com.app.swagse.LiveStreaming.utils.FileUtil;
import com.app.swagse.LiveStreaming.utils.PrefManager;
import com.app.swagse.R;
import com.app.swagse.helper.AppSignatureHashHelper;
import com.app.swagse.videoeditor.di.IntegrationKoinModule;
import com.banuba.sdk.arcloud.di.ArCloudKoinModule;
import com.banuba.sdk.audiobrowser.di.AudioBrowserKoinModule;
import com.banuba.sdk.effectplayer.adapter.BanubaEffectPlayerKoinModule;
import com.banuba.sdk.export.di.VeExportKoinModule;
import com.banuba.sdk.gallery.di.GalleryKoinModule;
import com.banuba.sdk.playback.di.VePlaybackSdkKoinModule;
import com.banuba.sdk.token.storage.di.TokenStorageKoinModule;
import com.banuba.sdk.ve.di.VeSdkKoinModule;
import com.banuba.sdk.ve.flow.di.VeFlowKoinModule;
import com.banuba.sdk.veui.di.VeUiSdkKoinModule;
import com.danikula.videocache.HttpProxyCacheServer;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.koin.core.context.GlobalContext;

import io.agora.rtc.RtcEngine;

public class App extends Application {

    private static App mInstance;
    private HttpProxyCacheServer proxy;
    private RtcEngine mRtcEngine;
    private final EngineConfig mGlobalConfig = new EngineConfig();
    private final AgoraEventHandler mHandler = new AgoraEventHandler();
    private final StatsManager mStatsManager = new StatsManager();

    @Override
    public void onCreate() {
        super.onCreate();
        AppSignatureHashHelper appSignatureHelper = new AppSignatureHashHelper(this);
        Fresco.initialize(this);
        appSignatureHelper.getAppSignatures();
        mInstance = this;
        try {
            mRtcEngine = RtcEngine.create(getApplicationContext(), getString(R.string.agora_app_id), mHandler);
            mRtcEngine.setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.enableVideo();
            mRtcEngine.setLogFile(FileUtil.initializeLogFile(this));
        } catch (Exception e) {
            Log.d("TAG", "StartTimerLiveStream: "+e);
            e.printStackTrace();
        }
        startKoin(koinApplication -> {
            androidContext(koinApplication, this);
            // pass the customized Koin module that implements required dependencies. Keep order of modules
            koinApplication.modules(
                    new VeSdkKoinModule().getModule(),
                    new VeExportKoinModule().getModule(),
                    new VePlaybackSdkKoinModule().getModule(),
                    new ArCloudKoinModule().getModule(),
//                    new AudioBrowserKoinModule().getModule(), // use this module only if you bought it
                    new ArCloudKoinModule().getModule(),
                    new TokenStorageKoinModule().getModule(),
                    new VeUiSdkKoinModule().getModule(),
                    new VeUiSdkKoinModule().getModule(),
                    new VeFlowKoinModule().getModule(),
                    new IntegrationKoinModule().getModule(),
//                    new musicKOIN().getMusicProviderModule(),
                    new GalleryKoinModule().getModule(),
                    new BanubaEffectPlayerKoinModule().getModule()
    ).allowOverride(true);
            return null;
        });


//        startKoin(GlobalContext.INSTANCE, koinApplication -> {
//            androidContext(koinApplication, this);
//            // pass the customized Koin module that implements required dependencies. Keep order of modules
//            koinApplication.modules(
//                    new VeSdkKoinModule().getModule(),
//                    new VeExportKoinModule().getModule(),
//                    new AudioBrowserKoinModule().getModule(), // use this module only if you bought it
//                    new ArCloudKoinModule().getModule(),
//                    new TokenStorageKoinModule().getModule(),
//                    new VideoEditorKoinModule().getModule(),
//                    new GalleryKoinModule().getModule(),
//                    new BanubaEffectPlayerKoinModule().getModule()
//            );
//            return null;
//        });

        initConfig();
    }

    private void initConfig() {
        SharedPreferences pref = PrefManager.getPreferences(getApplicationContext());
        mGlobalConfig.setVideoDimenIndex(pref.getInt(
                Constants.PREF_RESOLUTION_IDX, Constants.DEFAULT_PROFILE_IDX));

        boolean showStats = pref.getBoolean(Constants.PREF_ENABLE_STATS, false);
        mGlobalConfig.setIfShowVideoStats(showStats);
        mStatsManager.enableStats(showStats);

        mGlobalConfig.setMirrorLocalIndex(pref.getInt(Constants.PREF_MIRROR_LOCAL, 0));
        mGlobalConfig.setMirrorRemoteIndex(pref.getInt(Constants.PREF_MIRROR_REMOTE, 0));
        mGlobalConfig.setMirrorEncodeIndex(pref.getInt(Constants.PREF_MIRROR_ENCODE, 0));
    }

    public EngineConfig engineConfig() {
        return mGlobalConfig;
    }

    public RtcEngine rtcEngine() {
        return mRtcEngine;
    }

    public StatsManager statsManager() {
        return mStatsManager;
    }

    public void registerEventHandler(EventHandler handler) {
        mHandler.addHandler(handler);
    }

    public void removeEventHandler(EventHandler handler) {
        mHandler.removeHandler(handler);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RtcEngine.destroy();
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .cacheDirectory(CacheUtils.getVideoCacheDir(this))
                .maxCacheFilesCount(40)
                .maxCacheSize(1024 * 1024 * 1024)
                .build();
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
