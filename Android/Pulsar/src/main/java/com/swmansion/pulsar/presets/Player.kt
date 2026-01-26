package com.swmansion.pulsar.presets

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.swmansion.pulsar.Pulsar
import com.swmansion.pulsar.types.PatternData
import com.swmansion.pulsar.composers.PatternComposerImpl

open class Player(
    haptics: Pulsar,
    pattern: PatternData,
) {
    private var composer: PatternComposerImpl = haptics.PatternComposer()

    init {
        composer.parsePattern(pattern)
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun play() {
        composer.play()
    }

}
