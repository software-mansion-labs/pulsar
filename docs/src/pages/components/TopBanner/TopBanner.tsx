import styles from './TopBanner.module.scss';

import swmLogo from '../../../assets/swm-logo.svg';
import { Button } from '../Button/Button';

export function TopBanner() {
  return(<div className={styles.banner}>

    <div className={styles.leftBar}>

      <div className={styles.authors}>
        <span>Created by</span>
        <img src={swmLogo.src} />
      </div>

      <div className={styles.header}>
        <div className={styles.title}>
          Rich and ready-to use haptics library
        </div>
        <div className={styles.subtitle}>
          Presets that you can hear and feel with Live Preview.
        </div>
      </div>

      <div className={styles.buttonHolder}>
        <Button label='Preset playground' />
        <Button label='Read the docs' variant='filled' />
      </div>

    </div>

    <div className={styles.rightBar}>

      <div className={styles.circle}>
        <div className={styles.circle}>
          <div className={styles.circle}>
            <div className={styles.circle}></div>
          </div>
        </div>
      </div>

      <div className={styles.phoneBackground}>
        <div className={styles.phone}>
          <div className={styles.notch}></div>
          <div className={styles.buttonHolder}>

            <div className={styles.row}>
              <div className={styles.buttonBackground}>
                <div className={styles.button}>
                  <div className={styles.emoji}></div>
                </div>
              </div>
              <div className={styles.buttonBackground}>
                <div className={styles.button}>
                  <div className={styles.emoji}></div>
                </div>
              </div>
            </div>

            <div className={styles.row}>
              <div className={styles.buttonBackground}>
                <div className={styles.button}>
                  <div className={styles.emoji}></div>
                </div>
              </div>
              <div className={styles.buttonBackground}>
                <div className={styles.button}>
                  <div className={styles.emoji}></div>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>

      <div className={styles.soundBanner}>
        <div className={styles.text}>
          Keep you sound on for the best experience
        </div>
        <div className={styles.icon}>
          <img src='' />
        </div>
      </div>
      
    </div>


  </div>)
}
