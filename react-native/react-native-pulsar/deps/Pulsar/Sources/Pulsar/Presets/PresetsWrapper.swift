import AVFoundation
import UIKit
import Foundation
import AVFAudio

@objc public class PresetsWrapper : NSObject {
  private var playSound: Bool = true
  private var useCache: Bool = true
  private var cache: [String: Preset] = [:]
  private var haptics: Pulsar!
  
  private var mapper: [String: Preset.Type] = [
    "SystemImpactLight": SystemImpactLightPreset.self,
    "SystemImpactMedium": SystemImpactMediumPreset.self,
    "SystemImpactHeavy": SystemImpactHeavyPreset.self,
    "SystemImpactSoft": SystemImpactSoftPreset.self,
    "SystemImpactRigid": SystemImpactRigidPreset.self,
    "SystemNotificationSuccess": SystemNotificationSuccessPreset.self,
    "SystemNotificationWarning": SystemNotificationWarningPreset.self,
    "SystemNotificationError": SystemNotificationErrorPreset.self,
    "SystemSelection": SystemSelectionPreset.self,
    
// CODEGEN_BEGIN_{mappers}
    "Afterglow": AfterglowPreset.self,
    "Aftershock": AftershockPreset.self,
    "AimingFire": AimingFirePreset.self,
    "AimingLock": AimingLockPreset.self,
    "Alarm": AlarmPreset.self,
    "AngerFrustration": AngerFrustrationPreset.self,
    "Anvil": AnvilPreset.self,
    "Applause": ApplausePreset.self,
    "Attention": AttentionPreset.self,
    "BalloonPop": BalloonPopPreset.self,
    "BangDoor": BangDoorPreset.self,
    "Barrage": BarragePreset.self,
    "BassDrop": BassDropPreset.self,
    "BellToll": BellTollPreset.self,
    "Blip": BlipPreset.self,
    "Bloom": BloomPreset.self,
    "Bongo": BongoPreset.self,
    "Boulder": BoulderPreset.self,
    "BreakingWave": BreakingWavePreset.self,
    "Breath": BreathPreset.self,
    "Buildup": BuildupPreset.self,
    "Cadence": CadencePreset.self,
    "CameraShutter": CameraShutterPreset.self,
    "Canter": CanterPreset.self,
    "Cascade": CascadePreset.self,
    "Castanets": CastanetsPreset.self,
    "CatPaw": CatPawPreset.self,
    "Chip": ChipPreset.self,
    "Chirp": ChirpPreset.self,
    "Cleave": CleavePreset.self,
    "CoinDrop": CoinDropPreset.self,
    "CombinationLock": CombinationLockPreset.self,
    "Confirm": ConfirmPreset.self,
    "Cowboy": CowboyPreset.self,
    "Crescendo": CrescendoPreset.self,
    "CrossedEyes": CrossedEyesPreset.self,
    "Cursing": CursingPreset.self,
    "Dewdrop": DewdropPreset.self,
    "Dirge": DirgePreset.self,
    "Dissolve": DissolvePreset.self,
    "DogBark": DogBarkPreset.self,
    "Drone": DronePreset.self,
    "EngineRev": EngineRevPreset.self,
    "ErrorBuzz": ErrorBuzzPreset.self,
    "ExplodingHead": ExplodingHeadPreset.self,
    "Explosion": ExplosionPreset.self,
    "EyeRolling": EyeRollingPreset.self,
    "FadeOut": FadeOutPreset.self,
    "Fanfare": FanfarePreset.self,
    "Feather": FeatherPreset.self,
    "FingerDrum": FingerDrumPreset.self,
    "Firecracker": FirecrackerPreset.self,
    "Fizz": FizzPreset.self,
    "Flick": FlickPreset.self,
    "Gallop": GallopPreset.self,
    "GameCombo": GameComboPreset.self,
    "GameHit": GameHitPreset.self,
    "GameLevelUp": GameLevelUpPreset.self,
    "GamePickup": GamePickupPreset.self,
    "Gavel": GavelPreset.self,
    "Glitch": GlitchPreset.self,
    "GravityFreefall": GravityFreefallPreset.self,
    "GrinningSquinting": GrinningSquintingPreset.self,
    "GuitarStrum": GuitarStrumPreset.self,
    "Hail": HailPreset.self,
    "Heartbeat": HeartbeatPreset.self,
    "Herald": HeraldPreset.self,
    "HoofBeat": HoofBeatPreset.self,
    "Ignition": IgnitionPreset.self,
    "Jolt": JoltPreset.self,
    "KeyboardMechanical": KeyboardMechanicalPreset.self,
    "KeyboardMembrane": KeyboardMembranePreset.self,
    "KnockDoor": KnockDoorPreset.self,
    "Latch": LatchPreset.self,
    "LevelUp": LevelUpPreset.self,
    "Lighthouse": LighthousePreset.self,
    "LoaderBreathing": LoaderBreathingPreset.self,
    "LoaderPulse": LoaderPulsePreset.self,
    "LoaderRadar": LoaderRadarPreset.self,
    "LoaderSpin": LoaderSpinPreset.self,
    "LoaderWave": LoaderWavePreset.self,
    "Lock": LockPreset.self,
    "LongPress": LongPressPreset.self,
    "March": MarchPreset.self,
    "MarioGameOver": MarioGameOverPreset.self,
    "Metronome": MetronomePreset.self,
    "Murmur": MurmurPreset.self,
    "NewMessage": NewMessagePreset.self,
    "Notification": NotificationPreset.self,
    "NotificationKnock": NotificationKnockPreset.self,
    "NotificationUrgent": NotificationUrgentPreset.self,
    "NotifyInfoStandard": NotifyInfoStandardPreset.self,
    "NotifyReminderFinal": NotifyReminderFinalPreset.self,
    "NotifyReminderNudge": NotifyReminderNudgePreset.self,
    "NotifySocialMention": NotifySocialMentionPreset.self,
    "NotifySocialMessage": NotifySocialMessagePreset.self,
    "NotifyTimerDone": NotifyTimerDonePreset.self,
    "PassingCar": PassingCarPreset.self,
    "Patter": PatterPreset.self,
    "Peal": PealPreset.self,
    "Peck": PeckPreset.self,
    "Pendulum": PendulumPreset.self,
    "Ping": PingPreset.self,
    "Piston": PistonPreset.self,
    "Plunk": PlunkPreset.self,
    "PowerDown": PowerDownPreset.self,
    "Propel": PropelPreset.self,
    "Push": PushPreset.self,
    "Rain": RainPreset.self,
    "Ratchet": RatchetPreset.self,
    "ReadySteadyGo": ReadySteadyGoPreset.self,
    "Rebound": ReboundPreset.self,
    "ReliefSigh": ReliefSighPreset.self,
    "Ripple": RipplePreset.self,
    "Rivet": RivetPreset.self,
    "Rustle": RustlePreset.self,
    "Searching": SearchingPreset.self,
    "SearchSuccess": SearchSuccessPreset.self,
    "SelectionSnap": SelectionSnapPreset.self,
    "Shockwave": ShockwavePreset.self,
    "Sneezing": SneezingPreset.self,
    "Spark": SparkPreset.self,
    "Stampede": StampedePreset.self,
    "Stomp": StompPreset.self,
    "StoneSkip": StoneSkipPreset.self,
    "Strike": StrikePreset.self,
    "SuccessFlourish": SuccessFlourishPreset.self,
    "SurpriseGasp": SurpriseGaspPreset.self,
    "Sway": SwayPreset.self,
    "Syncopate": SyncopatePreset.self,
    "Tada": TadaPreset.self,
    "Thud": ThudPreset.self,
    "Thump": ThumpPreset.self,
    "Thunder": ThunderPreset.self,
    "ThunderRoll": ThunderRollPreset.self,
    "TickTock": TickTockPreset.self,
    "TidalSurge": TidalSurgePreset.self,
    "TideSwell": TideSwellPreset.self,
    "Tremor": TremorPreset.self,
    "Typewriter": TypewriterPreset.self,
    "Victory": VictoryPreset.self,
    "Vomiting": VomitingPreset.self,
    "Vortex": VortexPreset.self,
    "WarDrum": WarDrumPreset.self,
    "WarningPulse": WarningPulsePreset.self,
    "WarningUrgent": WarningUrgentPreset.self,
    "Waterfall": WaterfallPreset.self,
    "Wisp": WispPreset.self,
    "Wobble": WobblePreset.self,
    "Woodpecker": WoodpeckerPreset.self,
    "ZeldaChest": ZeldaChestPreset.self,
    "Zipper": ZipperPreset.self,
// CODEGEN_END_{mappers}
  ]
  
  public init(haptics: Pulsar) {
    super.init()
    self.haptics = haptics
  }
  
  public func enableCache(state: Bool) {
    self.useCache = state
    if (!state) {
      resetCache()
    }
  }
  
  public func isCacheEnabled() -> Bool {
    return self.useCache
  }
  
  public func resetCache() {
    cache.removeAll()
  }
  
  public func preloadPresetByNames(_ names: Array<String>) {
    for (name) in names {
      preloadPresetByName(name)
    }
  }
  
  public func preloadPresetByName(_ name: String) {
    self.useCache = true
    _ = getCacheablePreset(mapper[name]!)
  }
  
  @objc public func getByName(_ name: String) -> Preset? {
    guard mapper.keys.contains(name) else {
      return nil
    }
    return getCacheablePreset(mapper[name]!)
  }
  
  private func getCacheablePreset(_ type: Preset.Type) -> Preset {
    if (useCache) {
      if let cachedPreset = cache[type.name] {
        return cachedPreset
      } else {
        let preset = type.getInstance(haptics: haptics!)
        cache[type.name] = preset
        return preset
      }
    }
    return type.getInstance(haptics: haptics!)
  }
  
  public func systemImpactLight() {
    getCacheablePreset(SystemImpactLightPreset.self).play()
  }
  
  public func systemImpactMedium() {
    getCacheablePreset(SystemImpactMediumPreset.self).play()
  }
  
  public func systemImpactHeavy() {
    getCacheablePreset(SystemImpactHeavyPreset.self).play()
  }
  
  public func systemImpactSoft() {
    getCacheablePreset(SystemImpactSoftPreset.self).play()
  }
  
  public func systemImpactRigid() {
    getCacheablePreset(SystemImpactRigidPreset.self).play()
  }
  
  public func systemNotificationSuccess() {
    getCacheablePreset(SystemNotificationSuccessPreset.self).play()
  }
  
  public func systemNotificationWarning() {
    getCacheablePreset(SystemNotificationWarningPreset.self).play()
  }
  
  public func systemNotificationError() {
    getCacheablePreset(SystemNotificationErrorPreset.self).play()
  }
  
  public func systemSelection() {
    getCacheablePreset(SystemSelectionPreset.self).play()
  }
  
// CODEGEN_BEGIN_{getters}
  public func afterglow() {
    getCacheablePreset(AfterglowPreset.self).play()
  }

  public func aftershock() {
    getCacheablePreset(AftershockPreset.self).play()
  }

  public func aimingFire() {
    getCacheablePreset(AimingFirePreset.self).play()
  }

  public func aimingLock() {
    getCacheablePreset(AimingLockPreset.self).play()
  }

  public func alarm() {
    getCacheablePreset(AlarmPreset.self).play()
  }

  public func angerFrustration() {
    getCacheablePreset(AngerFrustrationPreset.self).play()
  }

  public func anvil() {
    getCacheablePreset(AnvilPreset.self).play()
  }

  public func applause() {
    getCacheablePreset(ApplausePreset.self).play()
  }

  public func attention() {
    getCacheablePreset(AttentionPreset.self).play()
  }

  public func balloonPop() {
    getCacheablePreset(BalloonPopPreset.self).play()
  }

  public func bangDoor() {
    getCacheablePreset(BangDoorPreset.self).play()
  }

  public func barrage() {
    getCacheablePreset(BarragePreset.self).play()
  }

  public func bassDrop() {
    getCacheablePreset(BassDropPreset.self).play()
  }

  public func bellToll() {
    getCacheablePreset(BellTollPreset.self).play()
  }

  public func blip() {
    getCacheablePreset(BlipPreset.self).play()
  }

  public func bloom() {
    getCacheablePreset(BloomPreset.self).play()
  }

  public func bongo() {
    getCacheablePreset(BongoPreset.self).play()
  }

  public func boulder() {
    getCacheablePreset(BoulderPreset.self).play()
  }

  public func breakingWave() {
    getCacheablePreset(BreakingWavePreset.self).play()
  }

  public func breath() {
    getCacheablePreset(BreathPreset.self).play()
  }

  public func buildup() {
    getCacheablePreset(BuildupPreset.self).play()
  }

  public func cadence() {
    getCacheablePreset(CadencePreset.self).play()
  }

  public func cameraShutter() {
    getCacheablePreset(CameraShutterPreset.self).play()
  }

  public func canter() {
    getCacheablePreset(CanterPreset.self).play()
  }

  public func cascade() {
    getCacheablePreset(CascadePreset.self).play()
  }

  public func castanets() {
    getCacheablePreset(CastanetsPreset.self).play()
  }

  public func catPaw() {
    getCacheablePreset(CatPawPreset.self).play()
  }

  public func chip() {
    getCacheablePreset(ChipPreset.self).play()
  }

  public func chirp() {
    getCacheablePreset(ChirpPreset.self).play()
  }

  public func cleave() {
    getCacheablePreset(CleavePreset.self).play()
  }

  public func coinDrop() {
    getCacheablePreset(CoinDropPreset.self).play()
  }

  public func combinationLock() {
    getCacheablePreset(CombinationLockPreset.self).play()
  }

  public func confirm() {
    getCacheablePreset(ConfirmPreset.self).play()
  }

  public func cowboy() {
    getCacheablePreset(CowboyPreset.self).play()
  }

  public func crescendo() {
    getCacheablePreset(CrescendoPreset.self).play()
  }

  public func crossedEyes() {
    getCacheablePreset(CrossedEyesPreset.self).play()
  }

  public func cursing() {
    getCacheablePreset(CursingPreset.self).play()
  }

  public func dewdrop() {
    getCacheablePreset(DewdropPreset.self).play()
  }

  public func dirge() {
    getCacheablePreset(DirgePreset.self).play()
  }

  public func dissolve() {
    getCacheablePreset(DissolvePreset.self).play()
  }

  public func dogBark() {
    getCacheablePreset(DogBarkPreset.self).play()
  }

  public func drone() {
    getCacheablePreset(DronePreset.self).play()
  }

  public func engineRev() {
    getCacheablePreset(EngineRevPreset.self).play()
  }

  public func errorBuzz() {
    getCacheablePreset(ErrorBuzzPreset.self).play()
  }

  public func explodingHead() {
    getCacheablePreset(ExplodingHeadPreset.self).play()
  }

  public func explosion() {
    getCacheablePreset(ExplosionPreset.self).play()
  }

  public func eyeRolling() {
    getCacheablePreset(EyeRollingPreset.self).play()
  }

  public func fadeOut() {
    getCacheablePreset(FadeOutPreset.self).play()
  }

  public func fanfare() {
    getCacheablePreset(FanfarePreset.self).play()
  }

  public func feather() {
    getCacheablePreset(FeatherPreset.self).play()
  }

  public func fingerDrum() {
    getCacheablePreset(FingerDrumPreset.self).play()
  }

  public func firecracker() {
    getCacheablePreset(FirecrackerPreset.self).play()
  }

  public func fizz() {
    getCacheablePreset(FizzPreset.self).play()
  }

  public func flick() {
    getCacheablePreset(FlickPreset.self).play()
  }

  public func gallop() {
    getCacheablePreset(GallopPreset.self).play()
  }

  public func gameCombo() {
    getCacheablePreset(GameComboPreset.self).play()
  }

  public func gameHit() {
    getCacheablePreset(GameHitPreset.self).play()
  }

  public func gameLevelUp() {
    getCacheablePreset(GameLevelUpPreset.self).play()
  }

  public func gamePickup() {
    getCacheablePreset(GamePickupPreset.self).play()
  }

  public func gavel() {
    getCacheablePreset(GavelPreset.self).play()
  }

  public func glitch() {
    getCacheablePreset(GlitchPreset.self).play()
  }

  public func gravityFreefall() {
    getCacheablePreset(GravityFreefallPreset.self).play()
  }

  public func grinningSquinting() {
    getCacheablePreset(GrinningSquintingPreset.self).play()
  }

  public func guitarStrum() {
    getCacheablePreset(GuitarStrumPreset.self).play()
  }

  public func hail() {
    getCacheablePreset(HailPreset.self).play()
  }

  public func heartbeat() {
    getCacheablePreset(HeartbeatPreset.self).play()
  }

  public func herald() {
    getCacheablePreset(HeraldPreset.self).play()
  }

  public func hoofBeat() {
    getCacheablePreset(HoofBeatPreset.self).play()
  }

  public func ignition() {
    getCacheablePreset(IgnitionPreset.self).play()
  }

  public func jolt() {
    getCacheablePreset(JoltPreset.self).play()
  }

  public func keyboardMechanical() {
    getCacheablePreset(KeyboardMechanicalPreset.self).play()
  }

  public func keyboardMembrane() {
    getCacheablePreset(KeyboardMembranePreset.self).play()
  }

  public func knockDoor() {
    getCacheablePreset(KnockDoorPreset.self).play()
  }

  public func latch() {
    getCacheablePreset(LatchPreset.self).play()
  }

  public func levelUp() {
    getCacheablePreset(LevelUpPreset.self).play()
  }

  public func lighthouse() {
    getCacheablePreset(LighthousePreset.self).play()
  }

  public func loaderBreathing() {
    getCacheablePreset(LoaderBreathingPreset.self).play()
  }

  public func loaderPulse() {
    getCacheablePreset(LoaderPulsePreset.self).play()
  }

  public func loaderRadar() {
    getCacheablePreset(LoaderRadarPreset.self).play()
  }

  public func loaderSpin() {
    getCacheablePreset(LoaderSpinPreset.self).play()
  }

  public func loaderWave() {
    getCacheablePreset(LoaderWavePreset.self).play()
  }

  public func lock() {
    getCacheablePreset(LockPreset.self).play()
  }

  public func longPress() {
    getCacheablePreset(LongPressPreset.self).play()
  }

  public func march() {
    getCacheablePreset(MarchPreset.self).play()
  }

  public func marioGameOver() {
    getCacheablePreset(MarioGameOverPreset.self).play()
  }

  public func metronome() {
    getCacheablePreset(MetronomePreset.self).play()
  }

  public func murmur() {
    getCacheablePreset(MurmurPreset.self).play()
  }

  public func newMessage() {
    getCacheablePreset(NewMessagePreset.self).play()
  }

  public func notification() {
    getCacheablePreset(NotificationPreset.self).play()
  }

  public func notificationKnock() {
    getCacheablePreset(NotificationKnockPreset.self).play()
  }

  public func notificationUrgent() {
    getCacheablePreset(NotificationUrgentPreset.self).play()
  }

  public func notifyInfoStandard() {
    getCacheablePreset(NotifyInfoStandardPreset.self).play()
  }

  public func notifyReminderFinal() {
    getCacheablePreset(NotifyReminderFinalPreset.self).play()
  }

  public func notifyReminderNudge() {
    getCacheablePreset(NotifyReminderNudgePreset.self).play()
  }

  public func notifySocialMention() {
    getCacheablePreset(NotifySocialMentionPreset.self).play()
  }

  public func notifySocialMessage() {
    getCacheablePreset(NotifySocialMessagePreset.self).play()
  }

  public func notifyTimerDone() {
    getCacheablePreset(NotifyTimerDonePreset.self).play()
  }

  public func passingCar() {
    getCacheablePreset(PassingCarPreset.self).play()
  }

  public func patter() {
    getCacheablePreset(PatterPreset.self).play()
  }

  public func peal() {
    getCacheablePreset(PealPreset.self).play()
  }

  public func peck() {
    getCacheablePreset(PeckPreset.self).play()
  }

  public func pendulum() {
    getCacheablePreset(PendulumPreset.self).play()
  }

  public func ping() {
    getCacheablePreset(PingPreset.self).play()
  }

  public func piston() {
    getCacheablePreset(PistonPreset.self).play()
  }

  public func plunk() {
    getCacheablePreset(PlunkPreset.self).play()
  }

  public func powerDown() {
    getCacheablePreset(PowerDownPreset.self).play()
  }

  public func propel() {
    getCacheablePreset(PropelPreset.self).play()
  }

  public func push() {
    getCacheablePreset(PushPreset.self).play()
  }

  public func rain() {
    getCacheablePreset(RainPreset.self).play()
  }

  public func ratchet() {
    getCacheablePreset(RatchetPreset.self).play()
  }

  public func readySteadyGo() {
    getCacheablePreset(ReadySteadyGoPreset.self).play()
  }

  public func rebound() {
    getCacheablePreset(ReboundPreset.self).play()
  }

  public func reliefSigh() {
    getCacheablePreset(ReliefSighPreset.self).play()
  }

  public func ripple() {
    getCacheablePreset(RipplePreset.self).play()
  }

  public func rivet() {
    getCacheablePreset(RivetPreset.self).play()
  }

  public func rustle() {
    getCacheablePreset(RustlePreset.self).play()
  }

  public func searching() {
    getCacheablePreset(SearchingPreset.self).play()
  }

  public func searchSuccess() {
    getCacheablePreset(SearchSuccessPreset.self).play()
  }

  public func selectionSnap() {
    getCacheablePreset(SelectionSnapPreset.self).play()
  }

  public func shockwave() {
    getCacheablePreset(ShockwavePreset.self).play()
  }

  public func sneezing() {
    getCacheablePreset(SneezingPreset.self).play()
  }

  public func spark() {
    getCacheablePreset(SparkPreset.self).play()
  }

  public func stampede() {
    getCacheablePreset(StampedePreset.self).play()
  }

  public func stomp() {
    getCacheablePreset(StompPreset.self).play()
  }

  public func stoneSkip() {
    getCacheablePreset(StoneSkipPreset.self).play()
  }

  public func strike() {
    getCacheablePreset(StrikePreset.self).play()
  }

  public func successFlourish() {
    getCacheablePreset(SuccessFlourishPreset.self).play()
  }

  public func surpriseGasp() {
    getCacheablePreset(SurpriseGaspPreset.self).play()
  }

  public func sway() {
    getCacheablePreset(SwayPreset.self).play()
  }

  public func syncopate() {
    getCacheablePreset(SyncopatePreset.self).play()
  }

  public func tada() {
    getCacheablePreset(TadaPreset.self).play()
  }

  public func thud() {
    getCacheablePreset(ThudPreset.self).play()
  }

  public func thump() {
    getCacheablePreset(ThumpPreset.self).play()
  }

  public func thunder() {
    getCacheablePreset(ThunderPreset.self).play()
  }

  public func thunderRoll() {
    getCacheablePreset(ThunderRollPreset.self).play()
  }

  public func tickTock() {
    getCacheablePreset(TickTockPreset.self).play()
  }

  public func tidalSurge() {
    getCacheablePreset(TidalSurgePreset.self).play()
  }

  public func tideSwell() {
    getCacheablePreset(TideSwellPreset.self).play()
  }

  public func tremor() {
    getCacheablePreset(TremorPreset.self).play()
  }

  public func typewriter() {
    getCacheablePreset(TypewriterPreset.self).play()
  }

  public func victory() {
    getCacheablePreset(VictoryPreset.self).play()
  }

  public func vomiting() {
    getCacheablePreset(VomitingPreset.self).play()
  }

  public func vortex() {
    getCacheablePreset(VortexPreset.self).play()
  }

  public func warDrum() {
    getCacheablePreset(WarDrumPreset.self).play()
  }

  public func warningPulse() {
    getCacheablePreset(WarningPulsePreset.self).play()
  }

  public func warningUrgent() {
    getCacheablePreset(WarningUrgentPreset.self).play()
  }

  public func waterfall() {
    getCacheablePreset(WaterfallPreset.self).play()
  }

  public func wisp() {
    getCacheablePreset(WispPreset.self).play()
  }

  public func wobble() {
    getCacheablePreset(WobblePreset.self).play()
  }

  public func woodpecker() {
    getCacheablePreset(WoodpeckerPreset.self).play()
  }

  public func zeldaChest() {
    getCacheablePreset(ZeldaChestPreset.self).play()
  }

  public func zipper() {
    getCacheablePreset(ZipperPreset.self).play()
  }
// CODEGEN_END_{getters}
}
