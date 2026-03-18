import { Presets } from "react-native-pulsar";
import { PresetProps } from "./types";

// CODEGEN_BEGIN_{imports}
const placeholder = require('@/assets/images/chart_placeholder.png');
// CODEGEN_END_{imports}

export const PresetsConfig: Array<PresetProps> = [
// CODEGEN_BEGIN_{presets}
  {
    name: 'Sparkle',
    description: "That feeling when some bricks fall onto your foot!",
    tags: ["Gentle", "Soft", "Bump", "Short"],
    duration: 1500,
    image: placeholder,
    play: Presets.Earthquake,
  },
  {
    name: 'Sparkle',
    description: "That feeling when some bricks fall onto your foot!",
    tags: ["Gentle", "Soft", "Bump", "Short"],
    duration: 1500,
    image: placeholder,
    play: Presets.Earthquake,
  },
// CODEGEN_END_{presets}
];