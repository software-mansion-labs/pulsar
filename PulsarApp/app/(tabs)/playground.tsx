import { StyleSheet, View, TouchableOpacity, Text, Share } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Image } from 'expo-image';
import { useThemeColor } from '@/hooks/use-theme-color';
import Button from '@/components/Button';
import { useEffect, useState } from 'react';

import { Link } from 'expo-router';
import { Gesture, GestureDetector } from 'react-native-gesture-handler';
import Animated, { useAnimatedRef, useAnimatedStyle, useSharedValue, measure } from 'react-native-reanimated';
import { runOnUIAsync } from 'react-native-worklets';
import GesturePlayground from '../../components/GesturePlayground';

const gridImage = require('@/assets/images/grid.svg');
const handImage = require('@/assets/images/hand.png');
const infoIcon = require('@/assets/images/info.svg');

const defaultEdges = {
  top: 'additive',
  left: 'additive',
  bottom: 'off',
  right: 'additive',
};

const stringToShare = "Example preset content";

export default function PlaygroundScreen() {
  // const containerSize = useSharedValue({ width: 0, height: 0 });
  // const tapIndicatorPosition = useSharedValue({ x: -100, y: -100 });
  // const panIndicatorPosition = useSharedValue({ x: -100, y: -100 });
  // const gridSize = useSharedValue({ width: 0, height: 0 });
  // const gridRef = useAnimatedRef();

  // const tapIndicatorStyle = useAnimatedStyle(() => ({
  //   left: tapIndicatorPosition.value.x - 15,
  //   top: tapIndicatorPosition.value.y - 15,
  // }));

  // const panIndicatorStyle = useAnimatedStyle(() => ({
  //   left: panIndicatorPosition.value.x - 15,
  //   top: panIndicatorPosition.value.y - 15,
  // }));

  const handleShare = async () => {
    try {
      await Share.share({
        message: stringToShare,
      });
    } catch (error) {
      console.error('Error sharing:', error);
    }
  };

  // const handleLayout = (event: any) => {
  //   const { width, height } = event.nativeEvent.layout;
  //   containerSize.value = { width, height };
  // };

  // const normalizePosition = (x: number, y: number) => {
  //   'worklet';
  //   return {
  //     x: containerSize.value.width > 0 ? x / containerSize.value.width : 0,
  //     y: containerSize.value.height > 0 ? y / containerSize.value.height : 0,
  //   };
  // };

  // const clampIndicatorPosition = (x: number, y: number) => {
  //   'worklet';
  //   // Allow hidden state: { x: -100, y: -100 }
  //   if (x === -100 && y === -100) {
  //     return { x, y };
  //   }
    
  //   // Clamp to grid bounds (indicator is 30x30 centered on position, so subtract 15)
  //   const clampedX = Math.max(15, Math.min(x, gridSize.value.width - 15));
  //   const clampedY = Math.max(15, Math.min(y, gridSize.value.height - 15));
    
  //   return { x: clampedX, y: clampedY };
  // };

  // const tapGesture = Gesture.Tap().onStart((e) => {
  //   const normalized = normalizePosition(e.x, e.y);
  //   console.log('Grid tapped', { absolute: { x: e.x, y: e.y }, normalized });

  //   const clamped = clampIndicatorPosition(e.x, e.y);
  //   tapIndicatorPosition.value = clamped;
  //   global.tapTimer = setTimeout(() => {
  //     tapIndicatorPosition.value = { x: -100, y: -100 };
  //   }, 100);

  // }).onEnd(() => {
  //   // tapIndicatorPosition.value = { x: -100, y: -100 };
  // });

  // const panGesture = Gesture.Pan()
  //   .onBegin((e) => {
  //     // const normalized = normalizePosition(e.x, e.y);
  //     // console.log('Pan begin', { absolute: { x: e.x, y: e.y }, normalized });
  //     // panIndicatorPosition.value = { x: e.x, y: e.y };
  //   })
  //   .onUpdate((e) => {
  //     const normalized = normalizePosition(e.x, e.y);
  //     console.log('Pan update', { absolute: { x: e.x, y: e.y }, normalized });
  //     const clamped = clampIndicatorPosition(e.x, e.y);
  //     panIndicatorPosition.value = clamped;
  //   })
  //   .onEnd((e) => {
  //     // const normalized = normalizePosition(e.x, e.y);
  //     // console.log('Pan end', { absolute: { x: e.x, y: e.y }, normalized });
  //     panIndicatorPosition.value = { x: -100, y: -100 };
  //   });

  // const composedGesture = Gesture.Simultaneous(tapGesture, panGesture);

  // useEffect(() => {
  //   runOnUIAsync(() => {
  //     'worklet';
  //     const size = measure(gridRef);
  //     gridSize.value = { width: size?.width ?? 0, height: size?.height ?? 0 };
  //   })();
  // }, []);

  return (
    <SafeAreaView edges={defaultEdges as any} style={styles.safeArea}>
      <View style={styles.container}>

        <View style={styles.header}>
          <ThemedText type="title" style={styles.title}>Playground</ThemedText>
          <Link href="/playgroundModal">
            <Link.Trigger>
              <View style={styles.howItWorksButton}>
                <ThemedText>How does it work?</ThemedText>
                <Image source={infoIcon} style={styles.infoIcon} />
              </View>
            </Link.Trigger>
          </Link>
        </View>


        {/* <GestureDetector gesture={composedGesture}>
          <Animated.View ref={gridRef} style={styles.gridContainer} onLayout={handleLayout}>
            <Animated.View style={[styles.tapIndicator, tapIndicatorStyle]} />
            <Animated.View style={[styles.panIndicator, panIndicatorStyle]} />
            <Image
              source={gridImage}
              style={styles.grid}
              contentFit="contain"
            />
          </Animated.View>
        </GestureDetector> */}

        {/* <Image
            source={handImage}
            style={styles.handPointer}
            contentFit="contain"
          /> */}

        <GesturePlayground />

        <View style={styles.controlsContainer}>
          <Button onClick={() => {}} showIcon="play" largeIcon={true} />
          <Button label="Record" onClick={() => {}} showIcon="record" fullWidth={true} />
          <Button onClick={handleShare} showIcon="download" largeIcon={true} />
        </View>
        
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
  },
  container: {
    flex: 1,
    paddingHorizontal: 20,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingTop: 20,
  },
  title: {
    fontSize: 32,
    fontWeight: 'bold',
  },
  howItWorksButton: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  infoIcon: {
    width: 18,
    height: 18,
  },
  infoIconText: {
    fontSize: 12,
    fontWeight: 'bold',
  },
  // gridContainer: {
  //   flex: 1,
  //   justifyContent: 'center',
  //   alignItems: 'center',
  //   position: 'relative',
  // },
  // grid: {
  //   width: '100%',
  //   height: '100%',
  // },
  handPointer: {
    width: 80,
    height: 80,
    position: 'absolute',
  },
  controlsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingBottom: 20,
    gap: 12,
  },
  controlButton: {
    borderWidth: 2,
    borderRadius: 8,
    width: 80,
    height: 80,
    justifyContent: 'center',
    alignItems: 'center',
  },
  playIcon: {
    fontSize: 24,
  },
  downloadIcon: {
    fontSize: 32,
    fontWeight: 'bold',
  },
  recordButton: {
    flex: 1,
    borderWidth: 3,
    borderColor: '#E74C3C',
    borderRadius: 8,
    height: 80,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    gap: 12,
  },
  recordDot: {
    width: 16,
    height: 16,
    borderRadius: 8,
    backgroundColor: '#E74C3C',
  },
  recordText: {
    fontSize: 18,
    fontWeight: '600',
  },

  // tapIndicator: {
  //   position: 'absolute',
  //   left: -100,
  //   top: -100,
  //   zIndex: 10,
  //   width: 30,
  //   height: 30,
  //   borderRadius: 15,
  //   backgroundColor: '#FF6259',
  //   opacity: 0.8,
  // },
  // panIndicator: {
  //   position: 'absolute',
  //   left: -100,
  //   top: -100,
  //   zIndex: 10,
  //   width: 30,
  //   height: 30,
  //   borderRadius: 15,
  //   backgroundColor: '#001A72',
  //   opacity: 0.8,
  // }
});
