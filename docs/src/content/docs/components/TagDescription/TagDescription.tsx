import style from './TagDescription.module.scss';

interface Props {
  name: string;
  description: string;
  usage: string;
}

export function TagDescription({ name, description, usage }: Props) {
  return (
    <div className={style.tagDescription}>
      <div className={style.tag}>{name}</div>
      <div className={style.description}>{description}</div>
      <div className={style.usage}>Usage: {usage}</div>
    </div>
  );
}
