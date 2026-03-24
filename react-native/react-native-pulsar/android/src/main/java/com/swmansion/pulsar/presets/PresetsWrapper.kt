package com.swmansion.pulsar.presets

import android.content.Context
import com.swmansion.pulsar.Pulsar
import com.swmansion.pulsar.haptics.HapticEngineWrapper
import com.swmansion.pulsar.types.Preset
import com.swmansion.pulsar.presets.generated.*

class PresetsWrapper(
    private val haptics: Pulsar,
    context: Context,
    engine: HapticEngineWrapper,
) {
    private var useCache: Boolean = true
    private val cache = mutableMapOf<String, Preset>()
    private val systemPrimitivePresets = SystemPrimitivePresets(engine)
    private val systemViewBasedPresets = SystemViewBasedPresets(context)

    private val mapper: Map<String, (Pulsar) -> Preset> = mapOf(
        SystemImpactLightPreset.name to { haptics -> SystemImpactLightPreset(haptics) },
        SystemImpactMediumPreset.name to { haptics -> SystemImpactMediumPreset(haptics) },
        SystemImpactHeavyPreset.name to { haptics -> SystemImpactHeavyPreset(haptics) },
        SystemImpactSoftPreset.name to { haptics -> SystemImpactSoftPreset(haptics) },
        SystemImpactRigidPreset.name to { haptics -> SystemImpactRigidPreset(haptics) },
        SystemNotificationSuccessPreset.name to { haptics -> SystemNotificationSuccessPreset(haptics) },
        SystemNotificationWarningPreset.name to { haptics -> SystemNotificationWarningPreset(haptics) },
        SystemNotificationErrorPreset.name to { haptics -> SystemNotificationErrorPreset(haptics) },

        SystemEffectClickPreset.name to { haptics -> SystemEffectClickPreset(haptics, systemPrimitivePresets) },
        SystemDoubleClickPreset.name to { haptics -> SystemDoubleClickPreset(haptics, systemPrimitivePresets) },
        SystemTickPreset.name to { haptics -> SystemTickPreset(haptics, systemPrimitivePresets) },
        SystemHeavyClickPreset.name to { haptics -> SystemHeavyClickPreset(haptics, systemPrimitivePresets) },

        SystemLongPressPreset.name to { haptics -> SystemLongPressPreset(haptics, systemViewBasedPresets) },
        SystemVirtualKeyPreset.name to { haptics -> SystemVirtualKeyPreset(haptics, systemViewBasedPresets) },
        SystemKeyboardTapPreset.name to { haptics -> SystemKeyboardTapPreset(haptics, systemViewBasedPresets) },
        SystemClockTickPreset.name to { haptics -> SystemClockTickPreset(haptics, systemViewBasedPresets) },
        SystemCalendarDatePreset.name to { haptics -> SystemCalendarDatePreset(haptics, systemViewBasedPresets) },
        SystemContextClickPreset.name to { haptics -> SystemContextClickPreset(haptics, systemViewBasedPresets) },
        SystemKeyboardPressPreset.name to { haptics -> SystemKeyboardPressPreset(haptics, systemViewBasedPresets) },
        SystemKeyboardReleasePreset.name to { haptics -> SystemKeyboardReleasePreset(haptics, systemViewBasedPresets) },
        SystemVirtualKeyReleasePreset.name to { haptics -> SystemVirtualKeyReleasePreset(haptics, systemViewBasedPresets) },
        SystemTextHandleMovePreset.name to { haptics -> SystemTextHandleMovePreset(haptics, systemViewBasedPresets) },
        SystemDragCrossingPreset.name to { haptics -> SystemDragCrossingPreset(haptics, systemViewBasedPresets) },
        SystemGestureStartPreset.name to { haptics -> SystemGestureStartPreset(haptics, systemViewBasedPresets) },
        SystemGestureEndPreset.name to { haptics -> SystemGestureEndPreset(haptics, systemViewBasedPresets) },
        SystemEdgeSqueezePreset.name to { haptics -> SystemEdgeSqueezePreset(haptics, systemViewBasedPresets) },
        SystemEdgeReleasePreset.name to { haptics -> SystemEdgeReleasePreset(haptics, systemViewBasedPresets) },
        SystemConfirmPreset.name to { haptics -> SystemConfirmPreset(haptics, systemViewBasedPresets) },
        SystemReleasePreset.name to { haptics -> SystemReleasePreset(haptics, systemViewBasedPresets) },
        SystemScrollTickPreset.name to { haptics -> SystemScrollTickPreset(haptics, systemViewBasedPresets) },
        SystemScrollItemFocusPreset.name to { haptics -> SystemScrollItemFocusPreset(haptics, systemViewBasedPresets) },
        SystemScrollLimitPreset.name to { haptics -> SystemScrollLimitPreset(haptics, systemViewBasedPresets) },
        SystemToggleOnPreset.name to { haptics -> SystemToggleOnPreset(haptics, systemViewBasedPresets) },
        SystemToggleOffPreset.name to { haptics -> SystemToggleOffPreset(haptics, systemViewBasedPresets) },
        SystemDragStartPreset.name to { haptics -> SystemDragStartPreset(haptics, systemViewBasedPresets) },
        SystemSegmentTickPreset.name to { haptics -> SystemSegmentTickPreset(haptics, systemViewBasedPresets) },
        SystemSegmentFrequentTickPreset.name to { haptics -> SystemSegmentFrequentTickPreset(haptics, systemViewBasedPresets) },
// CODEGEN_BEGIN_{mappers}
        AfterglowPreset.name to { haptics -> AfterglowPreset(haptics) },
        AftershockPreset.name to { haptics -> AftershockPreset(haptics) },
        AimingFirePreset.name to { haptics -> AimingFirePreset(haptics) },
        AimingLockPreset.name to { haptics -> AimingLockPreset(haptics) },
        AlarmPreset.name to { haptics -> AlarmPreset(haptics) },
        AngerFrustrationPreset.name to { haptics -> AngerFrustrationPreset(haptics) },
        AnvilPreset.name to { haptics -> AnvilPreset(haptics) },
        ApplausePreset.name to { haptics -> ApplausePreset(haptics) },
        AttentionPreset.name to { haptics -> AttentionPreset(haptics) },
        BalloonPopPreset.name to { haptics -> BalloonPopPreset(haptics) },
        BangDoorPreset.name to { haptics -> BangDoorPreset(haptics) },
        BarragePreset.name to { haptics -> BarragePreset(haptics) },
        BassDropPreset.name to { haptics -> BassDropPreset(haptics) },
        BellTollPreset.name to { haptics -> BellTollPreset(haptics) },
        BlipPreset.name to { haptics -> BlipPreset(haptics) },
        BloomPreset.name to { haptics -> BloomPreset(haptics) },
        BongoPreset.name to { haptics -> BongoPreset(haptics) },
        BoulderPreset.name to { haptics -> BoulderPreset(haptics) },
        BreakingWavePreset.name to { haptics -> BreakingWavePreset(haptics) },
        BreathPreset.name to { haptics -> BreathPreset(haptics) },
        BuildupPreset.name to { haptics -> BuildupPreset(haptics) },
        CadencePreset.name to { haptics -> CadencePreset(haptics) },
        CameraShutterPreset.name to { haptics -> CameraShutterPreset(haptics) },
        CanterPreset.name to { haptics -> CanterPreset(haptics) },
        CascadePreset.name to { haptics -> CascadePreset(haptics) },
        CastanetsPreset.name to { haptics -> CastanetsPreset(haptics) },
        CatPawPreset.name to { haptics -> CatPawPreset(haptics) },
        ChipPreset.name to { haptics -> ChipPreset(haptics) },
        ChirpPreset.name to { haptics -> ChirpPreset(haptics) },
        CleavePreset.name to { haptics -> CleavePreset(haptics) },
        ClickPreset.name to { haptics -> ClickPreset(haptics) },
        CoinDropPreset.name to { haptics -> CoinDropPreset(haptics) },
        CombinationLockPreset.name to { haptics -> CombinationLockPreset(haptics) },
        ConfirmPreset.name to { haptics -> ConfirmPreset(haptics) },
        CowboyPreset.name to { haptics -> CowboyPreset(haptics) },
        CrescendoPreset.name to { haptics -> CrescendoPreset(haptics) },
        CrossedEyesPreset.name to { haptics -> CrossedEyesPreset(haptics) },
        CursingPreset.name to { haptics -> CursingPreset(haptics) },
        DewdropPreset.name to { haptics -> DewdropPreset(haptics) },
        DirgePreset.name to { haptics -> DirgePreset(haptics) },
        DissolvePreset.name to { haptics -> DissolvePreset(haptics) },
        DogBarkPreset.name to { haptics -> DogBarkPreset(haptics) },
        DronePreset.name to { haptics -> DronePreset(haptics) },
        EngineRevPreset.name to { haptics -> EngineRevPreset(haptics) },
        ErrorBuzzPreset.name to { haptics -> ErrorBuzzPreset(haptics) },
        ExplodingHeadPreset.name to { haptics -> ExplodingHeadPreset(haptics) },
        ExplosionPreset.name to { haptics -> ExplosionPreset(haptics) },
        EyeRollingPreset.name to { haptics -> EyeRollingPreset(haptics) },
        FadeOutPreset.name to { haptics -> FadeOutPreset(haptics) },
        FanfarePreset.name to { haptics -> FanfarePreset(haptics) },
        FeatherPreset.name to { haptics -> FeatherPreset(haptics) },
        FingerDrumPreset.name to { haptics -> FingerDrumPreset(haptics) },
        FirecrackerPreset.name to { haptics -> FirecrackerPreset(haptics) },
        FizzPreset.name to { haptics -> FizzPreset(haptics) },
        FlickPreset.name to { haptics -> FlickPreset(haptics) },
        GallopPreset.name to { haptics -> GallopPreset(haptics) },
        GameComboPreset.name to { haptics -> GameComboPreset(haptics) },
        GameHitPreset.name to { haptics -> GameHitPreset(haptics) },
        GameLevelUpPreset.name to { haptics -> GameLevelUpPreset(haptics) },
        GamePickupPreset.name to { haptics -> GamePickupPreset(haptics) },
        GavelPreset.name to { haptics -> GavelPreset(haptics) },
        GlitchPreset.name to { haptics -> GlitchPreset(haptics) },
        GravityFreefallPreset.name to { haptics -> GravityFreefallPreset(haptics) },
        GrinningSquintingPreset.name to { haptics -> GrinningSquintingPreset(haptics) },
        GuitarStrumPreset.name to { haptics -> GuitarStrumPreset(haptics) },
        HailPreset.name to { haptics -> HailPreset(haptics) },
        HeartbeatPreset.name to { haptics -> HeartbeatPreset(haptics) },
        HeraldPreset.name to { haptics -> HeraldPreset(haptics) },
        HoofBeatPreset.name to { haptics -> HoofBeatPreset(haptics) },
        IgnitionPreset.name to { haptics -> IgnitionPreset(haptics) },
        JoltPreset.name to { haptics -> JoltPreset(haptics) },
        KeyboardMechanicalPreset.name to { haptics -> KeyboardMechanicalPreset(haptics) },
        KeyboardMembranePreset.name to { haptics -> KeyboardMembranePreset(haptics) },
        KnockDoorPreset.name to { haptics -> KnockDoorPreset(haptics) },
        LatchPreset.name to { haptics -> LatchPreset(haptics) },
        LevelUpPreset.name to { haptics -> LevelUpPreset(haptics) },
        LighthousePreset.name to { haptics -> LighthousePreset(haptics) },
        LoaderBreathingPreset.name to { haptics -> LoaderBreathingPreset(haptics) },
        LoaderPulsePreset.name to { haptics -> LoaderPulsePreset(haptics) },
        LoaderRadarPreset.name to { haptics -> LoaderRadarPreset(haptics) },
        LoaderSpinPreset.name to { haptics -> LoaderSpinPreset(haptics) },
        LoaderWavePreset.name to { haptics -> LoaderWavePreset(haptics) },
        LockPreset.name to { haptics -> LockPreset(haptics) },
        LongPressPreset.name to { haptics -> LongPressPreset(haptics) },
        MarchPreset.name to { haptics -> MarchPreset(haptics) },
        MarioGameOverPreset.name to { haptics -> MarioGameOverPreset(haptics) },
        MetronomePreset.name to { haptics -> MetronomePreset(haptics) },
        MurmurPreset.name to { haptics -> MurmurPreset(haptics) },
        NewMessagePreset.name to { haptics -> NewMessagePreset(haptics) },
        NotificationPreset.name to { haptics -> NotificationPreset(haptics) },
        NotificationKnockPreset.name to { haptics -> NotificationKnockPreset(haptics) },
        NotificationUrgentPreset.name to { haptics -> NotificationUrgentPreset(haptics) },
        NotifyInfoStandardPreset.name to { haptics -> NotifyInfoStandardPreset(haptics) },
        NotifyReminderFinalPreset.name to { haptics -> NotifyReminderFinalPreset(haptics) },
        NotifyReminderNudgePreset.name to { haptics -> NotifyReminderNudgePreset(haptics) },
        NotifySocialMentionPreset.name to { haptics -> NotifySocialMentionPreset(haptics) },
        NotifySocialMessagePreset.name to { haptics -> NotifySocialMessagePreset(haptics) },
        NotifyTimerDonePreset.name to { haptics -> NotifyTimerDonePreset(haptics) },
        PassingCarPreset.name to { haptics -> PassingCarPreset(haptics) },
        PatterPreset.name to { haptics -> PatterPreset(haptics) },
        PealPreset.name to { haptics -> PealPreset(haptics) },
        PeckPreset.name to { haptics -> PeckPreset(haptics) },
        PendulumPreset.name to { haptics -> PendulumPreset(haptics) },
        PingPreset.name to { haptics -> PingPreset(haptics) },
        PistonPreset.name to { haptics -> PistonPreset(haptics) },
        PlunkPreset.name to { haptics -> PlunkPreset(haptics) },
        PowerDownPreset.name to { haptics -> PowerDownPreset(haptics) },
        PropelPreset.name to { haptics -> PropelPreset(haptics) },
        RainPreset.name to { haptics -> RainPreset(haptics) },
        RatchetPreset.name to { haptics -> RatchetPreset(haptics) },
        ReadySteadyGoPreset.name to { haptics -> ReadySteadyGoPreset(haptics) },
        ReboundPreset.name to { haptics -> ReboundPreset(haptics) },
        ReliefSighPreset.name to { haptics -> ReliefSighPreset(haptics) },
        RipplePreset.name to { haptics -> RipplePreset(haptics) },
        RivetPreset.name to { haptics -> RivetPreset(haptics) },
        RustlePreset.name to { haptics -> RustlePreset(haptics) },
        SearchingPreset.name to { haptics -> SearchingPreset(haptics) },
        SearchSuccessPreset.name to { haptics -> SearchSuccessPreset(haptics) },
        SelectionSnapPreset.name to { haptics -> SelectionSnapPreset(haptics) },
        ShockwavePreset.name to { haptics -> ShockwavePreset(haptics) },
        SneezingPreset.name to { haptics -> SneezingPreset(haptics) },
        SparkPreset.name to { haptics -> SparkPreset(haptics) },
        StampedePreset.name to { haptics -> StampedePreset(haptics) },
        StompPreset.name to { haptics -> StompPreset(haptics) },
        StoneSkipPreset.name to { haptics -> StoneSkipPreset(haptics) },
        StrikePreset.name to { haptics -> StrikePreset(haptics) },
        SuccessFlourishPreset.name to { haptics -> SuccessFlourishPreset(haptics) },
        SurpriseGaspPreset.name to { haptics -> SurpriseGaspPreset(haptics) },
        SwayPreset.name to { haptics -> SwayPreset(haptics) },
        SyncopatePreset.name to { haptics -> SyncopatePreset(haptics) },
        TadaPreset.name to { haptics -> TadaPreset(haptics) },
        ThudPreset.name to { haptics -> ThudPreset(haptics) },
        ThumpPreset.name to { haptics -> ThumpPreset(haptics) },
        ThunderPreset.name to { haptics -> ThunderPreset(haptics) },
        ThunderRollPreset.name to { haptics -> ThunderRollPreset(haptics) },
        TickTockPreset.name to { haptics -> TickTockPreset(haptics) },
        TidalSurgePreset.name to { haptics -> TidalSurgePreset(haptics) },
        TideSwellPreset.name to { haptics -> TideSwellPreset(haptics) },
        TremorPreset.name to { haptics -> TremorPreset(haptics) },
        TypewriterPreset.name to { haptics -> TypewriterPreset(haptics) },
        VictoryPreset.name to { haptics -> VictoryPreset(haptics) },
        VomitingPreset.name to { haptics -> VomitingPreset(haptics) },
        VortexPreset.name to { haptics -> VortexPreset(haptics) },
        WarDrumPreset.name to { haptics -> WarDrumPreset(haptics) },
        WarningPulsePreset.name to { haptics -> WarningPulsePreset(haptics) },
        WarningUrgentPreset.name to { haptics -> WarningUrgentPreset(haptics) },
        WaterfallPreset.name to { haptics -> WaterfallPreset(haptics) },
        WispPreset.name to { haptics -> WispPreset(haptics) },
        WobblePreset.name to { haptics -> WobblePreset(haptics) },
        WoodpeckerPreset.name to { haptics -> WoodpeckerPreset(haptics) },
        ZeldaChestPreset.name to { haptics -> ZeldaChestPreset(haptics) },
        ZipperPreset.name to { haptics -> ZipperPreset(haptics) },
// CODEGEN_END_{mappers}
    )

    fun enableCache(state: Boolean) {
        this.useCache = state
        if (!state) {
            resetCache()
        }
    }

    fun isCacheEnabled(): Boolean = this.useCache

    fun resetCache() {
        cache.clear()
    }

    fun preloadPresetByNames(names: List<String>) {
        for (name in names) {
            preloadPresetByName(name)
        }
    }

    fun preloadPresetByName(name: String) {
        this.useCache = true
        getCacheablePreset(name)
    }

    fun getByName(name: String): Preset? {
        return getCacheablePreset(name)
    }

    private fun getCacheablePreset(name: String): Preset? {
        return if (useCache) {
            cache.getOrPut(name) {
                mapper[name]?.invoke(haptics) ?: return null
            }
        } else {
            mapper[name]?.invoke(haptics) ?: return null
        }
    }

// CODEGEN_BEGIN_{getters}
    fun afterglow() {
        getCacheablePreset(AfterglowPreset.name)!!.play()
    }

    fun aftershock() {
        getCacheablePreset(AftershockPreset.name)!!.play()
    }

    fun aimingFire() {
        getCacheablePreset(AimingFirePreset.name)!!.play()
    }

    fun aimingLock() {
        getCacheablePreset(AimingLockPreset.name)!!.play()
    }

    fun alarm() {
        getCacheablePreset(AlarmPreset.name)!!.play()
    }

    fun angerFrustration() {
        getCacheablePreset(AngerFrustrationPreset.name)!!.play()
    }

    fun anvil() {
        getCacheablePreset(AnvilPreset.name)!!.play()
    }

    fun applause() {
        getCacheablePreset(ApplausePreset.name)!!.play()
    }

    fun attention() {
        getCacheablePreset(AttentionPreset.name)!!.play()
    }

    fun balloonPop() {
        getCacheablePreset(BalloonPopPreset.name)!!.play()
    }

    fun bangDoor() {
        getCacheablePreset(BangDoorPreset.name)!!.play()
    }

    fun barrage() {
        getCacheablePreset(BarragePreset.name)!!.play()
    }

    fun bassDrop() {
        getCacheablePreset(BassDropPreset.name)!!.play()
    }

    fun bellToll() {
        getCacheablePreset(BellTollPreset.name)!!.play()
    }

    fun blip() {
        getCacheablePreset(BlipPreset.name)!!.play()
    }

    fun bloom() {
        getCacheablePreset(BloomPreset.name)!!.play()
    }

    fun bongo() {
        getCacheablePreset(BongoPreset.name)!!.play()
    }

    fun boulder() {
        getCacheablePreset(BoulderPreset.name)!!.play()
    }

    fun breakingWave() {
        getCacheablePreset(BreakingWavePreset.name)!!.play()
    }

    fun breath() {
        getCacheablePreset(BreathPreset.name)!!.play()
    }

    fun buildup() {
        getCacheablePreset(BuildupPreset.name)!!.play()
    }

    fun cadence() {
        getCacheablePreset(CadencePreset.name)!!.play()
    }

    fun cameraShutter() {
        getCacheablePreset(CameraShutterPreset.name)!!.play()
    }

    fun canter() {
        getCacheablePreset(CanterPreset.name)!!.play()
    }

    fun cascade() {
        getCacheablePreset(CascadePreset.name)!!.play()
    }

    fun castanets() {
        getCacheablePreset(CastanetsPreset.name)!!.play()
    }

    fun catPaw() {
        getCacheablePreset(CatPawPreset.name)!!.play()
    }

    fun chip() {
        getCacheablePreset(ChipPreset.name)!!.play()
    }

    fun chirp() {
        getCacheablePreset(ChirpPreset.name)!!.play()
    }

    fun cleave() {
        getCacheablePreset(CleavePreset.name)!!.play()
    }

    fun click() {
        getCacheablePreset(ClickPreset.name)!!.play()
    }

    fun coinDrop() {
        getCacheablePreset(CoinDropPreset.name)!!.play()
    }

    fun combinationLock() {
        getCacheablePreset(CombinationLockPreset.name)!!.play()
    }

    fun confirm() {
        getCacheablePreset(ConfirmPreset.name)!!.play()
    }

    fun cowboy() {
        getCacheablePreset(CowboyPreset.name)!!.play()
    }

    fun crescendo() {
        getCacheablePreset(CrescendoPreset.name)!!.play()
    }

    fun crossedEyes() {
        getCacheablePreset(CrossedEyesPreset.name)!!.play()
    }

    fun cursing() {
        getCacheablePreset(CursingPreset.name)!!.play()
    }

    fun dewdrop() {
        getCacheablePreset(DewdropPreset.name)!!.play()
    }

    fun dirge() {
        getCacheablePreset(DirgePreset.name)!!.play()
    }

    fun dissolve() {
        getCacheablePreset(DissolvePreset.name)!!.play()
    }

    fun dogBark() {
        getCacheablePreset(DogBarkPreset.name)!!.play()
    }

    fun drone() {
        getCacheablePreset(DronePreset.name)!!.play()
    }

    fun engineRev() {
        getCacheablePreset(EngineRevPreset.name)!!.play()
    }

    fun errorBuzz() {
        getCacheablePreset(ErrorBuzzPreset.name)!!.play()
    }

    fun explodingHead() {
        getCacheablePreset(ExplodingHeadPreset.name)!!.play()
    }

    fun explosion() {
        getCacheablePreset(ExplosionPreset.name)!!.play()
    }

    fun eyeRolling() {
        getCacheablePreset(EyeRollingPreset.name)!!.play()
    }

    fun fadeOut() {
        getCacheablePreset(FadeOutPreset.name)!!.play()
    }

    fun fanfare() {
        getCacheablePreset(FanfarePreset.name)!!.play()
    }

    fun feather() {
        getCacheablePreset(FeatherPreset.name)!!.play()
    }

    fun fingerDrum() {
        getCacheablePreset(FingerDrumPreset.name)!!.play()
    }

    fun firecracker() {
        getCacheablePreset(FirecrackerPreset.name)!!.play()
    }

    fun fizz() {
        getCacheablePreset(FizzPreset.name)!!.play()
    }

    fun flick() {
        getCacheablePreset(FlickPreset.name)!!.play()
    }

    fun gallop() {
        getCacheablePreset(GallopPreset.name)!!.play()
    }

    fun gameCombo() {
        getCacheablePreset(GameComboPreset.name)!!.play()
    }

    fun gameHit() {
        getCacheablePreset(GameHitPreset.name)!!.play()
    }

    fun gameLevelUp() {
        getCacheablePreset(GameLevelUpPreset.name)!!.play()
    }

    fun gamePickup() {
        getCacheablePreset(GamePickupPreset.name)!!.play()
    }

    fun gavel() {
        getCacheablePreset(GavelPreset.name)!!.play()
    }

    fun glitch() {
        getCacheablePreset(GlitchPreset.name)!!.play()
    }

    fun gravityFreefall() {
        getCacheablePreset(GravityFreefallPreset.name)!!.play()
    }

    fun grinningSquinting() {
        getCacheablePreset(GrinningSquintingPreset.name)!!.play()
    }

    fun guitarStrum() {
        getCacheablePreset(GuitarStrumPreset.name)!!.play()
    }

    fun hail() {
        getCacheablePreset(HailPreset.name)!!.play()
    }

    fun heartbeat() {
        getCacheablePreset(HeartbeatPreset.name)!!.play()
    }

    fun herald() {
        getCacheablePreset(HeraldPreset.name)!!.play()
    }

    fun hoofBeat() {
        getCacheablePreset(HoofBeatPreset.name)!!.play()
    }

    fun ignition() {
        getCacheablePreset(IgnitionPreset.name)!!.play()
    }

    fun jolt() {
        getCacheablePreset(JoltPreset.name)!!.play()
    }

    fun keyboardMechanical() {
        getCacheablePreset(KeyboardMechanicalPreset.name)!!.play()
    }

    fun keyboardMembrane() {
        getCacheablePreset(KeyboardMembranePreset.name)!!.play()
    }

    fun knockDoor() {
        getCacheablePreset(KnockDoorPreset.name)!!.play()
    }

    fun latch() {
        getCacheablePreset(LatchPreset.name)!!.play()
    }

    fun levelUp() {
        getCacheablePreset(LevelUpPreset.name)!!.play()
    }

    fun lighthouse() {
        getCacheablePreset(LighthousePreset.name)!!.play()
    }

    fun loaderBreathing() {
        getCacheablePreset(LoaderBreathingPreset.name)!!.play()
    }

    fun loaderPulse() {
        getCacheablePreset(LoaderPulsePreset.name)!!.play()
    }

    fun loaderRadar() {
        getCacheablePreset(LoaderRadarPreset.name)!!.play()
    }

    fun loaderSpin() {
        getCacheablePreset(LoaderSpinPreset.name)!!.play()
    }

    fun loaderWave() {
        getCacheablePreset(LoaderWavePreset.name)!!.play()
    }

    fun lock() {
        getCacheablePreset(LockPreset.name)!!.play()
    }

    fun longPress() {
        getCacheablePreset(LongPressPreset.name)!!.play()
    }

    fun march() {
        getCacheablePreset(MarchPreset.name)!!.play()
    }

    fun marioGameOver() {
        getCacheablePreset(MarioGameOverPreset.name)!!.play()
    }

    fun metronome() {
        getCacheablePreset(MetronomePreset.name)!!.play()
    }

    fun murmur() {
        getCacheablePreset(MurmurPreset.name)!!.play()
    }

    fun newMessage() {
        getCacheablePreset(NewMessagePreset.name)!!.play()
    }

    fun notification() {
        getCacheablePreset(NotificationPreset.name)!!.play()
    }

    fun notificationKnock() {
        getCacheablePreset(NotificationKnockPreset.name)!!.play()
    }

    fun notificationUrgent() {
        getCacheablePreset(NotificationUrgentPreset.name)!!.play()
    }

    fun notifyInfoStandard() {
        getCacheablePreset(NotifyInfoStandardPreset.name)!!.play()
    }

    fun notifyReminderFinal() {
        getCacheablePreset(NotifyReminderFinalPreset.name)!!.play()
    }

    fun notifyReminderNudge() {
        getCacheablePreset(NotifyReminderNudgePreset.name)!!.play()
    }

    fun notifySocialMention() {
        getCacheablePreset(NotifySocialMentionPreset.name)!!.play()
    }

    fun notifySocialMessage() {
        getCacheablePreset(NotifySocialMessagePreset.name)!!.play()
    }

    fun notifyTimerDone() {
        getCacheablePreset(NotifyTimerDonePreset.name)!!.play()
    }

    fun passingCar() {
        getCacheablePreset(PassingCarPreset.name)!!.play()
    }

    fun patter() {
        getCacheablePreset(PatterPreset.name)!!.play()
    }

    fun peal() {
        getCacheablePreset(PealPreset.name)!!.play()
    }

    fun peck() {
        getCacheablePreset(PeckPreset.name)!!.play()
    }

    fun pendulum() {
        getCacheablePreset(PendulumPreset.name)!!.play()
    }

    fun ping() {
        getCacheablePreset(PingPreset.name)!!.play()
    }

    fun piston() {
        getCacheablePreset(PistonPreset.name)!!.play()
    }

    fun plunk() {
        getCacheablePreset(PlunkPreset.name)!!.play()
    }

    fun powerDown() {
        getCacheablePreset(PowerDownPreset.name)!!.play()
    }

    fun propel() {
        getCacheablePreset(PropelPreset.name)!!.play()
    }

    fun rain() {
        getCacheablePreset(RainPreset.name)!!.play()
    }

    fun ratchet() {
        getCacheablePreset(RatchetPreset.name)!!.play()
    }

    fun readySteadyGo() {
        getCacheablePreset(ReadySteadyGoPreset.name)!!.play()
    }

    fun rebound() {
        getCacheablePreset(ReboundPreset.name)!!.play()
    }

    fun reliefSigh() {
        getCacheablePreset(ReliefSighPreset.name)!!.play()
    }

    fun ripple() {
        getCacheablePreset(RipplePreset.name)!!.play()
    }

    fun rivet() {
        getCacheablePreset(RivetPreset.name)!!.play()
    }

    fun rustle() {
        getCacheablePreset(RustlePreset.name)!!.play()
    }

    fun searching() {
        getCacheablePreset(SearchingPreset.name)!!.play()
    }

    fun searchSuccess() {
        getCacheablePreset(SearchSuccessPreset.name)!!.play()
    }

    fun selectionSnap() {
        getCacheablePreset(SelectionSnapPreset.name)!!.play()
    }

    fun shockwave() {
        getCacheablePreset(ShockwavePreset.name)!!.play()
    }

    fun sneezing() {
        getCacheablePreset(SneezingPreset.name)!!.play()
    }

    fun spark() {
        getCacheablePreset(SparkPreset.name)!!.play()
    }

    fun stampede() {
        getCacheablePreset(StampedePreset.name)!!.play()
    }

    fun stomp() {
        getCacheablePreset(StompPreset.name)!!.play()
    }

    fun stoneSkip() {
        getCacheablePreset(StoneSkipPreset.name)!!.play()
    }

    fun strike() {
        getCacheablePreset(StrikePreset.name)!!.play()
    }

    fun successFlourish() {
        getCacheablePreset(SuccessFlourishPreset.name)!!.play()
    }

    fun surpriseGasp() {
        getCacheablePreset(SurpriseGaspPreset.name)!!.play()
    }

    fun sway() {
        getCacheablePreset(SwayPreset.name)!!.play()
    }

    fun syncopate() {
        getCacheablePreset(SyncopatePreset.name)!!.play()
    }

    fun tada() {
        getCacheablePreset(TadaPreset.name)!!.play()
    }

    fun thud() {
        getCacheablePreset(ThudPreset.name)!!.play()
    }

    fun thump() {
        getCacheablePreset(ThumpPreset.name)!!.play()
    }

    fun thunder() {
        getCacheablePreset(ThunderPreset.name)!!.play()
    }

    fun thunderRoll() {
        getCacheablePreset(ThunderRollPreset.name)!!.play()
    }

    fun tickTock() {
        getCacheablePreset(TickTockPreset.name)!!.play()
    }

    fun tidalSurge() {
        getCacheablePreset(TidalSurgePreset.name)!!.play()
    }

    fun tideSwell() {
        getCacheablePreset(TideSwellPreset.name)!!.play()
    }

    fun tremor() {
        getCacheablePreset(TremorPreset.name)!!.play()
    }

    fun typewriter() {
        getCacheablePreset(TypewriterPreset.name)!!.play()
    }

    fun victory() {
        getCacheablePreset(VictoryPreset.name)!!.play()
    }

    fun vomiting() {
        getCacheablePreset(VomitingPreset.name)!!.play()
    }

    fun vortex() {
        getCacheablePreset(VortexPreset.name)!!.play()
    }

    fun warDrum() {
        getCacheablePreset(WarDrumPreset.name)!!.play()
    }

    fun warningPulse() {
        getCacheablePreset(WarningPulsePreset.name)!!.play()
    }

    fun warningUrgent() {
        getCacheablePreset(WarningUrgentPreset.name)!!.play()
    }

    fun waterfall() {
        getCacheablePreset(WaterfallPreset.name)!!.play()
    }

    fun wisp() {
        getCacheablePreset(WispPreset.name)!!.play()
    }

    fun wobble() {
        getCacheablePreset(WobblePreset.name)!!.play()
    }

    fun woodpecker() {
        getCacheablePreset(WoodpeckerPreset.name)!!.play()
    }

    fun zeldaChest() {
        getCacheablePreset(ZeldaChestPreset.name)!!.play()
    }

    fun zipper() {
        getCacheablePreset(ZipperPreset.name)!!.play()
    }
// CODEGEN_END_{getters}
}
