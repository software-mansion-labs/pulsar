import style from './Preset.module.scss';

interface Props {
  index?: number;
  children?: React.ReactNode;
}

export function Preset({ index, children }: Props) {
  return <div className={style.NAME}>
  </div>
}
