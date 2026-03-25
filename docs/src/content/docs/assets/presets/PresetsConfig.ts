import type { PresetConfig } from '../../components/Preset/types';

// CODEGEN_BEGIN_{imports}
import AfterglowPreset from './Afterglow.json';
import AfterglowImage from './Afterglow.png';
import AftershockPreset from './Aftershock.json';
import AftershockImage from './Aftershock.png';
import AimingFirePreset from './AimingFire.json';
import AimingFireImage from './AimingFire.png';
import AimingLockPreset from './AimingLock.json';
import AimingLockImage from './AimingLock.png';
import AlarmPreset from './Alarm.json';
import AlarmImage from './Alarm.png';
import AngerFrustrationPreset from './AngerFrustration.json';
import AngerFrustrationImage from './AngerFrustration.png';
import AnvilPreset from './Anvil.json';
import AnvilImage from './Anvil.png';
import ApplausePreset from './Applause.json';
import ApplauseImage from './Applause.png';
import AttentionPreset from './Attention.json';
import AttentionImage from './Attention.png';
import BalloonPopPreset from './BalloonPop.json';
import BalloonPopImage from './BalloonPop.png';
import BangDoorPreset from './BangDoor.json';
import BangDoorImage from './BangDoor.png';
import BarragePreset from './Barrage.json';
import BarrageImage from './Barrage.png';
import BassDropPreset from './BassDrop.json';
import BassDropImage from './BassDrop.png';
import BellTollPreset from './BellToll.json';
import BellTollImage from './BellToll.png';
import BlipPreset from './Blip.json';
import BlipImage from './Blip.png';
import BloomPreset from './Bloom.json';
import BloomImage from './Bloom.png';
import BongoPreset from './Bongo.json';
import BongoImage from './Bongo.png';
import BoulderPreset from './Boulder.json';
import BoulderImage from './Boulder.png';
import BreakingWavePreset from './BreakingWave.json';
import BreakingWaveImage from './BreakingWave.png';
import BreathPreset from './Breath.json';
import BreathImage from './Breath.png';
import BuildupPreset from './Buildup.json';
import BuildupImage from './Buildup.png';
import CadencePreset from './Cadence.json';
import CadenceImage from './Cadence.png';
import CameraShutterPreset from './CameraShutter.json';
import CameraShutterImage from './CameraShutter.png';
import CanterPreset from './Canter.json';
import CanterImage from './Canter.png';
import CascadePreset from './Cascade.json';
import CascadeImage from './Cascade.png';
import CastanetsPreset from './Castanets.json';
import CastanetsImage from './Castanets.png';
import CatPawPreset from './CatPaw.json';
import CatPawImage from './CatPaw.png';
import ChipPreset from './Chip.json';
import ChipImage from './Chip.png';
import ChirpPreset from './Chirp.json';
import ChirpImage from './Chirp.png';
import CleavePreset from './Cleave.json';
import CleaveImage from './Cleave.png';
import CoinDropPreset from './CoinDrop.json';
import CoinDropImage from './CoinDrop.png';
import CombinationLockPreset from './CombinationLock.json';
import CombinationLockImage from './CombinationLock.png';
import ConfirmPreset from './Confirm.json';
import ConfirmImage from './Confirm.png';
import CowboyPreset from './Cowboy.json';
import CowboyImage from './Cowboy.png';
import CrescendoPreset from './Crescendo.json';
import CrescendoImage from './Crescendo.png';
import CrossedEyesPreset from './CrossedEyes.json';
import CrossedEyesImage from './CrossedEyes.png';
import CursingPreset from './Cursing.json';
import CursingImage from './Cursing.png';
import DewdropPreset from './Dewdrop.json';
import DewdropImage from './Dewdrop.png';
import DirgePreset from './Dirge.json';
import DirgeImage from './Dirge.png';
import DissolvePreset from './Dissolve.json';
import DissolveImage from './Dissolve.png';
import DogBarkPreset from './DogBark.json';
import DogBarkImage from './DogBark.png';
import DronePreset from './Drone.json';
import DroneImage from './Drone.png';
import EngineRevPreset from './EngineRev.json';
import EngineRevImage from './EngineRev.png';
import ErrorBuzzPreset from './ErrorBuzz.json';
import ErrorBuzzImage from './ErrorBuzz.png';
import ExplodingHeadPreset from './ExplodingHead.json';
import ExplodingHeadImage from './ExplodingHead.png';
import ExplosionPreset from './Explosion.json';
import ExplosionImage from './Explosion.png';
import EyeRollingPreset from './EyeRolling.json';
import EyeRollingImage from './EyeRolling.png';
import FadeOutPreset from './FadeOut.json';
import FadeOutImage from './FadeOut.png';
import FanfarePreset from './Fanfare.json';
import FanfareImage from './Fanfare.png';
import FeatherPreset from './Feather.json';
import FeatherImage from './Feather.png';
import FingerDrumPreset from './FingerDrum.json';
import FingerDrumImage from './FingerDrum.png';
import FirecrackerPreset from './Firecracker.json';
import FirecrackerImage from './Firecracker.png';
import FizzPreset from './Fizz.json';
import FizzImage from './Fizz.png';
import FlickPreset from './Flick.json';
import FlickImage from './Flick.png';
import GallopPreset from './Gallop.json';
import GallopImage from './Gallop.png';
import GameComboPreset from './GameCombo.json';
import GameComboImage from './GameCombo.png';
import GameHitPreset from './GameHit.json';
import GameHitImage from './GameHit.png';
import GameLevelUpPreset from './GameLevelUp.json';
import GameLevelUpImage from './GameLevelUp.png';
import GamePickupPreset from './GamePickup.json';
import GamePickupImage from './GamePickup.png';
import GavelPreset from './Gavel.json';
import GavelImage from './Gavel.png';
import GlitchPreset from './Glitch.json';
import GlitchImage from './Glitch.png';
import GravityFreefallPreset from './GravityFreefall.json';
import GravityFreefallImage from './GravityFreefall.png';
import GrinningSquintingPreset from './GrinningSquinting.json';
import GrinningSquintingImage from './GrinningSquinting.png';
import GuitarStrumPreset from './GuitarStrum.json';
import GuitarStrumImage from './GuitarStrum.png';
import HailPreset from './Hail.json';
import HailImage from './Hail.png';
import HeartbeatPreset from './Heartbeat.json';
import HeartbeatImage from './Heartbeat.png';
import HeraldPreset from './Herald.json';
import HeraldImage from './Herald.png';
import HoofBeatPreset from './HoofBeat.json';
import HoofBeatImage from './HoofBeat.png';
import IgnitionPreset from './Ignition.json';
import IgnitionImage from './Ignition.png';
import JoltPreset from './Jolt.json';
import JoltImage from './Jolt.png';
import KeyboardMechanicalPreset from './KeyboardMechanical.json';
import KeyboardMechanicalImage from './KeyboardMechanical.png';
import KeyboardMembranePreset from './KeyboardMembrane.json';
import KeyboardMembraneImage from './KeyboardMembrane.png';
import KnockDoorPreset from './KnockDoor.json';
import KnockDoorImage from './KnockDoor.png';
import LatchPreset from './Latch.json';
import LatchImage from './Latch.png';
import LevelUpPreset from './LevelUp.json';
import LevelUpImage from './LevelUp.png';
import LighthousePreset from './Lighthouse.json';
import LighthouseImage from './Lighthouse.png';
import LoaderBreathingPreset from './LoaderBreathing.json';
import LoaderBreathingImage from './LoaderBreathing.png';
import LoaderPulsePreset from './LoaderPulse.json';
import LoaderPulseImage from './LoaderPulse.png';
import LoaderRadarPreset from './LoaderRadar.json';
import LoaderRadarImage from './LoaderRadar.png';
import LoaderSpinPreset from './LoaderSpin.json';
import LoaderSpinImage from './LoaderSpin.png';
import LoaderWavePreset from './LoaderWave.json';
import LoaderWaveImage from './LoaderWave.png';
import LockPreset from './Lock.json';
import LockImage from './Lock.png';
import LongPressPreset from './LongPress.json';
import LongPressImage from './LongPress.png';
import MarchPreset from './March.json';
import MarchImage from './March.png';
import MarioGameOverPreset from './MarioGameOver.json';
import MarioGameOverImage from './MarioGameOver.png';
import MetronomePreset from './Metronome.json';
import MetronomeImage from './Metronome.png';
import MurmurPreset from './Murmur.json';
import MurmurImage from './Murmur.png';
import NewMessagePreset from './NewMessage.json';
import NewMessageImage from './NewMessage.png';
import NotificationPreset from './Notification.json';
import NotificationImage from './Notification.png';
import NotificationKnockPreset from './NotificationKnock.json';
import NotificationKnockImage from './NotificationKnock.png';
import NotificationUrgentPreset from './NotificationUrgent.json';
import NotificationUrgentImage from './NotificationUrgent.png';
import NotifyInfoStandardPreset from './NotifyInfoStandard.json';
import NotifyInfoStandardImage from './NotifyInfoStandard.png';
import NotifyReminderFinalPreset from './NotifyReminderFinal.json';
import NotifyReminderFinalImage from './NotifyReminderFinal.png';
import NotifyReminderNudgePreset from './NotifyReminderNudge.json';
import NotifyReminderNudgeImage from './NotifyReminderNudge.png';
import NotifySocialMentionPreset from './NotifySocialMention.json';
import NotifySocialMentionImage from './NotifySocialMention.png';
import NotifySocialMessagePreset from './NotifySocialMessage.json';
import NotifySocialMessageImage from './NotifySocialMessage.png';
import NotifyTimerDonePreset from './NotifyTimerDone.json';
import NotifyTimerDoneImage from './NotifyTimerDone.png';
import PassingCarPreset from './PassingCar.json';
import PassingCarImage from './PassingCar.png';
import PatterPreset from './Patter.json';
import PatterImage from './Patter.png';
import PealPreset from './Peal.json';
import PealImage from './Peal.png';
import PeckPreset from './Peck.json';
import PeckImage from './Peck.png';
import PendulumPreset from './Pendulum.json';
import PendulumImage from './Pendulum.png';
import PingPreset from './Ping.json';
import PingImage from './Ping.png';
import PistonPreset from './Piston.json';
import PistonImage from './Piston.png';
import PlunkPreset from './Plunk.json';
import PlunkImage from './Plunk.png';
import PowerDownPreset from './PowerDown.json';
import PowerDownImage from './PowerDown.png';
import PropelPreset from './Propel.json';
import PropelImage from './Propel.png';
import PushPreset from './Push.json';
import PushImage from './Push.png';
import RainPreset from './Rain.json';
import RainImage from './Rain.png';
import RatchetPreset from './Ratchet.json';
import RatchetImage from './Ratchet.png';
import ReadySteadyGoPreset from './ReadySteadyGo.json';
import ReadySteadyGoImage from './ReadySteadyGo.png';
import ReboundPreset from './Rebound.json';
import ReboundImage from './Rebound.png';
import ReliefSighPreset from './ReliefSigh.json';
import ReliefSighImage from './ReliefSigh.png';
import RipplePreset from './Ripple.json';
import RippleImage from './Ripple.png';
import RivetPreset from './Rivet.json';
import RivetImage from './Rivet.png';
import RustlePreset from './Rustle.json';
import RustleImage from './Rustle.png';
import SearchingPreset from './Searching.json';
import SearchingImage from './Searching.png';
import SearchSuccessPreset from './SearchSuccess.json';
import SearchSuccessImage from './SearchSuccess.png';
import SelectionSnapPreset from './SelectionSnap.json';
import SelectionSnapImage from './SelectionSnap.png';
import ShockwavePreset from './Shockwave.json';
import ShockwaveImage from './Shockwave.png';
import SneezingPreset from './Sneezing.json';
import SneezingImage from './Sneezing.png';
import SparkPreset from './Spark.json';
import SparkImage from './Spark.png';
import StampedePreset from './Stampede.json';
import StampedeImage from './Stampede.png';
import StompPreset from './Stomp.json';
import StompImage from './Stomp.png';
import StoneSkipPreset from './StoneSkip.json';
import StoneSkipImage from './StoneSkip.png';
import StrikePreset from './Strike.json';
import StrikeImage from './Strike.png';
import SuccessFlourishPreset from './SuccessFlourish.json';
import SuccessFlourishImage from './SuccessFlourish.png';
import SurpriseGaspPreset from './SurpriseGasp.json';
import SurpriseGaspImage from './SurpriseGasp.png';
import SwayPreset from './Sway.json';
import SwayImage from './Sway.png';
import SyncopatePreset from './Syncopate.json';
import SyncopateImage from './Syncopate.png';
import TadaPreset from './Tada.json';
import TadaImage from './Tada.png';
import ThudPreset from './Thud.json';
import ThudImage from './Thud.png';
import ThumpPreset from './Thump.json';
import ThumpImage from './Thump.png';
import ThunderPreset from './Thunder.json';
import ThunderImage from './Thunder.png';
import ThunderRollPreset from './ThunderRoll.json';
import ThunderRollImage from './ThunderRoll.png';
import TickTockPreset from './TickTock.json';
import TickTockImage from './TickTock.png';
import TidalSurgePreset from './TidalSurge.json';
import TidalSurgeImage from './TidalSurge.png';
import TideSwellPreset from './TideSwell.json';
import TideSwellImage from './TideSwell.png';
import TremorPreset from './Tremor.json';
import TremorImage from './Tremor.png';
import TypewriterPreset from './Typewriter.json';
import TypewriterImage from './Typewriter.png';
import VictoryPreset from './Victory.json';
import VictoryImage from './Victory.png';
import VomitingPreset from './Vomiting.json';
import VomitingImage from './Vomiting.png';
import VortexPreset from './Vortex.json';
import VortexImage from './Vortex.png';
import WarDrumPreset from './WarDrum.json';
import WarDrumImage from './WarDrum.png';
import WarningPulsePreset from './WarningPulse.json';
import WarningPulseImage from './WarningPulse.png';
import WarningUrgentPreset from './WarningUrgent.json';
import WarningUrgentImage from './WarningUrgent.png';
import WaterfallPreset from './Waterfall.json';
import WaterfallImage from './Waterfall.png';
import WispPreset from './Wisp.json';
import WispImage from './Wisp.png';
import WobblePreset from './Wobble.json';
import WobbleImage from './Wobble.png';
import WoodpeckerPreset from './Woodpecker.json';
import WoodpeckerImage from './Woodpecker.png';
import ZeldaChestPreset from './ZeldaChest.json';
import ZeldaChestImage from './ZeldaChest.png';
import ZipperPreset from './Zipper.json';
import ZipperImage from './Zipper.png';
// CODEGEN_END_{imports}

export const PresetsConfig: Array<PresetConfig> = [
// CODEGEN_BEGIN_{presets}
  { data: AfterglowPreset, image: AfterglowImage },
  { data: AftershockPreset, image: AftershockImage },
  { data: AimingFirePreset, image: AimingFireImage },
  { data: AimingLockPreset, image: AimingLockImage },
  { data: AlarmPreset, image: AlarmImage },
  { data: AngerFrustrationPreset, image: AngerFrustrationImage },
  { data: AnvilPreset, image: AnvilImage },
  { data: ApplausePreset, image: ApplauseImage },
  { data: AttentionPreset, image: AttentionImage },
  { data: BalloonPopPreset, image: BalloonPopImage },
  { data: BangDoorPreset, image: BangDoorImage },
  { data: BarragePreset, image: BarrageImage },
  { data: BassDropPreset, image: BassDropImage },
  { data: BellTollPreset, image: BellTollImage },
  { data: BlipPreset, image: BlipImage },
  { data: BloomPreset, image: BloomImage },
  { data: BongoPreset, image: BongoImage },
  { data: BoulderPreset, image: BoulderImage },
  { data: BreakingWavePreset, image: BreakingWaveImage },
  { data: BreathPreset, image: BreathImage },
  { data: BuildupPreset, image: BuildupImage },
  { data: CadencePreset, image: CadenceImage },
  { data: CameraShutterPreset, image: CameraShutterImage },
  { data: CanterPreset, image: CanterImage },
  { data: CascadePreset, image: CascadeImage },
  { data: CastanetsPreset, image: CastanetsImage },
  { data: CatPawPreset, image: CatPawImage },
  { data: ChipPreset, image: ChipImage },
  { data: ChirpPreset, image: ChirpImage },
  { data: CleavePreset, image: CleaveImage },
  { data: CoinDropPreset, image: CoinDropImage },
  { data: CombinationLockPreset, image: CombinationLockImage },
  { data: ConfirmPreset, image: ConfirmImage },
  { data: CowboyPreset, image: CowboyImage },
  { data: CrescendoPreset, image: CrescendoImage },
  { data: CrossedEyesPreset, image: CrossedEyesImage },
  { data: CursingPreset, image: CursingImage },
  { data: DewdropPreset, image: DewdropImage },
  { data: DirgePreset, image: DirgeImage },
  { data: DissolvePreset, image: DissolveImage },
  { data: DogBarkPreset, image: DogBarkImage },
  { data: DronePreset, image: DroneImage },
  { data: EngineRevPreset, image: EngineRevImage },
  { data: ErrorBuzzPreset, image: ErrorBuzzImage },
  { data: ExplodingHeadPreset, image: ExplodingHeadImage },
  { data: ExplosionPreset, image: ExplosionImage },
  { data: EyeRollingPreset, image: EyeRollingImage },
  { data: FadeOutPreset, image: FadeOutImage },
  { data: FanfarePreset, image: FanfareImage },
  { data: FeatherPreset, image: FeatherImage },
  { data: FingerDrumPreset, image: FingerDrumImage },
  { data: FirecrackerPreset, image: FirecrackerImage },
  { data: FizzPreset, image: FizzImage },
  { data: FlickPreset, image: FlickImage },
  { data: GallopPreset, image: GallopImage },
  { data: GameComboPreset, image: GameComboImage },
  { data: GameHitPreset, image: GameHitImage },
  { data: GameLevelUpPreset, image: GameLevelUpImage },
  { data: GamePickupPreset, image: GamePickupImage },
  { data: GavelPreset, image: GavelImage },
  { data: GlitchPreset, image: GlitchImage },
  { data: GravityFreefallPreset, image: GravityFreefallImage },
  { data: GrinningSquintingPreset, image: GrinningSquintingImage },
  { data: GuitarStrumPreset, image: GuitarStrumImage },
  { data: HailPreset, image: HailImage },
  { data: HeartbeatPreset, image: HeartbeatImage },
  { data: HeraldPreset, image: HeraldImage },
  { data: HoofBeatPreset, image: HoofBeatImage },
  { data: IgnitionPreset, image: IgnitionImage },
  { data: JoltPreset, image: JoltImage },
  { data: KeyboardMechanicalPreset, image: KeyboardMechanicalImage },
  { data: KeyboardMembranePreset, image: KeyboardMembraneImage },
  { data: KnockDoorPreset, image: KnockDoorImage },
  { data: LatchPreset, image: LatchImage },
  { data: LevelUpPreset, image: LevelUpImage },
  { data: LighthousePreset, image: LighthouseImage },
  { data: LoaderBreathingPreset, image: LoaderBreathingImage },
  { data: LoaderPulsePreset, image: LoaderPulseImage },
  { data: LoaderRadarPreset, image: LoaderRadarImage },
  { data: LoaderSpinPreset, image: LoaderSpinImage },
  { data: LoaderWavePreset, image: LoaderWaveImage },
  { data: LockPreset, image: LockImage },
  { data: LongPressPreset, image: LongPressImage },
  { data: MarchPreset, image: MarchImage },
  { data: MarioGameOverPreset, image: MarioGameOverImage },
  { data: MetronomePreset, image: MetronomeImage },
  { data: MurmurPreset, image: MurmurImage },
  { data: NewMessagePreset, image: NewMessageImage },
  { data: NotificationPreset, image: NotificationImage },
  { data: NotificationKnockPreset, image: NotificationKnockImage },
  { data: NotificationUrgentPreset, image: NotificationUrgentImage },
  { data: NotifyInfoStandardPreset, image: NotifyInfoStandardImage },
  { data: NotifyReminderFinalPreset, image: NotifyReminderFinalImage },
  { data: NotifyReminderNudgePreset, image: NotifyReminderNudgeImage },
  { data: NotifySocialMentionPreset, image: NotifySocialMentionImage },
  { data: NotifySocialMessagePreset, image: NotifySocialMessageImage },
  { data: NotifyTimerDonePreset, image: NotifyTimerDoneImage },
  { data: PassingCarPreset, image: PassingCarImage },
  { data: PatterPreset, image: PatterImage },
  { data: PealPreset, image: PealImage },
  { data: PeckPreset, image: PeckImage },
  { data: PendulumPreset, image: PendulumImage },
  { data: PingPreset, image: PingImage },
  { data: PistonPreset, image: PistonImage },
  { data: PlunkPreset, image: PlunkImage },
  { data: PowerDownPreset, image: PowerDownImage },
  { data: PropelPreset, image: PropelImage },
  { data: PushPreset, image: PushImage },
  { data: RainPreset, image: RainImage },
  { data: RatchetPreset, image: RatchetImage },
  { data: ReadySteadyGoPreset, image: ReadySteadyGoImage },
  { data: ReboundPreset, image: ReboundImage },
  { data: ReliefSighPreset, image: ReliefSighImage },
  { data: RipplePreset, image: RippleImage },
  { data: RivetPreset, image: RivetImage },
  { data: RustlePreset, image: RustleImage },
  { data: SearchingPreset, image: SearchingImage },
  { data: SearchSuccessPreset, image: SearchSuccessImage },
  { data: SelectionSnapPreset, image: SelectionSnapImage },
  { data: ShockwavePreset, image: ShockwaveImage },
  { data: SneezingPreset, image: SneezingImage },
  { data: SparkPreset, image: SparkImage },
  { data: StampedePreset, image: StampedeImage },
  { data: StompPreset, image: StompImage },
  { data: StoneSkipPreset, image: StoneSkipImage },
  { data: StrikePreset, image: StrikeImage },
  { data: SuccessFlourishPreset, image: SuccessFlourishImage },
  { data: SurpriseGaspPreset, image: SurpriseGaspImage },
  { data: SwayPreset, image: SwayImage },
  { data: SyncopatePreset, image: SyncopateImage },
  { data: TadaPreset, image: TadaImage },
  { data: ThudPreset, image: ThudImage },
  { data: ThumpPreset, image: ThumpImage },
  { data: ThunderPreset, image: ThunderImage },
  { data: ThunderRollPreset, image: ThunderRollImage },
  { data: TickTockPreset, image: TickTockImage },
  { data: TidalSurgePreset, image: TidalSurgeImage },
  { data: TideSwellPreset, image: TideSwellImage },
  { data: TremorPreset, image: TremorImage },
  { data: TypewriterPreset, image: TypewriterImage },
  { data: VictoryPreset, image: VictoryImage },
  { data: VomitingPreset, image: VomitingImage },
  { data: VortexPreset, image: VortexImage },
  { data: WarDrumPreset, image: WarDrumImage },
  { data: WarningPulsePreset, image: WarningPulseImage },
  { data: WarningUrgentPreset, image: WarningUrgentImage },
  { data: WaterfallPreset, image: WaterfallImage },
  { data: WispPreset, image: WispImage },
  { data: WobblePreset, image: WobbleImage },
  { data: WoodpeckerPreset, image: WoodpeckerImage },
  { data: ZeldaChestPreset, image: ZeldaChestImage },
  { data: ZipperPreset, image: ZipperImage },
// CODEGEN_END_{presets}
];
