import style from './Point.module.scss';

interface PointProps {
  index: number;
  children: React.ReactNode;
}

export function Point({ index, children }: PointProps) {
  return (
    <div className={style.point}>
      <div className={style.index}>{index}</div>
      <div className={style.pointContent}>{children}</div>
    </div>
  );
}
