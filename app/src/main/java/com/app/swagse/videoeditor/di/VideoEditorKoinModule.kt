package com.app.swagse.videoeditor.di

import androidx.fragment.app.Fragment
import com.app.swagse.videoeditor.impl.IntegrationAppColorFilterOrderProvider

import com.banuba.sdk.arcloud.data.source.ArEffectsRepositoryProvider
import com.banuba.sdk.arcloud.di.ArCloudKoinModule
import com.banuba.sdk.audiobrowser.di.AudioBrowserKoinModule
import com.banuba.sdk.audiobrowser.domain.AudioBrowserMusicProvider
import com.banuba.sdk.cameraui.data.CameraTimerActionProvider
import com.banuba.sdk.cameraui.data.CameraTimerStateProvider
import com.banuba.sdk.cameraui.domain.HandsFreeTimerActionProvider
import com.banuba.sdk.core.data.OrderProvider
import com.banuba.sdk.core.domain.DraftConfig
import com.banuba.sdk.core.data.TrackData
import com.banuba.sdk.core.ui.ContentFeatureProvider
import com.banuba.sdk.effectplayer.adapter.BanubaEffectPlayerKoinModule
//import com.banuba.sdk.ve.effects.WatermarkProvider
import com.banuba.sdk.export.data.ExportFlowManager

import com.banuba.sdk.export.data.ForegroundExportFlowManager
import com.banuba.sdk.export.data.ExportParamsProvider
import com.banuba.sdk.export.di.VeExportKoinModule
import com.banuba.sdk.gallery.di.GalleryKoinModule
import com.banuba.sdk.playback.di.VePlaybackSdkKoinModule
import com.banuba.sdk.token.storage.di.TokenStorageKoinModule
import com.banuba.sdk.ve.di.VeSdkKoinModule
import com.banuba.sdk.ve.flow.di.VeFlowKoinModule
import com.banuba.sdk.veui.domain.CoverProvider
import org.koin.core.definition.BeanDefinition
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * All dependencies mentioned in this module will override default
 * implementations provided from SDK.
 * Some dependencies has no default implementations. It means that
 * these classes fully depends on your requirements
 */
//class VideoEditorKoinModule : FlowEditorModule() {
//
////    val exportFlowManager: BeanDefinition<ExportFlowManager> = single(override = true) {
////        ForegroundExportFlowManager(
////            exportDataProvider = get(),
////            sessionParamsProvider = get(),
////            exportSessionHelper = get(),
////            exportDir = get(named("exportDir")),
////            shouldClearSessionOnFinish = true,
////            publishManager = get(),
////            errorParser = get(),
////            mediaFileNameHelper = get()
////        )
////    }
//
//    /**
//     * Provides params for export
//     * */
//
////    val module = module { VeSdkKoinModule().module,
////        VeExportKoinModule().module,
////        VePlaybackSdkKoinModule().module,
////        AudioBrowserKoinModule().module, // use this module only if you bought it
////        ArCloudKoinModule().module,
////        TokenStorageKoinModule().module,
////        VeFlowKoinModule().module,
////        BanubaEffectPlayerKoinModule().module }
//
////    val exportParamsProvider: BeanDefinition<ExportParamsProvider> =
////        factory(override = true) {
////            IntegrationAppExportParamsProvider(
////                exportDir = get(named("exportDir")),
////                sizeProvider = get(),
////                watermarkBuilder = get()
////            )
////        }
////
////    val watermarkProvider: BeanDefinition<WatermarkProvider> = factory(override = true) {
////        IntegrationAppWatermarkProvider()
////    }
////
////    override val cameraTimerStateProvider: BeanDefinition<CameraTimerStateProvider> =
////        factory(override = true) {
////            IntegrationTimerStateProvider()
////        }
////
////    val arEffectsRepositoryProvider: BeanDefinition<ArEffectsRepositoryProvider> =
////        single(override = true, createdAtStart = true) {
////            ArEffectsRepositoryProvider(
////                arEffectsRepository = get(named("backendArEffectsRepository")),
////                ioDispatcher = get(named("ioDispatcher"))
////            )
////        }
////
////    override val musicTrackProvider: BeanDefinition<ContentFeatureProvider<TrackData, Fragment>> =
////        single(named("musicTrackProvider"), override = true) {
////            AudioBrowserMusicProvider()
////        }
////
////    override val coverProvider: BeanDefinition<CoverProvider> = single(override = true) {
////        CoverProvider.EXTENDED
////    }
////
////    override val cameraTimerActionProvider: BeanDefinition<CameraTimerActionProvider> =
////        single(override = true) {
////            HandsFreeTimerActionProvider()
////        }
////
////    override val colorFilterOrderProvider: BeanDefinition<OrderProvider> =
////        single(named("colorFilterOrderProvider"), override = true) {
////          IntegrationAppColorFilterOrderProvider();
////        }
////
////
////    override val draftConfig: BeanDefinition<DraftConfig> = factory(override = true) {
////        DraftConfig.ENABLED_ASK_TO_SAVE
////    }
//}
