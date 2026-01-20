npx @react-native-community/cli@latest init example
cd example
npm i react-native-reanimated
npm i react-native-worklets
npm link ./react-native-pulsar
cp react-native-pulsar/scripts/exampleApp/data/react-native.config.js example/react-native.config.js
cp react-native-pulsar/scripts/exampleApp/data/metro.config.js example/metro.config.js