package sinhpn.lib.glidepalette

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class GlidePalette<TranscodeType> protected constructor() : BitmapPalette(),
    RequestListener<TranscodeType> {
    protected var callback: RequestListener<TranscodeType>? = null
    fun asGif(): GlidePalette<GifDrawable> {
        return this as GlidePalette<GifDrawable>
    }

    override fun use(@Profile paletteProfile: Int): GlidePalette<TranscodeType> {
        super.use(paletteProfile)
        return this
    }

    fun intoBackground(view: View?): GlidePalette<TranscodeType> {
        return this.intoBackground(view, Swatch.RGB)
    }

    public override fun intoBackground(view: View?, @Swatch paletteSwatch: Int): GlidePalette<TranscodeType> {
        super.intoBackground(view, paletteSwatch)
        return this
    }

    fun intoTextColor(textView: TextView?): GlidePalette<TranscodeType> {
        return this.intoTextColor(
            textView,
            Swatch.TITLE_TEXT_COLOR
        )
    }

    public override fun intoTextColor(textView: TextView?, @Swatch paletteSwatch: Int): GlidePalette<TranscodeType> {
        super.intoTextColor(textView, paletteSwatch)
        return this
    }

    public override fun crossfade(crossfade: Boolean): GlidePalette<TranscodeType>? {
        super.crossfade(crossfade)
        return this
    }

    public override fun crossfade(
        crossfade: Boolean,
        crossfadeSpeed: Int
    ): GlidePalette<TranscodeType>? {
        super.crossfade(crossfade, crossfadeSpeed)
        return this
    }

    fun setGlideListener(listener: RequestListener<TranscodeType>?): GlidePalette<TranscodeType> {
        callback = listener
        return this
    }

    public override fun intoCallBack(callBack: CallBack?): GlidePalette<TranscodeType> {
        super.intoCallBack(callBack)
        return this
    }

    public override fun setPaletteBuilderInterceptor(interceptor: PaletteBuilderInterceptor?): GlidePalette<TranscodeType> {
        super.setPaletteBuilderInterceptor(interceptor)
        return this
    }

    public override fun skipPaletteCache(skipCache: Boolean): GlidePalette<TranscodeType> {
        super.skipPaletteCache(skipCache)
        return this
    }

    override fun onLoadFailed(
        e: GlideException?,
        model: Any,
        target: Target<TranscodeType>,
        isFirstResource: Boolean
    ): Boolean {
        return callback != null && callback!!.onLoadFailed(
            e,
            model,
            target,
            isFirstResource
        )
    }

    override fun onResourceReady(
        resource: TranscodeType,
        model: Any,
        target: Target<TranscodeType>,
        dataSource: DataSource,
        isFirstResource: Boolean
    ): Boolean {
        val callbackResult = callback != null && callback!!.onResourceReady(
            resource,
            model,
            target,
            dataSource,
            isFirstResource
        )
        var b: Bitmap? = null
        when {
            resource is BitmapDrawable -> {
                b = (resource as BitmapDrawable).bitmap
            }
            resource is GifDrawable -> {
                b = (resource as GifDrawable).firstFrame
            }
            target is BitmapHolder -> {
                b = (target as BitmapHolder).bitmap
            }
        }
        b?.let { start(it) }
        return callbackResult
    }

    interface BitmapHolder {
        val bitmap: Bitmap?
    }

    companion object {
        fun with(url: String?): GlidePalette<Drawable> {
            val glidePalette =
                GlidePalette<Drawable>()
            glidePalette.url = url
            return glidePalette
        }
    }
}