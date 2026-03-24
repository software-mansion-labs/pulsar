import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  SafeAreaView,
} from 'react-native';
import { Presets, Settings } from 'react-native-pulsar';
import { runOnUI } from 'react-native-worklets';

interface PresetItem {
  name: string;
  displayName: string;
  play: () => void;
}

const PRESETS: PresetItem[] = [
  { name: 'SystemImpactLight', displayName: '💫 Impact Light', play: Presets.System.ImpactLight },
  { name: 'SystemImpactMedium', displayName: '⚡ Impact Medium', play: Presets.System.ImpactMedium },
  { name: 'SystemImpactHeavy', displayName: '💥 Impact Heavy', play: Presets.System.ImpactHeavy },
  { name: 'SystemImpactSoft', displayName: '🌸 Impact Soft', play: Presets.System.ImpactSoft },
  { name: 'SystemImpactRigid', displayName: '🔨 Impact Rigid', play: Presets.System.ImpactRigid },
  { name: 'SystemNotificationSuccess', displayName: '🔔 Notification Success', play: Presets.System.NotificationSuccess },
  { name: 'SystemNotificationWarning', displayName: '⚠️ Notification Warning', play: Presets.System.NotificationWarning },
  { name: 'SystemNotificationError', displayName: '🚨 Notification Error', play: Presets.System.NotificationError },
  { name: 'SystemSelection', displayName: '🎯 Selection', play: Presets.System.Selection },
// CODEGEN_BEGIN_{example_app_preset_list}
  { name: 'Afterglow', displayName: '📳 Afterglow', play: Presets.Afterglow },
  { name: 'Aftershock', displayName: '📳 Aftershock', play: Presets.Aftershock },
  { name: 'AimingFire', displayName: '📳 AimingFire', play: Presets.AimingFire },
  { name: 'AimingLock', displayName: '📳 AimingLock', play: Presets.AimingLock },
  { name: 'Alarm', displayName: '📳 Alarm', play: Presets.Alarm },
  { name: 'AngerFrustration', displayName: '📳 AngerFrustration', play: Presets.AngerFrustration },
  { name: 'Anvil', displayName: '📳 Anvil', play: Presets.Anvil },
  { name: 'Applause', displayName: '📳 Applause', play: Presets.Applause },
  { name: 'Attention', displayName: '📳 Attention', play: Presets.Attention },
  { name: 'BalloonPop', displayName: '📳 BalloonPop', play: Presets.BalloonPop },
  { name: 'BangDoor', displayName: '📳 BangDoor', play: Presets.BangDoor },
  { name: 'Barrage', displayName: '📳 Barrage', play: Presets.Barrage },
  { name: 'BassDrop', displayName: '📳 BassDrop', play: Presets.BassDrop },
  { name: 'BellToll', displayName: '📳 BellToll', play: Presets.BellToll },
  { name: 'Blip', displayName: '📳 Blip', play: Presets.Blip },
  { name: 'Bloom', displayName: '📳 Bloom', play: Presets.Bloom },
  { name: 'Bongo', displayName: '📳 Bongo', play: Presets.Bongo },
  { name: 'Boulder', displayName: '📳 Boulder', play: Presets.Boulder },
  { name: 'BreakingWave', displayName: '📳 BreakingWave', play: Presets.BreakingWave },
  { name: 'Breath', displayName: '📳 Breath', play: Presets.Breath },
  { name: 'Buildup', displayName: '📳 Buildup', play: Presets.Buildup },
  { name: 'Cadence', displayName: '📳 Cadence', play: Presets.Cadence },
  { name: 'CameraShutter', displayName: '📳 CameraShutter', play: Presets.CameraShutter },
  { name: 'Canter', displayName: '📳 Canter', play: Presets.Canter },
  { name: 'Cascade', displayName: '📳 Cascade', play: Presets.Cascade },
  { name: 'Castanets', displayName: '📳 Castanets', play: Presets.Castanets },
  { name: 'CatPaw', displayName: '📳 CatPaw', play: Presets.CatPaw },
  { name: 'Chip', displayName: '📳 Chip', play: Presets.Chip },
  { name: 'Chirp', displayName: '📳 Chirp', play: Presets.Chirp },
  { name: 'Cleave', displayName: '📳 Cleave', play: Presets.Cleave },
  { name: 'Click', displayName: '📳 Click', play: Presets.Click },
  { name: 'CoinDrop', displayName: '📳 CoinDrop', play: Presets.CoinDrop },
  { name: 'CombinationLock', displayName: '📳 CombinationLock', play: Presets.CombinationLock },
  { name: 'Confirm', displayName: '📳 Confirm', play: Presets.Confirm },
  { name: 'Cowboy', displayName: '📳 Cowboy', play: Presets.Cowboy },
  { name: 'Crescendo', displayName: '📳 Crescendo', play: Presets.Crescendo },
  { name: 'CrossedEyes', displayName: '📳 CrossedEyes', play: Presets.CrossedEyes },
  { name: 'Cursing', displayName: '📳 Cursing', play: Presets.Cursing },
  { name: 'Dewdrop', displayName: '📳 Dewdrop', play: Presets.Dewdrop },
  { name: 'Dirge', displayName: '📳 Dirge', play: Presets.Dirge },
  { name: 'Dissolve', displayName: '📳 Dissolve', play: Presets.Dissolve },
  { name: 'DogBark', displayName: '📳 DogBark', play: Presets.DogBark },
  { name: 'Drone', displayName: '📳 Drone', play: Presets.Drone },
  { name: 'EngineRev', displayName: '📳 EngineRev', play: Presets.EngineRev },
  { name: 'ErrorBuzz', displayName: '📳 ErrorBuzz', play: Presets.ErrorBuzz },
  { name: 'ExplodingHead', displayName: '📳 ExplodingHead', play: Presets.ExplodingHead },
  { name: 'Explosion', displayName: '📳 Explosion', play: Presets.Explosion },
  { name: 'EyeRolling', displayName: '📳 EyeRolling', play: Presets.EyeRolling },
  { name: 'FadeOut', displayName: '📳 FadeOut', play: Presets.FadeOut },
  { name: 'Fanfare', displayName: '📳 Fanfare', play: Presets.Fanfare },
  { name: 'Feather', displayName: '📳 Feather', play: Presets.Feather },
  { name: 'FingerDrum', displayName: '📳 FingerDrum', play: Presets.FingerDrum },
  { name: 'Firecracker', displayName: '📳 Firecracker', play: Presets.Firecracker },
  { name: 'Fizz', displayName: '📳 Fizz', play: Presets.Fizz },
  { name: 'Flick', displayName: '📳 Flick', play: Presets.Flick },
  { name: 'Gallop', displayName: '📳 Gallop', play: Presets.Gallop },
  { name: 'GameCombo', displayName: '📳 GameCombo', play: Presets.GameCombo },
  { name: 'GameHit', displayName: '📳 GameHit', play: Presets.GameHit },
  { name: 'GameLevelUp', displayName: '📳 GameLevelUp', play: Presets.GameLevelUp },
  { name: 'GamePickup', displayName: '📳 GamePickup', play: Presets.GamePickup },
  { name: 'Gavel', displayName: '📳 Gavel', play: Presets.Gavel },
  { name: 'Glitch', displayName: '📳 Glitch', play: Presets.Glitch },
  { name: 'GravityFreefall', displayName: '📳 GravityFreefall', play: Presets.GravityFreefall },
  { name: 'GrinningSquinting', displayName: '📳 GrinningSquinting', play: Presets.GrinningSquinting },
  { name: 'GuitarStrum', displayName: '📳 GuitarStrum', play: Presets.GuitarStrum },
  { name: 'Hail', displayName: '📳 Hail', play: Presets.Hail },
  { name: 'Heartbeat', displayName: '📳 Heartbeat', play: Presets.Heartbeat },
  { name: 'Herald', displayName: '📳 Herald', play: Presets.Herald },
  { name: 'HoofBeat', displayName: '📳 HoofBeat', play: Presets.HoofBeat },
  { name: 'Ignition', displayName: '📳 Ignition', play: Presets.Ignition },
  { name: 'Jolt', displayName: '📳 Jolt', play: Presets.Jolt },
  { name: 'KeyboardMechanical', displayName: '📳 KeyboardMechanical', play: Presets.KeyboardMechanical },
  { name: 'KeyboardMembrane', displayName: '📳 KeyboardMembrane', play: Presets.KeyboardMembrane },
  { name: 'KnockDoor', displayName: '📳 KnockDoor', play: Presets.KnockDoor },
  { name: 'Latch', displayName: '📳 Latch', play: Presets.Latch },
  { name: 'LevelUp', displayName: '📳 LevelUp', play: Presets.LevelUp },
  { name: 'Lighthouse', displayName: '📳 Lighthouse', play: Presets.Lighthouse },
  { name: 'LoaderBreathing', displayName: '📳 LoaderBreathing', play: Presets.LoaderBreathing },
  { name: 'LoaderPulse', displayName: '📳 LoaderPulse', play: Presets.LoaderPulse },
  { name: 'LoaderRadar', displayName: '📳 LoaderRadar', play: Presets.LoaderRadar },
  { name: 'LoaderSpin', displayName: '📳 LoaderSpin', play: Presets.LoaderSpin },
  { name: 'LoaderWave', displayName: '📳 LoaderWave', play: Presets.LoaderWave },
  { name: 'Lock', displayName: '📳 Lock', play: Presets.Lock },
  { name: 'LongPress', displayName: '📳 LongPress', play: Presets.LongPress },
  { name: 'March', displayName: '📳 March', play: Presets.March },
  { name: 'MarioGameOver', displayName: '📳 MarioGameOver', play: Presets.MarioGameOver },
  { name: 'Metronome', displayName: '📳 Metronome', play: Presets.Metronome },
  { name: 'Murmur', displayName: '📳 Murmur', play: Presets.Murmur },
  { name: 'NewMessage', displayName: '📳 NewMessage', play: Presets.NewMessage },
  { name: 'Notification', displayName: '📳 Notification', play: Presets.Notification },
  { name: 'NotificationKnock', displayName: '📳 NotificationKnock', play: Presets.NotificationKnock },
  { name: 'NotificationUrgent', displayName: '📳 NotificationUrgent', play: Presets.NotificationUrgent },
  { name: 'NotifyInfoStandard', displayName: '📳 NotifyInfoStandard', play: Presets.NotifyInfoStandard },
  { name: 'NotifyReminderFinal', displayName: '📳 NotifyReminderFinal', play: Presets.NotifyReminderFinal },
  { name: 'NotifyReminderNudge', displayName: '📳 NotifyReminderNudge', play: Presets.NotifyReminderNudge },
  { name: 'NotifySocialMention', displayName: '📳 NotifySocialMention', play: Presets.NotifySocialMention },
  { name: 'NotifySocialMessage', displayName: '📳 NotifySocialMessage', play: Presets.NotifySocialMessage },
  { name: 'NotifyTimerDone', displayName: '📳 NotifyTimerDone', play: Presets.NotifyTimerDone },
  { name: 'PassingCar', displayName: '📳 PassingCar', play: Presets.PassingCar },
  { name: 'Patter', displayName: '📳 Patter', play: Presets.Patter },
  { name: 'Peal', displayName: '📳 Peal', play: Presets.Peal },
  { name: 'Peck', displayName: '📳 Peck', play: Presets.Peck },
  { name: 'Pendulum', displayName: '📳 Pendulum', play: Presets.Pendulum },
  { name: 'Ping', displayName: '📳 Ping', play: Presets.Ping },
  { name: 'Piston', displayName: '📳 Piston', play: Presets.Piston },
  { name: 'Plunk', displayName: '📳 Plunk', play: Presets.Plunk },
  { name: 'PowerDown', displayName: '📳 PowerDown', play: Presets.PowerDown },
  { name: 'Propel', displayName: '📳 Propel', play: Presets.Propel },
  { name: 'Rain', displayName: '📳 Rain', play: Presets.Rain },
  { name: 'Ratchet', displayName: '📳 Ratchet', play: Presets.Ratchet },
  { name: 'ReadySteadyGo', displayName: '📳 ReadySteadyGo', play: Presets.ReadySteadyGo },
  { name: 'Rebound', displayName: '📳 Rebound', play: Presets.Rebound },
  { name: 'ReliefSigh', displayName: '📳 ReliefSigh', play: Presets.ReliefSigh },
  { name: 'Ripple', displayName: '📳 Ripple', play: Presets.Ripple },
  { name: 'Rivet', displayName: '📳 Rivet', play: Presets.Rivet },
  { name: 'Rustle', displayName: '📳 Rustle', play: Presets.Rustle },
  { name: 'Searching', displayName: '📳 Searching', play: Presets.Searching },
  { name: 'SearchSuccess', displayName: '📳 SearchSuccess', play: Presets.SearchSuccess },
  { name: 'SelectionSnap', displayName: '📳 SelectionSnap', play: Presets.SelectionSnap },
  { name: 'Shockwave', displayName: '📳 Shockwave', play: Presets.Shockwave },
  { name: 'Sneezing', displayName: '📳 Sneezing', play: Presets.Sneezing },
  { name: 'Spark', displayName: '📳 Spark', play: Presets.Spark },
  { name: 'Stampede', displayName: '📳 Stampede', play: Presets.Stampede },
  { name: 'Stomp', displayName: '📳 Stomp', play: Presets.Stomp },
  { name: 'StoneSkip', displayName: '📳 StoneSkip', play: Presets.StoneSkip },
  { name: 'Strike', displayName: '📳 Strike', play: Presets.Strike },
  { name: 'SuccessFlourish', displayName: '📳 SuccessFlourish', play: Presets.SuccessFlourish },
  { name: 'SurpriseGasp', displayName: '📳 SurpriseGasp', play: Presets.SurpriseGasp },
  { name: 'Sway', displayName: '📳 Sway', play: Presets.Sway },
  { name: 'Syncopate', displayName: '📳 Syncopate', play: Presets.Syncopate },
  { name: 'Tada', displayName: '📳 Tada', play: Presets.Tada },
  { name: 'Thud', displayName: '📳 Thud', play: Presets.Thud },
  { name: 'Thump', displayName: '📳 Thump', play: Presets.Thump },
  { name: 'Thunder', displayName: '📳 Thunder', play: Presets.Thunder },
  { name: 'ThunderRoll', displayName: '📳 ThunderRoll', play: Presets.ThunderRoll },
  { name: 'TickTock', displayName: '📳 TickTock', play: Presets.TickTock },
  { name: 'TidalSurge', displayName: '📳 TidalSurge', play: Presets.TidalSurge },
  { name: 'TideSwell', displayName: '📳 TideSwell', play: Presets.TideSwell },
  { name: 'Tremor', displayName: '📳 Tremor', play: Presets.Tremor },
  { name: 'Typewriter', displayName: '📳 Typewriter', play: Presets.Typewriter },
  { name: 'Victory', displayName: '📳 Victory', play: Presets.Victory },
  { name: 'Vomiting', displayName: '📳 Vomiting', play: Presets.Vomiting },
  { name: 'Vortex', displayName: '📳 Vortex', play: Presets.Vortex },
  { name: 'WarDrum', displayName: '📳 WarDrum', play: Presets.WarDrum },
  { name: 'WarningPulse', displayName: '📳 WarningPulse', play: Presets.WarningPulse },
  { name: 'WarningUrgent', displayName: '📳 WarningUrgent', play: Presets.WarningUrgent },
  { name: 'Waterfall', displayName: '📳 Waterfall', play: Presets.Waterfall },
  { name: 'Wisp', displayName: '📳 Wisp', play: Presets.Wisp },
  { name: 'Wobble', displayName: '📳 Wobble', play: Presets.Wobble },
  { name: 'Woodpecker', displayName: '📳 Woodpecker', play: Presets.Woodpecker },
  { name: 'ZeldaChest', displayName: '📳 ZeldaChest', play: Presets.ZeldaChest },
  { name: 'Zipper', displayName: '📳 Zipper', play: Presets.Zipper },
// CODEGEN_END_{example_app_preset_list}
];

export default function PresetsScreen() {
  const handlePlayPreset = (play: () => void) => {
    runOnUI(() => {
      'worklet';
      play();
    })();
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollContent}>
        <Text style={styles.title}>Haptic Presets</Text>
        <Text style={styles.subtitle}>
          Test all available haptic presets in the Pulsar library
        </Text>

        <View style={styles.presetsList}>
          {PRESETS.map((preset) => (
            <View key={preset.name} style={styles.presetRow}>
              <Text style={styles.presetName}>{preset.displayName}</Text>
              <TouchableOpacity
                style={styles.playButton}
                onPress={() => handlePlayPreset(preset.play)}>
                <Text style={styles.playButtonText}>▶ Play</Text>
              </TouchableOpacity>
            </View>
          ))}
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  scrollContent: {
    padding: 20,
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 14,
    color: '#666',
    marginBottom: 24,
  },
  presetsList: {
    gap: 12,
  },
  presetRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: 'white',
    padding: 16,
    borderRadius: 12,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 2,
  },
  presetName: {
    fontSize: 16,
    color: '#333',
    flex: 1,
  },
  playButton: {
    backgroundColor: '#007AFF',
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 8,
  },
  playButtonText: {
    color: 'white',
    fontSize: 14,
    fontWeight: '600',
  },
});
