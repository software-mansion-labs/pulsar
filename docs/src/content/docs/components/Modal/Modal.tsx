import { useEffect, useRef } from 'react';
import style from './Modal.module.scss';
import xIcon from '../../assets/new_assets/x.svg';

interface Props {
  title: string;
  children: React.ReactNode;
  onClose: () => void;
}

export function Modal({ title, children, onClose }: Props) {
  const dialogRef = useRef<HTMLDialogElement>(null);

  useEffect(() => {
    dialogRef.current?.showModal();
  }, []);

  function handleClick(e: React.MouseEvent<HTMLDialogElement>) {
    if (e.target === dialogRef.current) onClose();
  }

  return (
    <dialog
      ref={dialogRef}
      className={style.dialog}
      onClose={onClose}
      onClick={handleClick}
      aria-labelledby="modal-title"
    >
      <div className={style.header}>
        <h2 id="modal-title" className={style.title}>{title}</h2>
        <div className={style.closeButtonWrapper}>
          <button className={style.closeButton} onClick={onClose} aria-label="Close">
            <img src={xIcon.src} alt="" />
          </button>
        </div>
      </div>
      <div className={style.content}>{children}</div>
    </dialog>
  );
}
