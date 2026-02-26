import React from 'react';
import { StyleSheet, View } from 'react-native';
import Animated, { FadeIn, FadeOut } from 'react-native-reanimated';
import { Image } from 'expo-image';

const handImage = require('@/assets/images/hand.png');
const ellipseSvg = require('@/assets/images/ellipse.svg');

const GLOBAL_DELAY = 1000;

export function StateTap({ children }: { children?: React.ReactNode }) {
  return (
    <Animated.View style={[styles.container]} entering={FadeIn} exiting={FadeOut}>
      {children}

      <View style={styles.circlesContainer} pointerEvents="none">
        <Animated.View style={[styles.pulseAnimation, { animationDelay: GLOBAL_DELAY + 50 }]}>
          <Image source={ellipseSvg} style={[styles.circle, { width: 60, height: 60 }]} contentFit="contain" />
        </Animated.View>
        <Animated.View style={[styles.pulseAnimation, { animationDelay: GLOBAL_DELAY + 100 }]}>
          <Image source={ellipseSvg} style={[styles.circle, { width: 90, height: 90 }]} contentFit="contain" />
        </Animated.View>
        <Animated.View style={[styles.pulseAnimation, { animationDelay: GLOBAL_DELAY + 150 }]}>
          <Image source={ellipseSvg} style={[styles.circle, { width: 120, height: 120 }]} contentFit="contain" />
        </Animated.View>
        <Animated.View style={[styles.pulseAnimation, { animationDelay: GLOBAL_DELAY + 200 }]}>
          <Image source={ellipseSvg} style={[styles.circle, { width: 150, height: 150 }]} contentFit="contain" />
        </Animated.View>
        <Animated.View style={[styles.pulseAnimation, { animationDelay: GLOBAL_DELAY + 250 }]}>
          <Image source={ellipseSvg} style={[styles.circle, { width: 180, height: 180 }]} contentFit="contain" />
        </Animated.View>
      </View>

      <Animated.View style={[styles.handWrapper, styles.handAnimation]} pointerEvents="none">
        <Image source={handImage} style={styles.hand} contentFit="contain" />
      </Animated.View>

    </Animated.View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  circlesContainer: {
    position: 'absolute',
    left: '52%',
    bottom: '52%',
    justifyContent: 'center',
    alignItems: 'center',
  },
  circle: {
    position: 'absolute',
  },
  handWrapper: {
    position: 'absolute',
    left: '45%',
    bottom: '45%',
  },
  hand: {
    width: 60,
    height: 60,
  },
  pulseAnimation: {
    justifyContent: 'center',
    alignItems: 'center',
    opacity: 0,

    animationDuration: 2000,
    animationIterationCount: 'infinite',
    animationTimingFunction: 'ease-out',
    animationName: {
      0: {
        transform: [{ scale: 1 }],
        opacity: 0,
      },
      0.1: {
        opacity: 1,
      },
      0.7: {
        transform: [{ scale: 1.2 }],
        opacity: 0,
      },
      1: {
        transform: [{ scale: 1.2 }],
        opacity: 0,
      },
    },
  },
  handAnimation: {
    animationDelay: GLOBAL_DELAY,
    animationDuration: 2000,
    animationIterationCount: 'infinite',
    animationTimingFunction: 'ease-out',
    animationName: {
      0: {
        transform: [{ scale: 1 }],
        opacity: 1,
      },
      0.10: {
        transform: [{ scale: 0.8 }],
        opacity: 0.8,
      },
      0.2: {
        transform: [{ scale: 1 }],
        opacity: 1,
      },
      1: {
        transform: [{ scale: 1 }],
        opacity: 1,
      },
    },
  },
});
