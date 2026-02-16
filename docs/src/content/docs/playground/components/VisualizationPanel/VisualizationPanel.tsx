import styles from './VisualizationPanel.module.scss';
import phoneIcon from '../../../assets/new_assets/phone.svg';
import playIcon from '../../../assets/new_assets/play.svg';
import { useState, useEffect } from 'react';

interface VisualizationPanelProps {
  image: {
    src: string;
  };
  duration?: number; // Duration in milliseconds
  onPlayClick: () => void;
  onRecordClick: () => void;
  className?: string;
}

export function VisualizationPanel({
  image,
  duration = 1000,
  onPlayClick,
  onRecordClick,
  className = '',
}: VisualizationPanelProps) {
  const [isPlaying, setIsPlaying] = useState(false);

  const handlePlayClick = () => {
    setIsPlaying(false);
    if (!isPlaying) {
      setTimeout(() => setIsPlaying(true), 10);
    }
    onPlayClick();
  };

  useEffect(() => {
    if (isPlaying) {
      const timer = setTimeout(() => {
        setIsPlaying(false);
      }, duration);
      return () => clearTimeout(timer);
    }
  }, [isPlaying, duration]);

  return (
    <div className={`${styles.visualizationPanel} ${className}`}>
      <div className={styles.visualization}>
        <img src={image.src} alt="visualization" className={styles.image} />
        {isPlaying && (
          <div 
            className={styles.indicator} 
            style={{ animationDuration: `${duration}ms` }}
          />
        )}
      </div>

      <div className={styles.controls}>
        <div
          className={styles.controlButton}
          onClick={handlePlayClick}
        >
          <img src={playIcon.src} />
        </div>

        <div
          className={styles.controlButton}
          onClick={onRecordClick}
          title="Record"
          aria-label="Record"
        >
          <img src={phoneIcon.src} alt="Play vibration on device" />
        </div>
      </div>
    </div>
  );
}
