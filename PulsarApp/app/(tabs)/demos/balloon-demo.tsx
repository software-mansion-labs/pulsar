import { useEffect, useRef } from 'react';
import { Pressable, StyleSheet, View } from 'react-native';
import { Pattern, usePatternComposer } from 'react-native-pulsar';
import Animated, {
  Easing,
  Extrapolation,
  cancelAnimation,
  interpolate,
  runOnJS,
  useAnimatedReaction,
  useAnimatedStyle,
  useSharedValue,
  withTiming,
} from 'react-native-reanimated';

import BasicLayout from '@/components/BasicLayout';
import { ThemedText } from '@/components/themed-text';
import { Colors, Margins } from '@/constants/theme';

const BALLOON_COUNT = 4;
const CHARGE_TO_POP_MS = 950;
const RELEASE_RESET_MS = 180;
const POP_FADE_MS = 120;
const COOLDOWN_MS = 1400;

const risePatterns: Pattern[] = [
  {
    discretePattern: [
      { time: 0, amplitude: 0.35, frequency: 0.9 },
      { time: 35, amplitude: 0, frequency: 0.9 },
    ],
    continuousPattern: { amplitude: [], frequency: [] },
  },
  {
    discretePattern: [
      { time: 0, amplitude: 0.3, frequency: 0.6 },
      { time: 45, amplitude: 0, frequency: 0.6 },
    ],
    continuousPattern: { amplitude: [], frequency: [] },
  },
  {
    discretePattern: [
      { time: 0, amplitude: 0.4, frequency: 0.4 },
      { time: 55, amplitude: 0, frequency: 0.4 },
    ],
    continuousPattern: { amplitude: [], frequency: [] },
  },
  {
    discretePattern: [
      { time: 0, amplitude: 0.32, frequency: 0.2 },
      { time: 65, amplitude: 0, frequency: 0.2 },
    ],
    continuousPattern: { amplitude: [], frequency: [] },
  },
];

const popPatterns: Pattern[] = [
  {
    discretePattern: [
      { time: 0, amplitude: 1, frequency: 0.9 },
      { time: 40, amplitude: 0, frequency: 0.9 },
      { time: 70, amplitude: 0.45, frequency: 0.7 },
      { time: 110, amplitude: 0, frequency: 0.7 },
    ],
    continuousPattern: { amplitude: [], frequency: [] },
  },
  {
    discretePattern: [
      { time: 0, amplitude: 0.85, frequency: 0.65 },
      { time: 50, amplitude: 0, frequency: 0.65 },
      { time: 90, amplitude: 0.65, frequency: 0.4 },
      { time: 135, amplitude: 0, frequency: 0.4 },
    ],
    continuousPattern: { amplitude: [], frequency: [] },
  },
  {
    discretePattern: [
      { time: 0, amplitude: 0.9, frequency: 0.35 },
      { time: 65, amplitude: 0, frequency: 0.35 },
      { time: 110, amplitude: 0.55, frequency: 0.2 },
      { time: 160, amplitude: 0, frequency: 0.2 },
    ],
    continuousPattern: { amplitude: [], frequency: [] },
  },
  {
    discretePattern: [
      { time: 0, amplitude: 0.75, frequency: 1 },
      { time: 35, amplitude: 0, frequency: 1 },
      { time: 65, amplitude: 0.95, frequency: 0.55 },
      { time: 115, amplitude: 0, frequency: 0.55 },
    ],
    continuousPattern: { amplitude: [], frequency: [] },
  },
];

type BalloonCellProps = {
  index: number;
  risePattern: Pattern;
  popPattern: Pattern;
};

function BalloonCell({ index, risePattern, popPattern }: BalloonCellProps) {
  const riseComposer = usePatternComposer();
  const popComposer = usePatternComposer();

  const progress = useSharedValue(0);
  const balloonOpacity = useSharedValue(1);
  const poppedOpacity = useSharedValue(0);
  const poppedRef = useRef(false);
  const cooldownTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  useEffect(() => {
    riseComposer.parse(risePattern);
    popComposer.parse(popPattern);

    return () => {
      if (cooldownTimerRef.current) {
        clearTimeout(cooldownTimerRef.current);
      }
    };
  }, [popComposer, popPattern, riseComposer, risePattern]);

  const playRise = () => {
    riseComposer.play();
  };

  const handlePop = () => {
    if (poppedRef.current) {
      return;
    }

    poppedRef.current = true;
    popComposer.play();

    balloonOpacity.value = withTiming(0, { duration: POP_FADE_MS });
    poppedOpacity.value = withTiming(1, { duration: POP_FADE_MS });

    if (cooldownTimerRef.current) {
      clearTimeout(cooldownTimerRef.current);
    }

    cooldownTimerRef.current = setTimeout(() => {
      progress.value = 0;
      poppedOpacity.value = withTiming(0, { duration: 140 });
      balloonOpacity.value = withTiming(1, { duration: 220 });
      poppedRef.current = false;
    }, COOLDOWN_MS);
  };

  useAnimatedReaction(
    () => progress.value,
    (current, previous) => {
      const prev = previous ?? 0;

      if (current >= 1 && prev < 1) {
        runOnJS(handlePop)();
        return;
      }

      if ((prev < 0.25 && current >= 0.25) || (prev < 0.5 && current >= 0.5) || (prev < 0.75 && current >= 0.75)) {
        runOnJS(playRise)();
      }
    },
    [],
  );

  const balloonStyle = useAnimatedStyle(() => {
    const translateY = interpolate(progress.value, [0, 1], [0, -24], Extrapolation.CLAMP);
    const scale = interpolate(progress.value, [0, 1], [1, 1.2], Extrapolation.CLAMP);
    const opacity = interpolate(progress.value, [0, 1], [1, 0.85], Extrapolation.CLAMP);

    return {
      transform: [{ translateY }, { scale }],
      opacity: opacity * balloonOpacity.value,
    };
  });

  const poppedStyle = useAnimatedStyle(() => ({
    opacity: poppedOpacity.value,
  }));

  const startCharge = () => {
    if (poppedRef.current) {
      return;
    }

    cancelAnimation(progress);
    progress.value = withTiming(1, {
      duration: CHARGE_TO_POP_MS,
      easing: Easing.out(Easing.cubic),
    });
  };

  const stopCharge = () => {
    if (poppedRef.current) {
      return;
    }

    cancelAnimation(progress);
    progress.value = withTiming(0, {
      duration: RELEASE_RESET_MS,
      easing: Easing.out(Easing.quad),
    });
  };

  return (
    <Pressable style={styles.cell} delayLongPress={140} onLongPress={startCharge} onPressOut={stopCharge}>
      <View style={styles.dotSlot}>
        <Animated.View style={[styles.dot, balloonStyle]} />
        <Animated.View style={[styles.poppedDot, styles.poppedOverlay, poppedStyle]} />
      </View>
    </Pressable>
  );
}

export default function TypingFeedbackDemo() {
  return (
    <BasicLayout>
      <ThemedText type="title" style={Margins.marginTop4X}>
        Popping balloons
      </ThemedText>
      <ThemedText style={Margins.marginTop2X}>
        Long press each dot to charge it. As it rises, subtle haptics guide the buildup.
      </ThemedText>
      <ThemedText style={styles.helperText}>
        Reach the top to pop it with a unique haptic burst. Popped balloons respawn after a short cooldown.
      </ThemedText>

      <View style={styles.grid}>
        {Array.from({ length: BALLOON_COUNT }).map((_, index) => (
          <BalloonCell
            key={`balloon-${index}`}
            index={index}
            risePattern={risePatterns[index]}
            popPattern={popPatterns[index]}
          />
        ))}
      </View>
    </BasicLayout>
  );
}

const styles = StyleSheet.create({
  helperText: {
    marginTop: 8,
  },
  grid: {
    marginTop: 24,
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
    rowGap: 14,
  },
  cell: {
    width: '48%',
    borderWidth: 1,
    borderColor: Colors.light.borderColor,
    borderRadius: 14,
    paddingVertical: 16,
    alignItems: 'center',
    backgroundColor: Colors.light.background,
  },
  dotSlot: {
    width: 64,
    height: 64,
    alignItems: 'center',
    justifyContent: 'center',
    position: 'relative',
  },
  dot: {
    position: 'absolute',
    width: 26,
    height: 26,
    borderRadius: 13,
    backgroundColor: Colors.light.tint,
  },
  poppedDot: {
    position: 'absolute',
    width: 24,
    height: 24,
    borderRadius: 12,
    borderWidth: 2,
    borderStyle: 'dashed',
    borderColor: Colors.light.borderColor,
    opacity: 0.7,
  },
  poppedOverlay: {
    opacity: 0,
  },
  statusText: {
    marginTop: 10,
  },
});
