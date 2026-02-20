import React, { useMemo } from 'react';
import { View, StyleSheet } from 'react-native';
import { PresetsConfig } from '@/constants/PresetsConfig';
import { TagsInfo } from '@/constants/Tags';
import { useFilters } from '@/contexts/FilterContext';
import Preset from './Preset';

export default function PresetList() {
  const { selectedTags } = useFilters();

  const selectedTagsByGroup = useMemo(() => {
    const grouped: Record<string, string[]> = {};
    
    selectedTags.forEach(tagName => {
      TagsInfo.forEach(group => {
        const tagExists = group.tags.some(tag => tag.name === tagName);
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
      return PresetsConfig;
    }
    
    return PresetsConfig.filter(preset => {
      const presetTagLabels = preset.tags.map(tag => tag.label);
      
      for (const groupName in selectedTagsByGroup) {
        const selectedTagsInGroup = selectedTagsByGroup[groupName];
        const hasTagFromGroup = selectedTagsInGroup.some(tagName => 
          presetTagLabels.includes(tagName)
        );
        if (!hasTagFromGroup) {
          return false;
        }
      }
      
      return true;
    });
  }, [selectedTags, selectedTagsByGroup]);

  return (
    <View style={styles.container}>
      {filteredPresets.map((preset, index) => (
        <Preset
          key={`${preset.shortName}-${index}`}
          title={preset.name}
          subtitle={preset.description}
          tags={preset.tags.map((tag) => ({
            label: tag.label,
          }))}
          image={preset.image}
          onPress={preset.play}
          duration={preset.duration}
        />
      ))}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    gap: 30,
    marginTop: 20,
    paddingBottom: 50,
  },
});
