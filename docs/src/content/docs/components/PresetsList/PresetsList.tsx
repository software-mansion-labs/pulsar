import style from './PresetsList.module.scss';
import infoIcon from '../../assets/new_assets/info.svg';
import { Filters } from '../Filters/Filters';
import { Preset } from '../Preset/Preset';
import { useState, useMemo } from 'react';
import { TagsModal } from '../TagsModal/TagsModal';
import { TagsInfo } from './Tags';
import { PresetsConfig } from '../../assets/presets/PresetsConfig';
import { AndroidPresetsConfig, IOSPresetsConfig } from '../../assets/systemPresets/SystemPresetsConfig';
import { NoResult } from '../NoResult/NoResult';
import { ChartModal } from '../ChartModal/ChartModal';

declare global {
  interface Window {
    posthog?: {
      capture: (event: string, properties?: Record<string, unknown>) => void;
    };
  }
}

export function PresetsList() {
  const [showModal, setShowModal] = useState<'no' | 'tags' | 'chart'>('no');
  const [selectedTags, setSelectedTags] = useState<string[]>([]);
  const [showSystemPresets, setShowSystemPresets] = useState(false);

  const activePresets = showSystemPresets
    ? [...IOSPresetsConfig, ...AndroidPresetsConfig]
    : PresetsConfig;

  function handleSetSelectedTags(tags: string[] | ((tags: string[]) => string[])) {
    if (typeof tags === 'function') {
      setSelectedTags(tags);
    } else {
      setSelectedTags(tags);
      if (tags.length > 0) {
        window.posthog?.capture('preset_filter_applied', {
          selected_tags: tags,
          tag_count: tags.length,
        });
      }
    }
  }
  const selectedTagsByGroup = useMemo(() => {
    const grouped: Record<string, string[]> = {};

    selectedTags.forEach((tagName) => {
      TagsInfo.forEach((group) => {
        const tagExists = group.tags.some((tag) => tag.name === tagName);
        if (tagExists) {
          if (!grouped[group.groupName]) {
            grouped[group.groupName] = [];
          }
          grouped[group.groupName].push(tagName);
        }
      });
    });

    return grouped;
  }, [selectedTags]);

  const filteredPresets = useMemo(() => {
    if (selectedTags.length === 0) {
      return activePresets;
    }

    return activePresets.filter((preset) => {
      const presetTagLabels = preset.data.tags;

      for (const groupName in selectedTagsByGroup) {
        const selectedTagsInGroup = selectedTagsByGroup[groupName];
        const hasTagFromGroup = selectedTagsInGroup.some((tagName) =>
          presetTagLabels.includes(tagName),
        );
        if (!hasTagFromGroup) {
          return false;
        }
      }

      return true;
    });
  }, [selectedTags, selectedTagsByGroup, activePresets]);

  return (
    <div className={['not-content', style.presets].join(' ')}>
      <div className={style.header}>
        <div className={style.title}>Presets</div>
        <div className={style.info}>
          <div className={style.inner} onClick={() => setShowModal('tags')}>
            <div>Learn more about tags</div>
            <img src={infoIcon.src} />
          </div>
          <div className={style.inner} onClick={() => setShowModal('chart')}>
            <div>Learn more about chart visualization</div>
            <img src={infoIcon.src} />
          </div>
        </div>
      </div>

      <Filters
        selectedTags={selectedTags}
        setSelectedTags={handleSetSelectedTags}
        showSystemPresets={showSystemPresets}
        setShowSystemPresets={setShowSystemPresets}
      />

      {filteredPresets.length > 0 && (
        <div className={style.resultCount}>{filteredPresets.length} results</div>
      )}

      {filteredPresets.length === 0 && <NoResult />}

      {filteredPresets.map((preset, index) => (
        <Preset key={index} {...preset} />
      ))}

      {showModal === 'tags' && <TagsModal onClose={() => setShowModal('no')} />}
      {showModal === 'chart' && <ChartModal onClose={() => setShowModal('no')} />}
    </div>
  );
}
