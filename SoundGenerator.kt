// MainActivity.kt
package com.example.synthesizer

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.*

// MARK: - Data Classes
enum class WaveformType {
    SINE, SQUARE, SAWTOOTH, TRIANGLE
}

data class OscillatorConfig(
    var waveformType: WaveformType,
    var baseFreq: Float,
    var freqMod: Float,
    var gain: Float,
    var attack: Float,
    var release: Float
)

// MARK: - Audio Engine
class AudioEngine {
    private val sampleRate = 44100
    private val bufferSize = AudioTrack.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )
    
    private var audioTracks = mutableListOf<AudioTrack>()
    private var isRunning = false
    private var modulationPhases = mutableListOf(0.0, 0.0, 0.0, 0.0)
    
    var masterGain = 0.3f
    var filterFrequency = 2000f
    
    fun startSynthesis(configs: List<OscillatorConfig>): List<Job> {
        isRunning = true
        modulationPhases = mutableListOf(0.0, 0.0, 0.0, 0.0)
        
        return configs.mapIndexed { index, config ->
            val audioTrack = createAudioTrack()
            audioTracks.add(audioTrack)
            audioTrack.play()
            
            kotlinx.coroutines.GlobalScope.launch(Dispatchers.Default) {
                generateAudioLoop(audioTrack, config, index)
            }
        }
    }
    
    private fun createAudioTrack(): AudioTrack {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        
        val audioFormat = AudioFormat.Builder()
            .setSampleRate(sampleRate)
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()
        
        return AudioTrack.Builder()
            .setAudioAttributes(audioAttributes)
            .setAudioFormat(audioFormat)
            .setBufferSizeInBytes(bufferSize * 2)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()
    }
    
    private suspend fun generateAudioLoop(
        audioTrack: AudioTrack,
        config: OscillatorConfig,
        index: Int
    ) {
        val chunkSize = 1024
        val buffer = ShortArray(chunkSize)
        var phase = 0.0
        
        while (isRunning) {
            // Calculate modulated frequency
            modulationPhases[index] += 0.01
            val lfo = sin(modulationPhases[index])
            val freqRange = config.baseFreq * (config.freqMod - 1.0f)
            val currentFreq = config.baseFreq + freqRange * ((lfo + 1.0) / 2.0).toFloat()
            
            // Generate waveform
            for (i in 0 until chunkSize) {
                val sample = generateSample(phase, config.waveformType)
                
                // Apply gain and filter
                val filtered = applyLowPassFilter(sample, currentFreq)
                val gained = filtered * config.gain * masterGain
                
                buffer[i] = (gained * 32767).toInt().coerceIn(-32768, 32767).toShort()
                
                phase += 2.0 * PI * currentFreq / sampleRate
                if (phase >= 2.0 * PI) phase -= 2.0 * PI
            }
            
            withContext(Dispatchers.IO) {
                audioTrack.write(buffer, 0, chunkSize)
            }
            
            delay(10) // Small delay to prevent overwhelming the CPU
        }
    }
    
    private fun generateSample(phase: Double, type: WaveformType): Float {
        return when (type) {
            WaveformType.SINE -> sin(phase).toFloat()
            WaveformType.SQUARE -> if (sin(phase) >= 0) 1f else -1f
            WaveformType.SAWTOOTH -> {
                val normalizedPhase = (phase / (2.0 * PI)).toFloat()
                (2f * normalizedPhase - 1f)
            }
            WaveformType.TRIANGLE -> {
                val normalizedPhase = (phase / (2.0 * PI)).toFloat()
                if (normalizedPhase < 0.5f) {
                    4f * normalizedPhase - 1f
                } else {
                    3f - 4f * normalizedPhase
                }
            }
        }
    }
    
    private var prevFilteredSample = 0f
    
    private fun applyLowPassFilter(sample: Float, frequency: Float): Float {
        val rc = 1.0f / (2.0f * PI.toFloat() * filterFrequency)
        val dt = 1.0f / sampleRate
        val alpha = dt / (rc + dt)
        
        val filtered = prevFilteredSample + alpha * (sample - prevFilteredSample)
        prevFilteredSample = filtered
        
        return filtered
    }
    
    fun stopSynthesis() {
        isRunning = false
        
        audioTracks.forEach { track ->
            try {
                track.stop()
                track.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        audioTracks.clear()
        prevFilteredSample = 0f
    }
    
    fun updateOscillatorGain(index: Int, gain: Float, configs: List<OscillatorConfig>) {
        if (index < configs.size) {
            configs[index].gain = gain
        }
    }
}

// MARK: - ViewModel
class SynthesizerViewModel : ViewModel() {
    private val audioEngine = AudioEngine()
    
    var isPlaying by mutableStateOf(false)
        private set
    
    var masterGain by mutableStateOf(0.3f)
    var filterFrequency by mutableStateOf(2000f)
    
    var oscillatorConfigs = mutableStateListOf(
        OscillatorConfig(WaveformType.SINE, 220f, 2f, 0.3f, 0.5f, 0.5f),
        OscillatorConfig(WaveformType.SQUARE, 440f, 3f, 0.2f, 0.3f, 0.4f),
        OscillatorConfig(WaveformType.SAWTOOTH, 110f, 1.5f, 0.25f, 0.4f, 0.6f),
        OscillatorConfig(WaveformType.TRIANGLE, 330f, 2.5f, 0.28f, 0.2f, 0.3f)
    )
    
    fun startSynthesis() {
        if (!isPlaying) {
            isPlaying = true
            audioEngine.startSynthesis(oscillatorConfigs)
        }
    }
    
    fun stopSynthesis() {
        if (isPlaying) {
            isPlaying = false
            audioEngine.stopSynthesis()
        }
    }
    
    fun updateMasterGain(value: Float) {
        masterGain = value
        audioEngine.masterGain = value
    }
    
    fun updateFilterFrequency(value: Float) {
        filterFrequency = value
        audioEngine.filterFrequency = value
    }
    
    fun updateOscillatorConfig(index: Int, config: OscillatorConfig) {
        oscillatorConfigs[index] = config
    }
    
    override fun onCleared() {
        super.onCleared()
        audioEngine.stopSynthesis()
    }
}

// MARK: - Composables
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SynthesizerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SynthesizerScreen()
                }
            }
        }
    }
}

@Composable
fun SynthesizerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF6366F1),
            secondary = Color(0xFF8B5CF6),
            background = Color(0xFF1E1E2E),
            surface = Color(0xFF2A2A3E)
        ),
        content = content
    )
}

@Composable
fun SynthesizerScreen(viewModel: SynthesizerViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF667EEA),
                        Color(0xFF764BA2)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎵 Multi-Oscillator Synthesizer",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 20.dp)
            )
            
            // Global Controls
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.15f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SliderControl(
                        label = "Master Gain",
                        value = viewModel.masterGain,
                        valueRange = 0f..1f,
                        onValueChange = { viewModel.updateMasterGain(it) },
                        valueText = String.format("%.2f", viewModel.masterGain)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    SliderControl(
                        label = "Low Pass Filter",
                        value = viewModel.filterFrequency,
                        valueRange = 100f..5000f,
                        onValueChange = { viewModel.updateFilterFrequency(it) },
                        valueText = "${viewModel.filterFrequency.toInt()} Hz"
                    )
                }
            }
            
            // Oscillators
            viewModel.oscillatorConfigs.forEachIndexed { index, config ->
                OscillatorCard(
                    index = index,
                    config = config,
                    onConfigChange = { viewModel.updateOscillatorConfig(index, it) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            // Control Buttons
            Spacer(modifier = Modifier.height(8.dp))
            
            if (!viewModel.isPlaying) {
                Button(
                    onClick = { viewModel.startSynthesis() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6366F1)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Start Synthesis", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = { viewModel.stopSynthesis() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEC4899)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Stop Synthesis", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun OscillatorCard(
    index: Int,
    config: OscillatorConfig,
    onConfigChange: (OscillatorConfig) -> Unit
) {
    val waveformNames = listOf("SINE", "SQUARE", "SAWTOOTH", "TRIANGLE")
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.15f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Oscillator ${index + 1} - ${waveformNames[index]}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            SliderControl(
                label = "Base Frequency",
                value = config.baseFreq,
                valueRange = 50f..880f,
                onValueChange = { onConfigChange(config.copy(baseFreq = it)) },
                valueText = "${config.baseFreq.toInt()} Hz"
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SliderControl(
                label = "Frequency Modulation",
                value = config.freqMod,
                valueRange = 0.5f..5f,
                onValueChange = { onConfigChange(config.copy(freqMod = it)) },
                valueText = String.format("%.1fx", config.freqMod)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SliderControl(
                label = "Gain",
                value = config.gain,
                valueRange = 0f..0.5f,
                onValueChange = { onConfigChange(config.copy(gain = it)) },
                valueText = String.format("%.2f", config.gain)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SliderControl(
                label = "Attack",
                value = config.attack,
                valueRange = 0.1f..2f,
                onValueChange = { onConfigChange(config.copy(attack = it)) },
                valueText = String.format("%.1fs", config.attack)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SliderControl(
                label = "Release",
                value = config.release,
                valueRange = 0.1f..2f,
                onValueChange = { onConfigChange(config.copy(release = it)) },
                valueText = String.format("%.1fs", config.release)
            )
        }
    }
}

@Composable
fun SliderControl(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    valueText: String
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
            )
            Text(
                text = valueText,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White.copy(alpha = 0.7f),
                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
            )
        )
    }
}