package quang.app.luckymoney.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.media.ToneGenerator
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import quang.app.luckymoney.R

/**
 * Manages all audio for the app, including Background Music (BGM) and Sound Effects (SFX).
 * 
 * --- USER INSTRUCTIONS FOR REAL AUDIO ---
 * To use real high-quality audio instead of generated tones:
 * 1. Find the following .mp3 files (e.g., from Pixabay, Mixkit, or Zapsplat):
 *    - bgm_tet.mp3: A cheerful, festive Vietnamese Lunar New Year melody.
 *    - pull.mp3: A short "paper sliding" or "scratch" sound.
 *    - success.mp3: A rewarding "ta-da" or "ding" sound.
 *    - swipe.mp3: A quick "whoosh" sound.
 *    - flap.mp3: A soft "paper flap" or "click" sound.
 * 2. Place them in the `app/src/main/res/raw/` directory.
 * 3. Uncomment the loading and playing logic in this class.
 * -----------------------------------------
 */
class AudioManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var soundPool: SoundPool
    private val soundMap = mutableMapOf<String, Int>()
    private var toneGenerator: ToneGenerator? = null
    private val audioScope = CoroutineScope(Dispatchers.Default + Job())
    private var bgmJob: Job? = null

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        loadSounds()
        
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        } catch (e: Exception) {
            Log.e("AudioManager", "Failed to initialize ToneGenerator", e)
        }
    }

    private fun loadSounds() {
        // Load real audio resources if they exist, otherwise fallback logic in playSfx will handle it
        try {
            soundMap["pop"] = soundPool.load(context, R.raw.pop, 1)
            soundMap["pull"] = soundPool.load(context, R.raw.pull, 1)
            soundMap["success"] = soundPool.load(context, R.raw.success, 1)
            Log.d("AudioManager", "Successfully loaded SFX from res/raw")
        } catch (e: Exception) {
            Log.e("AudioManager", "Failed to load some audio resources, fallbacks will be used", e)
        }
    }

    fun startBgm() {
        if (mediaPlayer == null) {
            try {
                mediaPlayer = MediaPlayer.create(context, R.raw.bgm_tet)
                mediaPlayer?.isLooping = true
                mediaPlayer?.start()
                Log.d("AudioManager", "BGM started with R.raw.bgm_tet")
            } catch (e: Exception) {
                Log.e("AudioManager", "Failed to start BGM from resource, using fallback melody", e)
                // Fallback: Start a generated melody loop
                if (bgmJob == null || bgmJob?.isCompleted == true) {
                    bgmJob = audioScope.launch {
                        playFallbackMelody()
                    }
                }
            }
        }
    }

    private suspend fun playFallbackMelody() {
        val melody = listOf(
            ToneGenerator.TONE_DTMF_1 to 300L,
            ToneGenerator.TONE_DTMF_2 to 300L,
            ToneGenerator.TONE_DTMF_3 to 300L,
            ToneGenerator.TONE_DTMF_1 to 300L,
            ToneGenerator.TONE_DTMF_5 to 600L,
            ToneGenerator.TONE_DTMF_4 to 300L,
            ToneGenerator.TONE_DTMF_3 to 600L
        )
        
        while (true) {
            for ((tone, duration) in melody) {
                toneGenerator?.startTone(tone, duration.toInt() - 50)
                delay(duration)
            }
            delay(1000) // Gap between loops
        }
    }

    fun stopBgm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        bgmJob?.cancel()
        bgmJob = null
    }

    fun playSfx(soundName: String) {
        val soundId = soundMap[soundName]
        if (soundId != null && soundId != 0) {
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
        } else {
            // Fallback to ToneGenerator sequences
            audioScope.launch {
                playFallbackToneSequence(soundName)
            }
        }
    }

    private suspend fun playFallbackToneSequence(soundName: String) {
        when (soundName) {
            "pop", "page_swipe" -> {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 50)
            }
            "pull", "pull_dragging" -> {
                // Frictional/scratchy sound simulation - quick upward frequency shift
                val slide = listOf(
                    ToneGenerator.TONE_DTMF_1,
                    ToneGenerator.TONE_DTMF_4,
                    ToneGenerator.TONE_DTMF_7,
                    ToneGenerator.TONE_DTMF_A
                )
                for (tone in slide) {
                    toneGenerator?.startTone(tone, 15)
                    delay(20)
                }
            }
            "success", "reveal_success" -> {
                // Arpeggio/Fanfare
                val fanfare = listOf(
                    ToneGenerator.TONE_DTMF_1 to 100L,
                    ToneGenerator.TONE_DTMF_3 to 100L,
                    ToneGenerator.TONE_DTMF_5 to 100L,
                    ToneGenerator.TONE_DTMF_8 to 300L
                )
                for ((tone, duration) in fanfare) {
                    toneGenerator?.startTone(tone, duration.toInt())
                    delay(duration + 20)
                }
            }
            "envelope_created" -> {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
                delay(150)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
            }
            "flap_open" -> {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 100)
            }
        }
    }

    fun release() {
        stopBgm()
        soundPool.release()
        toneGenerator?.release()
    }

    companion object {
        @Volatile
        private var instance: quang.app.luckymoney.utils.AudioManager? = null

        fun getInstance(context: Context): quang.app.luckymoney.utils.AudioManager {
            return instance ?: synchronized(this) {
                instance ?: quang.app.luckymoney.utils.AudioManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
