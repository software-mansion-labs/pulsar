import styles from './Button.module.scss';

import arrowIcon from '../../../assets/landing-page/arrow-icon.svg';

export function Button(
  { label, url = '#', variant = 'unfilled' } 
  : { label: string, url: string, variant?: 'filled' | 'unfilled' }
) {
  return(<div className={styles.background}>
    <a className={[styles.innerHolder, variant === 'filled' ? styles.filled : styles.unfilled].join(' ')} href={url}>
      {label}
      <img src={arrowIcon.src} />
    </a>
  </div>)
}
