package sinhpn.lib.glidepalette

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.util.Log
import android.util.LruCache
import android.util.Pair
import android.view.View
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.palette.graphics.Palette
import java.util.*

abstract class BitmapPalette {
    interface CallBack {
        fun onPaletteLoaded(palette: Palette?)
    }

    interface PaletteBuilderInterceptor {
        fun intercept(builder: Palette.Builder?): Palette.Builder?
    }

    @IntDef(
        Profile.VIBRANT,
        Profile.VIBRANT_DARK,
        Profile.VIBRANT_LIGHT,
        Profile.MUTED,
        Profile.MUTED_DARK,
        Profile.MUTED_LIGHT
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Profile {
        companion object {
            const val VIBRANT = 0
            const val VIBRANT_DARK = 1
            const val VIBRANT_LIGHT = 2
            const val MUTED = 3
            const val MUTED_DARK = 4
            const val MUTED_LIGHT = 5
        }
    }

    @IntDef(
        Swatch.RGB,
        Swatch.TITLE_TEXT_COLOR,
        Swatch.BODY_TEXT_COLOR
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Swatch {
        companion object {
            const val RGB = 0
            const val TITLE_TEXT_COLOR = 1
            const val BODY_TEXT_COLOR = 2
        }
    }

    protected var url: String? = null
    protected var targets =
        LinkedList<PaletteTarget>()
    protected var callbacks: ArrayList<CallBack>? = ArrayList()
    private var interceptor: PaletteBuilderInterceptor? = null
    private var skipCache = false
    open fun use(@Profile paletteProfile: Int): BitmapPalette {
        targets.add(PaletteTarget(paletteProfile))
        return this
    }

    protected open fun intoBackground(view: View?, @Swatch paletteSwatch: Int): BitmapPalette {
        assertTargetsIsNotEmpty()
        targets.last.targetsBackground!!.add(
            Pair(
                view,
                paletteSwatch
            )
        )
        return this
    }

    protected open fun intoTextColor(textView: TextView?, @Swatch paletteSwatch: Int): BitmapPalette {
        assertTargetsIsNotEmpty()
        targets.last.targetsText!!.add(
            Pair(
                textView,
                paletteSwatch
            )
        )
        return this
    }

    protected open fun crossfade(crossfade: Boolean): BitmapPalette? {
        assertTargetsIsNotEmpty()
        targets.last.targetCrossfade = crossfade
        return this
    }

    protected open fun crossfade(crossfade: Boolean, crossfadeSpeed: Int): BitmapPalette? {
        assertTargetsIsNotEmpty()
        targets.last.targetCrossfadeSpeed = crossfadeSpeed
        return this.crossfade(crossfade)
    }

    private fun assertTargetsIsNotEmpty() {
        if (targets.isEmpty()) {
            throw UnsupportedOperationException("You must specify a palette with use(Profile.Profile)")
        }
    }

    protected open fun intoCallBack(callBack: CallBack?): BitmapPalette {
        if (callBack != null) callbacks!!.add(callBack)
        return this
    }

    protected open fun skipPaletteCache(skipCache: Boolean): BitmapPalette {
        this.skipCache = skipCache
        return this
    }

    protected open fun setPaletteBuilderInterceptor(interceptor: PaletteBuilderInterceptor?): BitmapPalette {
        this.interceptor = interceptor
        return this
    }

    /*
     * Apply the Palette Profile & Swatch to our current targets
     *
     * palette  the palette to apply
     * cacheHit true if the palette was retrieved from the cache, else false
     */
    protected fun apply(palette: Palette?, cacheHit: Boolean) {
        if (callbacks == null) return
        for (c in callbacks!!) {
            c.onPaletteLoaded(palette)
        }
        if (palette == null) return
        for (target in targets) {
            var swatch: Palette.Swatch? = null
            when (target.paletteProfile) {
                Profile.VIBRANT -> swatch =
                    palette.vibrantSwatch
                Profile.VIBRANT_DARK -> swatch =
                    palette.darkVibrantSwatch
                Profile.VIBRANT_LIGHT -> swatch =
                    palette.lightVibrantSwatch
                Profile.MUTED -> swatch =
                    palette.mutedSwatch
                Profile.MUTED_DARK -> swatch =
                    palette.darkMutedSwatch
                Profile.MUTED_LIGHT -> swatch =
                    palette.lightMutedSwatch
            }
            if (swatch == null) return
            if (target.targetsBackground == null) return
            for (t in target.targetsBackground!!) {
                val color = getColor(swatch, t.second!!)
                //Only crossfade if we're not coming from a cache hit.
                if (!cacheHit && target.targetCrossfade) {
                    crossfadeTargetBackground(target, t, color)
                } else {
                    t.first!!.setBackgroundColor(color)
                }
            }
            if (target.targetsText == null) return
            for (t in target.targetsText!!) {
                val color = getColor(swatch, t.second!!)
                t.first!!.setTextColor(color)
            }
            target.clear()
            callbacks = null
        }
    }

    private fun crossfadeTargetBackground(
        target: PaletteTarget,
        t: Pair<View?, Int?>?,
        newColor: Int
    ) {
        val oldColor = t!!.first!!.background
        val drawables =
            arrayOfNulls<Drawable>(2)
        drawables[0] =
            oldColor ?: ColorDrawable(t.first!!.solidColor)
        drawables[1] = ColorDrawable(newColor)
        val transitionDrawable = TransitionDrawable(drawables)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            t.first!!.background = transitionDrawable
        } else {
            t.first!!.setBackgroundDrawable(transitionDrawable)
        }
        transitionDrawable.startTransition(target.targetCrossfadeSpeed)
    }

    protected fun start(bitmap: Bitmap) {
        val skipCache = skipCache
        if (!skipCache) {
            val palette = CACHE[url]
            if (palette != null) {
                apply(palette, true)
                return
            }
        }
        var builder: Palette.Builder? = Palette.Builder(bitmap)
        if (interceptor != null) {
            builder = interceptor!!.intercept(builder)
        }
        builder?.generate { palette ->
            if (!skipCache) {
                CACHE.put(url, palette)
            }
            apply(palette, false)
        }
    }

    companion object {
        private const val TAG = "BitmapPalette"
        private val CACHE =
            LruCache<String?, Palette?>(40)

        protected fun getColor(swatch: Palette.Swatch?, @Swatch paletteSwatch: Int): Int {
            if (swatch != null) {
                when (paletteSwatch) {
                    Swatch.RGB -> return swatch.rgb
                    Swatch.TITLE_TEXT_COLOR -> return swatch.titleTextColor
                    Swatch.BODY_TEXT_COLOR -> return swatch.bodyTextColor
                }
            } else {
                Log.e(
                    TAG,
                    "error while generating Palette, null palette returned"
                )
            }
            return 0
        }
    }
}