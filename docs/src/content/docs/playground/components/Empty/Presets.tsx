import style from './Point.module.scss';

interface Props {
  index: number;
  children: React.ReactNode;
}

export function Presets({ index, children }: Props) {
  return <div className={style.NAME}>
  </div>
}
