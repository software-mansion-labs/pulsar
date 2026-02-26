import { StyleSheet } from 'react-native';
import { Gesture, GestureDetector } from 'react-native-gesture-handler';
import Animated, { useAnimatedStyle, useSharedValue } from 'react-native-reanimated';
import { Image } from 'expo-image';
import OnboardingOverlay from './OnboardingOverlay';
import { useRealtimeComposer, ImperativePatternComposer, Pattern } from 'react-native-pulsar';
import { runOnUISync, scheduleOnRN, scheduleOnUI } from 'react-native-worklets';
import { useOnboarding } from '@/contexts/OnboardingContext';
import { useState, useRef, useImperativeHandle, forwardRef, useEffect } from 'react';

const gridImage = require('@/assets/images/grid.svg');

declare global {
  var tapTimer: any;
  var PatternRecorder: any[];
  var PatternRecorderStartTime: number;
}

type RecordedEvent = {
  type: 'tap' | 'pan';
  time: number;
  x: number;
  y: number;
  amplitude?: number;
  frequency?: number;
};

export type GesturePlaygroundHandle = {
  startRecording: () => void;
  stopRecording: () => void;
  playRecordedPattern: () => void;
  getPatternAsJson: () => string | null;
};

type GesturePlaygroundProps = {
  onRecordingChange?: (isRecording: boolean) => void;
  onPlayingChange?: (isPlaying: boolean) => void;
  onRecordedChange?: (isRecorded: boolean) => void;
};

function convertToPattern(events: RecordedEvent[]): Pattern {
  const discretePattern: { time: number, amplitude: number, frequency: number }[] = [];
  const continuousAmplitude: { time: number, value: number }[] = [];
  const continuousFrequency: { time: number, value: number }[] = [];

  let lastPanTime = 0;
  const panGroups: RecordedEvent[][] = [];
  let currentPanGroup: RecordedEvent[] = [];

  events.forEach((event) => {
    if (event.type === 'tap') {
      discretePattern.push({
        time: event.time,
        amplitude: event.y,
        frequency: event.x,
      });
    } else if (event.type === 'pan') {
      if (currentPanGroup.length === 0 || event.time - lastPanTime < 100) {
        currentPanGroup.push(event);
      } else {
        if (currentPanGroup.length > 0) {
          panGroups.push([...currentPanGroup]);
        }
        currentPanGroup = [event];
      }
      lastPanTime = event.time;
    }
  });

  if (currentPanGroup.length > 0) {
    panGroups.push(currentPanGroup);
  }

  // Convert pan groups to continuous pattern
  panGroups.forEach((group) => {
    group.forEach((event) => {
      continuousAmplitude.push({
        time: event.time,
        value: event.y,
      });
      continuousFrequency.push({
        time: event.time,
        value: event.x,
      });
    });

    // Add end point
    const lastEvent = group[group.length - 1];
    continuousAmplitude.push({
      time: lastEvent.time,
      value: 0,
    });
    continuousFrequency.push({
      time: lastEvent.time,
      value: 0,
    });
  });

  return {
    discretePattern,
    continuesPattern: {
      amplitude: continuousAmplitude,
      frequency: continuousFrequency,
    },
  };
};

const GesturePlayground = forwardRef<GesturePlaygroundHandle, GesturePlaygroundProps>(function GesturePlayground(
  { onRecordingChange, onPlayingChange, onRecordedChange },
  ref
) {
  const { onboardingState, setOnboardingState } = useOnboarding();
  const composer = useRealtimeComposer();
  const containerSize = useSharedValue({ width: 0, height: 0 });
  const tapIndicatorPosition = useSharedValue({ x: -100, y: -100 });
  const panIndicatorPosition = useSharedValue({ x: -100, y: -100 });
  // Recording state
  const recordedPattern = useSharedValue<any[]>([]);
  const [isRecording, setIsRecording] = useState(false);
  const [isPlaying, setIsPlaying] = useState(false);
  const patternComposer = useRef(new ImperativePatternComposer());

  const startRecording = () => {
    setIsRecording(true);
    recordedPattern.value = [];
    scheduleOnUI(() => {
      global.PatternRecorderStartTime = Date.now();
      global.PatternRecorder = [];
    });
  };

  const stopRecording = () => {
    setIsRecording(false);
    runOnUISync(() => {
      recordedPattern.value = global.PatternRecorder;
    });
    onRecordedChange?.(recordedPattern.value.length > 0);
  };

  const playRecordedPattern = () => {
    if (recordedPattern.value.length === 0) {
      console.log('No recorded events to play');
      return;
    }

    setIsPlaying(true);
    const recordedEvents = recordedPattern.value as RecordedEvent[];
    const pattern = convertToPattern(recordedPattern.value);
    
    patternComposer.current.parse(pattern);
    patternComposer.current.play();

    // Estimate duration and reset playing state
    const lastEvent = recordedEvents[recordedEvents.length - 1];
    const duration = lastEvent?.time * 1000;
    setTimeout(() => {
      setIsPlaying(false);
    }, duration + 100);
  };

  const recordEvent = (type: 'tap' | 'pan', x: number, y: number) => {
    'worklet';
    const time = (Date.now() - global.PatternRecorderStartTime) / 1000;
    const event: RecordedEvent = { type, time, x, y };
    if (!global.PatternRecorder) {
      global.PatternRecorder = []
    }
    global.PatternRecorder.push(event);
  };

  const getPatternAsJson = () => {
    if (recordedPattern.value.length === 0) {
      return null;
    }
    const pattern = convertToPattern(recordedPattern.value);
    return "Pulsar custom preset\n\n" + JSON.stringify(pattern, null, 2);
  };

  useImperativeHandle(ref, () => ({
    startRecording,
    stopRecording,
    playRecordedPattern,
    getPatternAsJson,
  }));

  useEffect(() => {
    onRecordingChange?.(isRecording);
  }, [isRecording, onRecordingChange]);

  useEffect(() => {
    onPlayingChange?.(isPlaying);
  }, [isPlaying, onPlayingChange]);

  const tapIndicatorStyle = useAnimatedStyle(() => ({
    left: tapIndicatorPosition.value.x - 15,
    top: tapIndicatorPosition.value.y - 15,
  }));

  const panIndicatorStyle = useAnimatedStyle(() => ({
    left: panIndicatorPosition.value.x - 15,
    top: panIndicatorPosition.value.y - 15,
  }));

  const handleLayout = (event: any) => {
    const { width, height } = event.nativeEvent.layout;
    containerSize.value = { width, height };
  };

  const normalizePosition = (x: number, y: number) => {
    'worklet';
    return {
      x: (containerSize.value.width > 0 ? x / containerSize.value.width : 0),
      y: 1 - (containerSize.value.height > 0 ? y / containerSize.value.height : 0),
    };
  };

  const clampIndicatorPosition = (x: number, y: number) => {
    'worklet';
    if (x === -100 && y === -100) {
      return { x, y };
    }
    
    const clampedX = Math.max(15, Math.min(x, containerSize.value.width - 15));
    const clampedY = Math.max(15, Math.min(y, containerSize.value.height - 15));
    
    return { x: clampedX, y: clampedY };
  };

  const tapGesture = Gesture.Tap().onStart((e) => {
    const normalized = normalizePosition(e.x, e.y);
    composer.playDiscrete(normalized.y, normalized.x);
    recordEvent('tap', normalized.x, normalized.y);

    const clamped = clampIndicatorPosition(e.x, e.y);
    tapIndicatorPosition.value = clamped;
    global.tapTimer = setTimeout(() => {
      tapIndicatorPosition.value = { x: -100, y: -100 };
    }, 100);

  }).onEnd(() => {
    // tapIndicatorPosition.value = { x: -100, y: -100 };
  });

  const panGesture = Gesture.Pan()
    .onBegin((e) => {
      // const normalized = normalizePosition(e.x, e.y);
      // console.log('Pan begin', { absolute: { x: e.x, y: e.y }, normalized });
      // panIndicatorPosition.value = { x: e.x, y: e.y };
    })
    .onUpdate((e) => {
      const normalized = normalizePosition(e.x, e.y);
      composer.update(normalized.y, normalized.x);
      recordEvent('pan', normalized.x, normalized.y);
      // console.log('Pan update', { absolute: { x: e.x, y: e.y }, normalized });
      const clamped = clampIndicatorPosition(e.x, e.y);
      panIndicatorPosition.value = clamped;
    })
    .onEnd((e) => {
      composer.stop();
      // const normalized = normalizePosition(e.x, e.y);
      // console.log('Pan end', { absolute: { x: e.x, y: e.y }, normalized });
      panIndicatorPosition.value = { x: -100, y: -100 };
    });

  const composedGesture = Gesture.Simultaneous(tapGesture, panGesture);

  const updateOnboardingState = (newState: number) => {
    if (onboardingState >= newState) {
      return;
    }
    setOnboardingState(newState);
  }
  const onboardingTapGesture = Gesture.Tap().onStart((e) => {
    scheduleOnRN(updateOnboardingState, 2);
    const normalized = normalizePosition(e.x, e.y);
    composer.playDiscrete(normalized.y, normalized.x);
    tapIndicatorPosition.value = clampIndicatorPosition(e.x, e.y);;
    global.tapTimer = setTimeout(() => {
      tapIndicatorPosition.value = { x: -100, y: -100 };
    }, 100);
  });
  const onboardingPanGesture = Gesture.Pan()
    .onUpdate((e) => {
      const normalized = normalizePosition(e.x, e.y);
      composer.update(normalized.y, normalized.x);
      panIndicatorPosition.value = clampIndicatorPosition(e.x, e.y);
    })
    .onEnd(() => {
      scheduleOnRN(updateOnboardingState, 3);
      composer.stop();
      panIndicatorPosition.value = { x: -100, y: -100 };
    });
  const onboardingComposedGesture = Gesture.Simultaneous(onboardingTapGesture, onboardingPanGesture);

  return (
    <OnboardingOverlay state={onboardingState}>
      <GestureDetector gesture={onboardingState === 3 ? composedGesture : onboardingComposedGesture}>
        <Animated.View  style={styles.gridContainer} onLayout={handleLayout}>
          <Animated.View style={[styles.tapIndicator, tapIndicatorStyle]} />
          <Animated.View style={[styles.panIndicator, panIndicatorStyle]} />
          <Image
            source={gridImage}
            style={styles.grid}
            contentFit="contain"
          />
        </Animated.View>
      </GestureDetector>
    </OnboardingOverlay>
  );
});

export default GesturePlayground;

const styles = StyleSheet.create({
  gridContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    position: 'relative',
  },
  grid: {
    width: '100%',
    height: '100%',
  },
  tapIndicator: {
    position: 'absolute',
    left: -100,
    top: -100,
    zIndex: 10,
    width: 30,
    height: 30,
    borderRadius: 15,
    backgroundColor: '#FF6259',
    opacity: 0.8,
  },
  panIndicator: {
    position: 'absolute',
    left: -100,
    top: -100,
    zIndex: 10,
    width: 30,
    height: 30,
    borderRadius: 15,
    backgroundColor: '#001A72',
    opacity: 0.8,
  },
});