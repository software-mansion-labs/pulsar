import styles from './Checkbox.module.scss';
import checkIcon from '../../assets/new_assets/check.svg';

interface CheckboxProps {
  id: string;
  label: string;
  checked: boolean;
  onChange: (id: string, checked: boolean) => void;
}

export function Checkbox({ id, label, checked, onChange }: CheckboxProps) {
  return (
    <label className={styles.checkboxLabel} onClick={() => onChange(id, !checked)}>
      <div className={`${styles.checkbox} ${checked ? styles.checked : ''}`}>
        {checked && <img src={checkIcon.src} className={styles.checkmark} />}
      </div>
      <span className={styles.label}>{label}</span>
    </label>
  );
}
