package com.swmansion.pulsarapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swmansion.pulsar.Pulsar

data class PresetItem(
    val name: String,
    val action: () -> Unit
)

@Composable
fun PresetsScreen(pulsar: Pulsar?) {
    val presets = androidx.compose.runtime.remember {
        listOf(
// CODEGEN_BEGIN_{example_app_preset_list}
            PresetItem("Afterglow") { pulsar?.getPresets()?.afterglow() },
            PresetItem("Aftershock") { pulsar?.getPresets()?.aftershock() },
            PresetItem("AimingFire") { pulsar?.getPresets()?.aimingFire() },
            PresetItem("AimingLock") { pulsar?.getPresets()?.aimingLock() },
            PresetItem("Alarm") { pulsar?.getPresets()?.alarm() },
            PresetItem("AngerFrustration") { pulsar?.getPresets()?.angerFrustration() },
            PresetItem("Anvil") { pulsar?.getPresets()?.anvil() },
            PresetItem("Applause") { pulsar?.getPresets()?.applause() },
            PresetItem("Attention") { pulsar?.getPresets()?.attention() },
            PresetItem("BalloonPop") { pulsar?.getPresets()?.balloonPop() },
            PresetItem("BangDoor") { pulsar?.getPresets()?.bangDoor() },
            PresetItem("Barrage") { pulsar?.getPresets()?.barrage() },
            PresetItem("BassDrop") { pulsar?.getPresets()?.bassDrop() },
            PresetItem("BellToll") { pulsar?.getPresets()?.bellToll() },
            PresetItem("Blip") { pulsar?.getPresets()?.blip() },
            PresetItem("Bloom") { pulsar?.getPresets()?.bloom() },
            PresetItem("Bongo") { pulsar?.getPresets()?.bongo() },
            PresetItem("Boulder") { pulsar?.getPresets()?.boulder() },
            PresetItem("BreakingWave") { pulsar?.getPresets()?.breakingWave() },
            PresetItem("Breath") { pulsar?.getPresets()?.breath() },
            PresetItem("Buildup") { pulsar?.getPresets()?.buildup() },
            PresetItem("Cadence") { pulsar?.getPresets()?.cadence() },
            PresetItem("CameraShutter") { pulsar?.getPresets()?.cameraShutter() },
            PresetItem("Canter") { pulsar?.getPresets()?.canter() },
            PresetItem("Cascade") { pulsar?.getPresets()?.cascade() },
            PresetItem("Castanets") { pulsar?.getPresets()?.castanets() },
            PresetItem("CatPaw") { pulsar?.getPresets()?.catPaw() },
            PresetItem("Chip") { pulsar?.getPresets()?.chip() },
            PresetItem("Chirp") { pulsar?.getPresets()?.chirp() },
            PresetItem("Cleave") { pulsar?.getPresets()?.cleave() },
            PresetItem("Click") { pulsar?.getPresets()?.click() },
            PresetItem("CoinDrop") { pulsar?.getPresets()?.coinDrop() },
            PresetItem("CombinationLock") { pulsar?.getPresets()?.combinationLock() },
            PresetItem("Confirm") { pulsar?.getPresets()?.confirm() },
            PresetItem("Cowboy") { pulsar?.getPresets()?.cowboy() },
            PresetItem("Crescendo") { pulsar?.getPresets()?.crescendo() },
            PresetItem("CrossedEyes") { pulsar?.getPresets()?.crossedEyes() },
            PresetItem("Cursing") { pulsar?.getPresets()?.cursing() },
            PresetItem("Dewdrop") { pulsar?.getPresets()?.dewdrop() },
            PresetItem("Dirge") { pulsar?.getPresets()?.dirge() },
            PresetItem("Dissolve") { pulsar?.getPresets()?.dissolve() },
            PresetItem("DogBark") { pulsar?.getPresets()?.dogBark() },
            PresetItem("Drone") { pulsar?.getPresets()?.drone() },
            PresetItem("EngineRev") { pulsar?.getPresets()?.engineRev() },
            PresetItem("ErrorBuzz") { pulsar?.getPresets()?.errorBuzz() },
            PresetItem("ExplodingHead") { pulsar?.getPresets()?.explodingHead() },
            PresetItem("Explosion") { pulsar?.getPresets()?.explosion() },
            PresetItem("EyeRolling") { pulsar?.getPresets()?.eyeRolling() },
            PresetItem("FadeOut") { pulsar?.getPresets()?.fadeOut() },
            PresetItem("Fanfare") { pulsar?.getPresets()?.fanfare() },
            PresetItem("Feather") { pulsar?.getPresets()?.feather() },
            PresetItem("FingerDrum") { pulsar?.getPresets()?.fingerDrum() },
            PresetItem("Firecracker") { pulsar?.getPresets()?.firecracker() },
            PresetItem("Fizz") { pulsar?.getPresets()?.fizz() },
            PresetItem("Flick") { pulsar?.getPresets()?.flick() },
            PresetItem("Gallop") { pulsar?.getPresets()?.gallop() },
            PresetItem("GameCombo") { pulsar?.getPresets()?.gameCombo() },
            PresetItem("GameHit") { pulsar?.getPresets()?.gameHit() },
            PresetItem("GameLevelUp") { pulsar?.getPresets()?.gameLevelUp() },
            PresetItem("GamePickup") { pulsar?.getPresets()?.gamePickup() },
            PresetItem("Gavel") { pulsar?.getPresets()?.gavel() },
            PresetItem("Glitch") { pulsar?.getPresets()?.glitch() },
            PresetItem("GravityFreefall") { pulsar?.getPresets()?.gravityFreefall() },
            PresetItem("GrinningSquinting") { pulsar?.getPresets()?.grinningSquinting() },
            PresetItem("GuitarStrum") { pulsar?.getPresets()?.guitarStrum() },
            PresetItem("Hail") { pulsar?.getPresets()?.hail() },
            PresetItem("Heartbeat") { pulsar?.getPresets()?.heartbeat() },
            PresetItem("Herald") { pulsar?.getPresets()?.herald() },
            PresetItem("HoofBeat") { pulsar?.getPresets()?.hoofBeat() },
            PresetItem("Ignition") { pulsar?.getPresets()?.ignition() },
            PresetItem("Jolt") { pulsar?.getPresets()?.jolt() },
            PresetItem("KeyboardMechanical") { pulsar?.getPresets()?.keyboardMechanical() },
            PresetItem("KeyboardMembrane") { pulsar?.getPresets()?.keyboardMembrane() },
            PresetItem("KnockDoor") { pulsar?.getPresets()?.knockDoor() },
            PresetItem("Latch") { pulsar?.getPresets()?.latch() },
            PresetItem("LevelUp") { pulsar?.getPresets()?.levelUp() },
            PresetItem("Lighthouse") { pulsar?.getPresets()?.lighthouse() },
            PresetItem("LoaderBreathing") { pulsar?.getPresets()?.loaderBreathing() },
            PresetItem("LoaderPulse") { pulsar?.getPresets()?.loaderPulse() },
            PresetItem("LoaderRadar") { pulsar?.getPresets()?.loaderRadar() },
            PresetItem("LoaderSpin") { pulsar?.getPresets()?.loaderSpin() },
            PresetItem("LoaderWave") { pulsar?.getPresets()?.loaderWave() },
            PresetItem("Lock") { pulsar?.getPresets()?.lock() },
            PresetItem("LongPress") { pulsar?.getPresets()?.longPress() },
            PresetItem("March") { pulsar?.getPresets()?.march() },
            PresetItem("MarioGameOver") { pulsar?.getPresets()?.marioGameOver() },
            PresetItem("Metronome") { pulsar?.getPresets()?.metronome() },
            PresetItem("Murmur") { pulsar?.getPresets()?.murmur() },
            PresetItem("NewMessage") { pulsar?.getPresets()?.newMessage() },
            PresetItem("Notification") { pulsar?.getPresets()?.notification() },
            PresetItem("NotificationKnock") { pulsar?.getPresets()?.notificationKnock() },
            PresetItem("NotificationUrgent") { pulsar?.getPresets()?.notificationUrgent() },
            PresetItem("NotifyInfoStandard") { pulsar?.getPresets()?.notifyInfoStandard() },
            PresetItem("NotifyReminderFinal") { pulsar?.getPresets()?.notifyReminderFinal() },
            PresetItem("NotifyReminderNudge") { pulsar?.getPresets()?.notifyReminderNudge() },
            PresetItem("NotifySocialMention") { pulsar?.getPresets()?.notifySocialMention() },
            PresetItem("NotifySocialMessage") { pulsar?.getPresets()?.notifySocialMessage() },
            PresetItem("NotifyTimerDone") { pulsar?.getPresets()?.notifyTimerDone() },
            PresetItem("PassingCar") { pulsar?.getPresets()?.passingCar() },
            PresetItem("Patter") { pulsar?.getPresets()?.patter() },
            PresetItem("Peal") { pulsar?.getPresets()?.peal() },
            PresetItem("Peck") { pulsar?.getPresets()?.peck() },
            PresetItem("Pendulum") { pulsar?.getPresets()?.pendulum() },
            PresetItem("Ping") { pulsar?.getPresets()?.ping() },
            PresetItem("Piston") { pulsar?.getPresets()?.piston() },
            PresetItem("Plunk") { pulsar?.getPresets()?.plunk() },
            PresetItem("PowerDown") { pulsar?.getPresets()?.powerDown() },
            PresetItem("Propel") { pulsar?.getPresets()?.propel() },
            PresetItem("Rain") { pulsar?.getPresets()?.rain() },
            PresetItem("Ratchet") { pulsar?.getPresets()?.ratchet() },
            PresetItem("ReadySteadyGo") { pulsar?.getPresets()?.readySteadyGo() },
            PresetItem("Rebound") { pulsar?.getPresets()?.rebound() },
            PresetItem("ReliefSigh") { pulsar?.getPresets()?.reliefSigh() },
            PresetItem("Ripple") { pulsar?.getPresets()?.ripple() },
            PresetItem("Rivet") { pulsar?.getPresets()?.rivet() },
            PresetItem("Rustle") { pulsar?.getPresets()?.rustle() },
            PresetItem("Searching") { pulsar?.getPresets()?.searching() },
            PresetItem("SearchSuccess") { pulsar?.getPresets()?.searchSuccess() },
            PresetItem("SelectionSnap") { pulsar?.getPresets()?.selectionSnap() },
            PresetItem("Shockwave") { pulsar?.getPresets()?.shockwave() },
            PresetItem("Sneezing") { pulsar?.getPresets()?.sneezing() },
            PresetItem("Spark") { pulsar?.getPresets()?.spark() },
            PresetItem("Stampede") { pulsar?.getPresets()?.stampede() },
            PresetItem("Stomp") { pulsar?.getPresets()?.stomp() },
            PresetItem("StoneSkip") { pulsar?.getPresets()?.stoneSkip() },
            PresetItem("Strike") { pulsar?.getPresets()?.strike() },
            PresetItem("SuccessFlourish") { pulsar?.getPresets()?.successFlourish() },
            PresetItem("SurpriseGasp") { pulsar?.getPresets()?.surpriseGasp() },
            PresetItem("Sway") { pulsar?.getPresets()?.sway() },
            PresetItem("Syncopate") { pulsar?.getPresets()?.syncopate() },
            PresetItem("Tada") { pulsar?.getPresets()?.tada() },
            PresetItem("Thud") { pulsar?.getPresets()?.thud() },
            PresetItem("Thump") { pulsar?.getPresets()?.thump() },
            PresetItem("Thunder") { pulsar?.getPresets()?.thunder() },
            PresetItem("ThunderRoll") { pulsar?.getPresets()?.thunderRoll() },
            PresetItem("TickTock") { pulsar?.getPresets()?.tickTock() },
            PresetItem("TidalSurge") { pulsar?.getPresets()?.tidalSurge() },
            PresetItem("TideSwell") { pulsar?.getPresets()?.tideSwell() },
            PresetItem("Tremor") { pulsar?.getPresets()?.tremor() },
            PresetItem("Typewriter") { pulsar?.getPresets()?.typewriter() },
            PresetItem("Victory") { pulsar?.getPresets()?.victory() },
            PresetItem("Vomiting") { pulsar?.getPresets()?.vomiting() },
            PresetItem("Vortex") { pulsar?.getPresets()?.vortex() },
            PresetItem("WarDrum") { pulsar?.getPresets()?.warDrum() },
            PresetItem("WarningPulse") { pulsar?.getPresets()?.warningPulse() },
            PresetItem("WarningUrgent") { pulsar?.getPresets()?.warningUrgent() },
            PresetItem("Waterfall") { pulsar?.getPresets()?.waterfall() },
            PresetItem("Wisp") { pulsar?.getPresets()?.wisp() },
            PresetItem("Wobble") { pulsar?.getPresets()?.wobble() },
            PresetItem("Woodpecker") { pulsar?.getPresets()?.woodpecker() },
            PresetItem("ZeldaChest") { pulsar?.getPresets()?.zeldaChest() },
            PresetItem("Zipper") { pulsar?.getPresets()?.zipper() },
// CODEGEN_END_{example_app_preset_list}
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Haptics Presets",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp)
        ) {
            items(presets) { preset ->
                PresetItemRow(preset)
            }
        }
    }
}

@Composable
private fun PresetItemRow(preset: PresetItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            preset.name,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            fontSize = 16.sp
        )
        Button(
            onClick = preset.action,
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text("Play")
        }
    }
}

