import style from './Modal.module.scss';
import xIcon from '../../../assets/new_assets/x.svg';

interface Props {
  title: string;
  children: React.ReactNode
  onClose: () => void;
}

export function Modal({ title, children, onClose }: Props) {
  return (
    <div className={style.overlay} onClick={onClose}>
      <div className={style.modal} onClick={(e) => e.stopPropagation()}>
        <div className={style.header}>
          <h2 className={style.title}>{title}</h2>
          <button className={style.closeButton} onClick={onClose}>
            <img src={xIcon.src} alt="Close" />
          </button>
        </div>
        <div className={style.content}>
          {children}
        </div>
      </div>
    </div>
  );
}
