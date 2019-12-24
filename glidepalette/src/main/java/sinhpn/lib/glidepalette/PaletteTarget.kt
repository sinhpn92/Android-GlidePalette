package sinhpn.lib.glidepalette

import android.util.Pair
import android.view.View
import android.widget.TextView
import java.util.*

class PaletteTarget(@BitmapPalette.Profile paletteProfile: Int) {
    @BitmapPalette.Profile
    var paletteProfile: Int = BitmapPalette.Profile.VIBRANT
    var targetsBackground: ArrayList<Pair<View?, Int?>>? =
        ArrayList()
    var targetsText: ArrayList<Pair<TextView?, Int?>>? =
        ArrayList()
    var targetCrossfade = false
    var targetCrossfadeSpeed = DEFAULT_CROSSFADE_SPEED
    fun clear() {
        targetsBackground!!.clear()
        targetsText!!.clear()
        targetsBackground = null
        targetsText = null
        targetCrossfade = false
        targetCrossfadeSpeed = DEFAULT_CROSSFADE_SPEED
    }

    companion object {
        protected const val DEFAULT_CROSSFADE_SPEED = 300
    }

    init {
        this.paletteProfile = paletteProfile
    }
}