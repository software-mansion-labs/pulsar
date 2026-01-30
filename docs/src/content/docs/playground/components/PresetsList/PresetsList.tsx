import style from './PresetsList.module.scss';
import infoIcon from '../../../assets/new_assets/info.svg'
import { Filters } from '../Filters/Filters';
import { Preset } from '../Preset/Preset';

export function PresetsList() {
  return <div className={['not-content', style.presets].join(' ')}>

    <div className={style.header}>
      <div className={style.title}>Presets</div>
      <div className={style.info}>
        Learn more about tags
        <img src={infoIcon.src} />
      </div>
    </div>

    <Filters />

    <Preset />
    <Preset />
    <Preset />

  </div>
}
