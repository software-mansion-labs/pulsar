import style from './Preset.module.scss';
import { VisualizationPanel } from '../../../content/docs/components/VisualizationPanel/VisualizationPanel';
import { Tag } from '../../../content/docs/components/Tag/Tag';
import type { PresetConfig } from '../../../content/docs/components/Preset/types';
import { Modal } from '../../../content/docs/components/Modal/Modal';
import { useState } from 'react';

export function Preset(preset: PresetConfig) {
  const { data } = preset;
  const [isModalOpen, setIsModalOpen] = useState(false);

  return (
    <div className={style.preset}>
      {data.tags && data.tags.length > 0 && (
        <div className={style.tagsBar}>
          {data.tags.map((tag, idx) => (
            <Tag key={idx} label={tag} />
          ))}
        </div>
      )}

      <div className={style.header}>
        <div className={style.name}>{data.name}</div>
        <div className={style.description}>{data.description}</div>
      </div>

      <VisualizationPanel
        visualization={preset}
        duration={data.duration}
        playOnDevice={() => setIsModalOpen(true)}
        presetName={data.name}
      />

      {isModalOpen && (
        <Modal title="Play on device" onClose={() => setIsModalOpen(false)}>
          // TODO
        </Modal>
      )}
    </div>
  );
}
