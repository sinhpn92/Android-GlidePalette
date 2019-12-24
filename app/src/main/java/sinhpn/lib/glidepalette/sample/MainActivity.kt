package sinhpn.lib.glidepalette.sample

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
import sinhpn.lib.glidepalette.BitmapPalette
import sinhpn.lib.glidepalette.GlidePalette
import sinhpn.lib.glidepalette.R

class MainActivity : AppCompatActivity() {

    val URL = "http://i.huffpost.com/gen/2299606/images/n-STARRY-NIGHT-628x314.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Glide.with(this).load(URL)
            .listener(
                GlidePalette.with(URL)
                    .use(BitmapPalette.Profile.VIBRANT)
                    .intoBackground(textVibrant, BitmapPalette.Swatch.RGB)
                    .intoTextColor(textVibrant, BitmapPalette.Swatch.BODY_TEXT_COLOR)
                    .crossfade(true)
                    ?.use(BitmapPalette.Profile.VIBRANT_DARK)
                    ?.intoBackground(textVibrantDark, BitmapPalette.Swatch.RGB)
                    ?.intoTextColor(textVibrantDark, BitmapPalette.Swatch.BODY_TEXT_COLOR)
                    ?.crossfade(false)
                    ?.use(BitmapPalette.Profile.VIBRANT_LIGHT)
                    ?.intoBackground(textVibrantLight, BitmapPalette.Swatch.RGB)
                    ?.intoTextColor(textVibrantLight, BitmapPalette.Swatch.BODY_TEXT_COLOR)
                    ?.crossfade(true, 1000)
                    ?.use(BitmapPalette.Profile.MUTED)
                    ?.intoBackground(textMuted, BitmapPalette.Swatch.RGB)
                    ?.intoTextColor(textMuted, BitmapPalette.Swatch.BODY_TEXT_COLOR)
                    ?.use(BitmapPalette.Profile.MUTED_DARK)
                    ?.intoBackground(textMutedDark, BitmapPalette.Swatch.RGB)
                    ?.intoTextColor(textMutedDark, BitmapPalette.Swatch.BODY_TEXT_COLOR)
                    ?.crossfade(true, 2000)
                    ?.use(BitmapPalette.Profile.MUTED_LIGHT)
                    ?.intoBackground(textMutedLight, BitmapPalette.Swatch.RGB)
                    ?.intoTextColor(
                        textMutedLight,
                        BitmapPalette.Swatch.BODY_TEXT_COLOR
                    ) // optional
                    ?.intoCallBack(object : BitmapPalette.CallBack {
                        override fun onPaletteLoaded(@Nullable palette: Palette?) { //specific task

                        }
                    }) // optional
                    ?.setGlideListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            @Nullable e: GlideException?, model: Any,
                            target: Target<Drawable?>,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any,
                            target: Target<Drawable?>,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    }) // optional: do stuff with the builder
                    ?.setPaletteBuilderInterceptor(object :
                        BitmapPalette.PaletteBuilderInterceptor {
                        override fun intercept(builder: Palette.Builder?): Palette.Builder? {
                            return builder?.resizeBitmapArea(300 * 100)
                        }
                    })
            )
            .into(image)
    }
}
