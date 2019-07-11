package com.mindera.skeletoid.caching

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Environment
import android.os.Environment.isExternalStorageRemovable
import android.util.LruCache
import com.jakewharton.disklrucache.DiskLruCache
import java.io.File
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private const val DISK_CACHE_SIZE = 1024 * 1024 * 10 // 10MB
private const val DISK_CACHE_SUBDIR = "imagecache"

private lateinit var mMemoryCache: LruCache<String, Bitmap>
private var mDiskLruCache: DiskLruCache? = null

private val mDiskCacheLock = ReentrantLock()
private val mDiskCacheLockCondition: Condition = mDiskCacheLock.newCondition()
private var mDiskCacheStarting = true

class DiskCacheUtils {

    fun init() {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        // Use 1/8th of the available memory for this memory cache.
        val cacheSize = maxMemory / 8

        // Initialize memory cache
        mMemoryCache = object : LruCache<String, Bitmap>(cacheSize) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.byteCount / 1024
            }
        }

        // Initialize disk cache on background thread
//        val cacheDir = getDiskCacheDir(context, DISK_CACHE_SUBDIR)
        mDiskCacheLock.withLock {
            mMemoryCache = LruCache(DISK_CACHE_SIZE)
            mDiskCacheStarting = false // Finished initialization
            mDiskCacheLockCondition.signalAll() // Wake any waiting threads
        }
    }

    internal inner class BitmapWorkerTask : AsyncTask<Int, Unit, Bitmap>() {

        // Decode image in background.
        override fun doInBackground(vararg params: Int?): Bitmap? {
            val imageKey = params[0].toString()

            // Check disk cache in background thread
            return getBitmapFromDiskCache(imageKey) ?:
            // Not found in disk cache
            decodeSampledBitmapFromResource(resources, params[0], 100, 100)
                ?.also {
                    // Add final bitmap to caches
                    addBitmapToCache(imageKey, it)
                }
        }
    }

    fun addBitmapToCache(key: String, bitmap: Bitmap) {
        // Add to memory cache as before
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap)
        }

        // Also add to disk cache
        synchronized(mDiskCacheLock) {
            mDiskLruCache.run {
                if (this.get(key) == null) {
                    put(key, bitmap)
                }
            }
        }
    }

    private fun getBitmapFromMemCache(key: String): Bitmap? {
        return return mMemoryCache[key]
    }

    fun getBitmapFromDiskCache(key: String): Bitmap? =
        mDiskCacheLock.withLock {
            // Wait while disk cache is started from background thread
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLockCondition.await()
                } catch (e: InterruptedException) {
                }

            }
            return mDiskLruCache?.get(key)
        }

    // Creates a unique subdirectory of the designated app cache directory. Tries to use external
    // but if not mounted, falls back on internal storage.
    fun getDiskCacheDir(context: Context, uniqueName: String): File {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        val cachePath =
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
                || !isExternalStorageRemovable()
            ) {
                context.externalCacheDir.path
            } else {
                context.cacheDir.path
            }

        return File(cachePath + File.separator + uniqueName)
    }

}