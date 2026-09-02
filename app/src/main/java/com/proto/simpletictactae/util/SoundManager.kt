package com.proto.simpletictactae.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SoundManager(private val context: Context) {

    private var toneGenerator: ToneGenerator? = null
    var isSoundEnabled: Boolean = true

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
        } catch (_: Exception) {
            toneGenerator = null
        }
    }

    fun playClick() {
        if (!isSoundEnabled) return
        playTone(ToneGenerator.TONE_PROP_BEEP, 50)
    }

    fun playMoveX() {
        if (!isSoundEnabled) return
        playTone(ToneGenerator.TONE_DTMF_1, 70)
    }

    fun playMoveO() {
        if (!isSoundEnabled) return
        playTone(ToneGenerator.TONE_DTMF_5, 70)
    }

    fun playWin() {
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            playTone(ToneGenerator.TONE_DTMF_3, 100)
            delay(100)
            playTone(ToneGenerator.TONE_DTMF_6, 100)
            delay(100)
            playTone(ToneGenerator.TONE_DTMF_9, 200)
        }
    }

    fun playDraw() {
        if (!isSoundEnabled) return
        playTone(ToneGenerator.TONE_PROP_NACK, 180)
    }

    private fun playTone(toneType: Int, durationMs: Int) {
        try {
            toneGenerator?.startTone(toneType, durationMs)
        } catch (_: Exception) {
        }
    }

    fun release() {
        try {
            toneGenerator?.release()
            toneGenerator = null
        } catch (_: Exception) {
        }
    }
}
