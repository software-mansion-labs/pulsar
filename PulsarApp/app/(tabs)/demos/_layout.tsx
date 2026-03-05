import { Stack } from 'expo-router';

export default function DemosLayout() {
  return (
    <Stack screenOptions={{ headerShown: true }}>
      <Stack.Screen name="index" options={{ headerShown: false, title: 'Demos' }} />
      <Stack.Screen name="slider-demo" options={{ headerShown: true, title: 'Slider Demo' }} />
      <Stack.Screen name="buttons-demo" options={{ headerShown: true, title: 'Buttons Demo' }} />
      
      <Stack.Screen name="payment-success" options={{ headerShown: true, title: 'Payment success' }} />
      <Stack.Screen name="camera-shutter" options={{ headerShown: true, title: 'Camera shutter' }} />
      <Stack.Screen name="workout-interval" options={{ headerShown: true, title: 'Workout interval' }} />
    </Stack>
  );
}
