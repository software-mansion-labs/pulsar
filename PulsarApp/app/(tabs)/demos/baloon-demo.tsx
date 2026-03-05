import { useEffect, useMemo, useRef, useState } from 'react';
import { Pressable, StyleSheet, View } from 'react-native';
import { Pattern, usePatternComposer } from 'react-native-pulsar';

import BasicLayout from '@/components/BasicLayout';
import { ThemedText } from '@/components/themed-text';
import { Colors, Margins } from '@/constants/theme';

type BalloonState = {
  progress: number;
  popped: boolean;
};

const BALLOON_COUNT = 4;
const CHARGE_STEP = 0.08;
const CHARGE_INTERVAL_MS = 70;
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

const initialBalloons: BalloonState[] = Array.from({ length: BALLOON_COUNT }, () => ({
  progress: 0,
  popped: false,
}));

export default function TypingFeedbackDemo() {
  const [balloons, setBalloons] = useState<BalloonState[]>(initialBalloons);

  const riseComposer1 = usePatternComposer();
  const riseComposer2 = usePatternComposer();
  const riseComposer3 = usePatternComposer();
  const riseComposer4 = usePatternComposer();

  const popComposer1 = usePatternComposer();
  const popComposer2 = usePatternComposer();
  const popComposer3 = usePatternComposer();
  const popComposer4 = usePatternComposer();

  const riseComposers = useMemo(
    () => [riseComposer1, riseComposer2, riseComposer3, riseComposer4],
    [riseComposer1, riseComposer2, riseComposer3, riseComposer4],
  );

  const popComposers = useMemo(
    () => [popComposer1, popComposer2, popComposer3, popComposer4],
    [popComposer1, popComposer2, popComposer3, popComposer4],
  );

  const holdRef = useRef<boolean[]>(Array(BALLOON_COUNT).fill(false));
  const intervalsRef = useRef<(ReturnType<typeof setInterval> | null)[]>(Array(BALLOON_COUNT).fill(null));
  const respawnRef = useRef<(ReturnType<typeof setTimeout> | null)[]>(Array(BALLOON_COUNT).fill(null));
  const stageRef = useRef<number[]>(Array(BALLOON_COUNT).fill(0));

  useEffect(() => {
    riseComposers.forEach((composer, index) => composer.parse(risePatterns[index]));
    popComposers.forEach((composer, index) => composer.parse(popPatterns[index]));

    const activeIntervals = intervalsRef.current;
    const activeRespawnTimers = respawnRef.current;

    return () => {
      activeIntervals.forEach((timer) => {
        if (timer) {
          clearInterval(timer);
        }
      });
      activeRespawnTimers.forEach((timer) => {
        if (timer) {
          clearTimeout(timer);
        }
      });
    };
  }, [popComposers, riseComposers]);

  const stopCharging = (index: number) => {
    holdRef.current[index] = false;

    if (intervalsRef.current[index]) {
      clearInterval(intervalsRef.current[index]!);
      intervalsRef.current[index] = null;
    }

    setBalloons((prev) => {
      if (prev[index].popped || prev[index].progress === 0) {
        return prev;
      }
      const next = [...prev];
      next[index] = { ...next[index], progress: 0 };
      return next;
    });
    stageRef.current[index] = 0;
  };

  const startCharging = (index: number) => {
    if (balloons[index].popped || intervalsRef.current[index]) {
      return;
    }

    holdRef.current[index] = true;

    intervalsRef.current[index] = setInterval(() => {
      if (!holdRef.current[index]) {
        if (intervalsRef.current[index]) {
          clearInterval(intervalsRef.current[index]!);
          intervalsRef.current[index] = null;
        }
        return;
      }

      setBalloons((prev) => {
        const current = prev[index];

        if (current.popped) {
          return prev;
        }

        const nextProgress = Math.min(1, current.progress + CHARGE_STEP);
        const nextStage = Math.floor(nextProgress / 0.25);

        if (nextStage > stageRef.current[index] && nextStage < 4) {
          stageRef.current[index] = nextStage;
          riseComposers[index].play();
        }

        const next = [...prev];

        if (nextProgress >= 1) {
          next[index] = { progress: 1, popped: true };
          holdRef.current[index] = false;
          stageRef.current[index] = 0;
          popComposers[index].play();

          if (intervalsRef.current[index]) {
            clearInterval(intervalsRef.current[index]!);
            intervalsRef.current[index] = null;
          }

          if (respawnRef.current[index]) {
            clearTimeout(respawnRef.current[index]!);
          }

          respawnRef.current[index] = setTimeout(() => {
            setBalloons((latest) => {
              const restored = [...latest];
              restored[index] = { progress: 0, popped: false };
              return restored;
            });
          }, COOLDOWN_MS);

          return next;
        }

        next[index] = { ...current, progress: nextProgress };
        return next;
      });
    }, CHARGE_INTERVAL_MS);
  };

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
        {balloons.map((balloon, index) => {
          const translateY = -24 * balloon.progress;
          const scale = 1 + balloon.progress * 0.2;

          return (
            <Pressable
              key={`balloon-${index}`}
              style={styles.cell}
              disabled={balloon.popped}
              delayLongPress={140}
              onLongPress={() => startCharging(index)}
              onPressOut={() => stopCharging(index)}>
              <View style={styles.dotSlot}>
                {balloon.popped ? (
                  <View style={styles.poppedDot} />
                ) : (
                  <View
                    style={[
                      styles.dot,
                      {
                        transform: [{ translateY }, { scale }],
                        opacity: 1 - balloon.progress * 0.15,
                      },
                    ]}
                  />
                )}
              </View>
              <ThemedText style={styles.statusText}>
                {balloon.popped ? 'Cooling down...' : `Balloon ${index + 1}`}
              </ThemedText>
            </Pressable>
          );
        })}
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
  },
  dot: {
    width: 26,
    height: 26,
    borderRadius: 13,
    backgroundColor: Colors.light.tint,
  },
  poppedDot: {
    width: 24,
    height: 24,
    borderRadius: 12,
    borderWidth: 2,
    borderStyle: 'dashed',
    borderColor: Colors.light.borderColor,
    opacity: 0.7,
  },
  statusText: {
    marginTop: 10,
  },
});
