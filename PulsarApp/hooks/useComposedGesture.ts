import { Gesture, type SimultaneousGesture } from 'react-native-gesture-handler';
import type { SharedValue } from 'react-native-reanimated';
import { Position, Composer, PositionTransform, RecordEventFn } from './gestureTypes';

export const useComposedGesture = (
  containerSize: SharedValue<{ width: number; height: number }>,
  composer: Composer,
  recordEvent: RecordEventFn,
  tapIndicatorPosition: SharedValue<Position>,
  panIndicatorPosition: SharedValue<Position>,
  clampIndicatorPosition: PositionTransform
): SimultaneousGesture => {
  const normalizePosition = (x: number, y: number) => {
    'worklet';
    return {
      x: (containerSize.value.width > 0 ? x / containerSize.value.width : 0),
      y: 1 - (containerSize.value.height > 0 ? y / containerSize.value.height : 0),
    };
  };
  const tapGesture = Gesture.Tap()
    .onStart((e) => {
      const normalized = normalizePosition(e.x, e.y);
      composer.playDiscrete(normalized.y, normalized.x);
      recordEvent('tap', normalized.x, normalized.y);

      const clamped = clampIndicatorPosition(e.x, e.y);
      tapIndicatorPosition.value = clamped;
      global.tapTimer = setTimeout(() => {
        tapIndicatorPosition.value = { x: -100, y: -100 };
      }, 100);
    })
    .onEnd(() => {
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
      composer.set(normalized.y, normalized.x);
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

  return Gesture.Simultaneous(tapGesture, panGesture);
};
