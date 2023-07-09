package com.valllent.giphy.app

import android.app.Application
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TheApplication : Application(), ImageLoaderFactory {

    override fun newImageLoader() = ImageLoader.Builder(this)
        .diskCache {
            DiskCache.Builder()
                .directory(cacheDir.resolve("gif_cache"))
                .maxSizePercent(0.02)
                .build()
        }
        .memoryCache {
            MemoryCache.Builder(this)
                .strongReferencesEnabled(true)
                .maxSizePercent(0.20)
                .build()
        }
        .components {
            if (Build.VERSION.SDK_INT > 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

}