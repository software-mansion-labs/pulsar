import { useState } from 'react';
import { CodeTabs } from '../CodeTabs/CodeTabs';
import style from './Preset.module.scss';
import { VisualizationPanel } from '../VisualizationPanel/VisualizationPanel';
import { Accordion } from '../Accordion/Accordion';
import { Tag } from '../Tag/Tag';
import { Modal } from '../Modal/Modal';
import type { PresetConfig } from './types';
import codeIcon from '../../assets/new_assets/code.svg';
import copyIcon from '../../assets/new_assets/copy.svg';

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
  const [definitionOpen, setDefinitionOpen] = useState(false);

  function handleUsageToggle() {
    if (!usageViewed) {
      window.posthog?.capture('preset_code_copied', { preset_name: data.name });
      setUsageViewed(true);
    }
  }

  const definitionJson = JSON.stringify(
    { continuousPattern: data.continuousPattern, discretePattern: data.discretePattern },
    null,
    2
  );

  function handleCopy() {
    navigator.clipboard.writeText(definitionJson);
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

      <button className={style.definitionButton} onClick={() => setDefinitionOpen(true)}>
        <img src={codeIcon.src} alt="Definition" />
      </button>

      <div className={style.header}>
        <div className={style.name}>{data.name}</div>
        <div className={style.description}>{data.description}</div>
      </div>

      {definitionOpen && (
        <Modal title="Preset Definition" onClose={() => setDefinitionOpen(false)}>
          <div className={style.jsonContainer}>
            <pre className={style.jsonBlock}>{definitionJson}</pre>
            <button className={style.copyButton} onClick={handleCopy}>
              <img src={copyIcon.src} alt="Copy" />
            </button>
          </div>
        </Modal>
      )}

      <VisualizationPanel visualization={preset} presetName={data.name} />

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
