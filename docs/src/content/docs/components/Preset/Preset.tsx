import { useRef, useState } from 'react';
import { CodeTabs } from '../CodeTabs/CodeTabs';
import style from './Preset.module.scss';
import { VisualizationPanel } from '../VisualizationPanel/VisualizationPanel';
import { Accordion } from '../Accordion/Accordion';
import { Tag } from '../Tag/Tag';
import type { PresetConfig } from './types';
import { API_SERVER_URL } from '../config';

declare global {
  interface Window {
    posthog?: {
      capture: (event: string, properties?: Record<string, unknown>) => void;
    };
  }
}

function getSwiftPresetImport(shortName: string) {
  return `import Pulsar\n\nPulsar.${shortName}.play()`;
}

function getReactNativePresetImport(shortName: string) {
  return `import { Pulsar } from '@haptics/library';\n\nPulsar.${shortName}.play();`;
}

export function Preset(preset: PresetConfig) {
  const { data } = preset;
  const [usageViewed, setUsageViewed] = useState(false);

  function handleUsageToggle() {
    if (!usageViewed) {
      window.posthog?.capture('preset_code_copied', { preset_name: data.name });
      setUsageViewed(true);
    }
  }

  return (
    <div className={style.preset}>
      {data.tags && data.tags.length > 0 && (
        <div className={style.tagsBar}>
          {data.tags.map((tag, idx) => (
            <Tag key={idx} label={tag} variant='blue' />
          ))}
        </div>
      )}

      <div className={style.header}>
        <div className={style.name}>{data.name}</div>
        <div className={style.description}>{data.description}</div>
      </div>

      <VisualizationPanel visualization={preset} duration={data.duration} presetName={data.name} />

      <div onClick={handleUsageToggle}>
        <Accordion title="Usage" className={style.marginTop}>
          <CodeTabs
            swift={getSwiftPresetImport(data.name)}
            reactNative={getReactNativePresetImport(data.name)}
          />
        </Accordion>
      </div>
    </div>
  );
}
