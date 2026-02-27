module.exports = function (api) {
  api.cache(true);

  return {
    presets: ['babel-preset-expo'],
    plugins: [
      'expo-router/babel',
      'react-compiler',
      // Keep reanimated plugin last.
      'react-native-reanimated/plugin',
    ],
  };
};
