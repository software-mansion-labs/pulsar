import React from 'react';
import { StyleSheet, View } from 'react-native';
import Animated from 'react-native-reanimated';
import { Image } from 'expo-image';
import { BaseButton } from 'react-native-gesture-handler';
import { ImperativePatternComposer } from 'react-native-pulsar';

const impulse1 = new ImperativePatternComposer();
impulse1.parse({discretePattern: [{ time: 0, amplitude: 0.8, frequency: 0.8 }], continuesPattern: { amplitude: [], frequency: [] }});

const handImage = require('@/assets/images/hand.png');
const ellipseSvg = require('@/assets/images/ellipse.svg');

interface Props {
  children?: React.ReactNode;
}

const GLOBAL_DELAY = 2000;

function OnboardingOverlay({ style, children }: Props & { style?: React.ComponentProps<typeof View>['style'] }) {
  const [currentState, setCurrentState] = React.useState(1);

  return (<>
    {currentState === 1 && <State1 onClick={() => setCurrentState(2)}>{children}</State1>}
    {currentState === 2 && children}
  </>);
};

function State1({ children, onClick }: {children?: React.ReactNode; onClick?: () => void }) {
  return (
    <BaseButton 
      style={[styles.container]} 
      onPress={() => {
        impulse1.play()
        if (onClick) {
          onClick();
        }
      }}
    >
      {children}

      <View style={styles.circlesContainer}>
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

      <Animated.View style={[styles.handWrapper, styles.clickAnimation]}>
        <Image source={handImage} style={styles.hand} contentFit="contain" />
      </Animated.View>

    </BaseButton>
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
  clickAnimation: {
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

export default OnboardingOverlay;