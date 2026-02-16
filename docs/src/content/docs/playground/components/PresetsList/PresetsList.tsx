import style from './PresetsList.module.scss';
import infoIcon from '../../../assets/new_assets/info.svg'
import { Filters } from '../Filters/Filters';
import { Preset } from '../Preset/Preset';
import { useState } from 'react';
import type { PresetProps } from '../Preset/types';
import { TagsModal } from '../TagsModal/TagsModal';

const presets: Array<PresetProps> = [
  {
    name: '🧱 Falling Bricks',
    shortName: 'FallingBricks',
    description: "That feeling when some bricks fall onto your foot!",
    tags: [
      { label: "Short", variant: "blue" },
      { label: "Happy", variant: "blue" }
    ]
  },
  {
    name: '🧱 Falling Bricks 2',
    shortName: 'FallingBricks',
    description: "That feeling when some bricks fall onto your foot!",
    tags: [
      { label: "Short", variant: "blue" },
      { label: "Happy", variant: "blue" }
    ]
  },
];

export function PresetsList() {
  const [showModal, setShowModal] = useState<boolean>(false);
  
  return <div className={['not-content', style.presets].join(' ')}>

    <div className={style.header}>
      <div className={style.title}>Presets</div>
      <div className={style.info} onClick={() => setShowModal(true)}>
        <div>Learn more about tags</div>
        <img src={infoIcon.src} />
      </div>
    </div>

    <Filters />

    {presets.map((preset, index) => (
      <Preset key={index} {...preset} />
    ))}
    
    {showModal && (
      <TagsModal onClose={() => setShowModal(false)} />
    )}
  </div>
}
