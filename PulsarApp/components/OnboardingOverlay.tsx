import React from 'react';
import { View } from 'react-native';
import { BaseButton } from 'react-native-gesture-handler';
import { ImperativePatternComposer } from 'react-native-pulsar';
import { StateSwipe } from './StateSwipe';
import { StateTap } from './StateTap';

const impulse1 = new ImperativePatternComposer();
impulse1.parse({discretePattern: [{ time: 0, amplitude: 0.8, frequency: 0.8 }], continuesPattern: { amplitude: [], frequency: [] }});

interface Props {
  children?: React.ReactNode;
  state: number;
}

function OnboardingOverlay({ style, children, state }: Props & { style?: React.ComponentProps<typeof View>['style'] }) {
  return (<>
    {state === 1 && <StateTap>{children}</StateTap>}
    {state === 2 && <StateSwipe>{children}</StateSwipe>}
    {state === 3 && children}
  </>);
};

export default OnboardingOverlay;