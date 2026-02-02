import style from './PresetsList.module.scss';
import infoIcon from '../../../assets/new_assets/info.svg'
import { Filters } from '../Filters/Filters';
import { Preset } from '../Preset/Preset';
import { Modal } from '../Modal/Modal';
import { useState } from 'react';
import { Tab, Tabs } from '../Tabs/Tabs';
import { TagDescription } from '../TagDescription/TagDescription';

export function PresetsList() {
  const [showModal, setShowModal] = useState<boolean>(true);
  
  return <div className={['not-content', style.presets].join(' ')}>

    <div className={style.header}>
      <div className={style.title}>Presets</div>
      <div className={style.info} onClick={() => setShowModal(true)}>
        <div>Learn more about tags</div>
        <img src={infoIcon.src} />
      </div>
    </div>

    <Filters />

    <Preset
      name='🧱 Falling Bricks'
      shortName='FallingBricks'
      description="That feeling when some bricks fall onto your foot!"
      tags={[
        { label: "Short", variant: "blue" },
        { label: "Happy", variant: "blue" }
      ]} 
    />
    <Preset
      name='🧱 Falling Bricks'
      shortName='FallingBricks'
      description="That feeling when some bricks fall onto your foot!"
      tags={[
        { label: "Short", variant: "blue" },
        { label: "Happy", variant: "blue" }
      ]} 
    />
    <Preset
      name='🧱 Falling Bricks'
      shortName='FallingBricks'
      description="That feeling when some bricks fall onto your foot!"
      tags={[
        { label: "Short", variant: "blue" },
        { label: "Happy", variant: "blue" }
      ]} 
    />
    {showModal && (
      <Modal title="Connection guide" onClose={() => setShowModal(false)}>
        <Tabs defaultTab={0}>
          <Tab name="Swift">
            <div className={style.elementsGap}>
              <TagDescription 
                name="Super short"
                description="Very short haptics pattern with duration less than 100 ms."
                usage="Good as a reaction for tap events."
              />
            </div>
          </Tab>
          <Tab name="React Native">
            <div>Your content here2</div>
          </Tab>
          <Tab name="Android">
            <div>Your content here3</div>
          </Tab>
        </Tabs>
      </Modal>
    )}
  </div>
}
