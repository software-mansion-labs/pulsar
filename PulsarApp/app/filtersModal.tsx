import { router } from 'expo-router';
import React, { useState } from 'react';
import { StyleSheet, View, Text, Pressable, ScrollView } from 'react-native';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import Button from '@/components/Button';
import { Colors } from '@/constants/theme';

type FilterState = {
  [key: string]: boolean;
};

export default function FiltersModal() {
  const [length, setLength] = useState<FilterState>({
    '1': true,
    '2': false,
    '3': true,
    '4': true,
    '5': true,
  });

  const [complexity, setComplexity] = useState<FilterState>({
    '1': true,
    '2': false,
    '3': true,
    '4': true,
  });

  const [intensity, setIntensity] = useState<FilterState>({
    '1': false,
    '2': false,
    '3': false,
    '4': true,
    '5': false,
  });

  const [feedback, setFeedback] = useState<FilterState>({
    '1': false,
    '2': false,
    '3': false,
    '4': true,
  });

  const toggleFilter = (
    category: 'length' | 'complexity' | 'intensity' | 'feedback',
    value: string
  ) => {
    const setters = {
      length: setLength,
      complexity: setComplexity,
      intensity: setIntensity,
      feedback: setFeedback,
    };

    const setter = setters[category];
    setter((prev) => ({ ...prev, [value]: !prev[value] }));
  };

  const clearAll = () => {
    setLength({ '1': false, '2': false, '3': false, '4': false, '5': false });
    setComplexity({ '1': false, '2': false, '3': false, '4': false });
    setIntensity({ '1': false, '2': false, '3': false, '4': false, '5': false });
    setFeedback({ '1': false, '2': false, '3': false, '4': false });
  };

  const handleShowResults = () => {
    // Handle showing results with selected filters
    router.back();
  };

  return (
    <ThemedView style={styles.container}>
      <View style={styles.header}>
        <Pressable onPress={() => router.back()} style={styles.closeButton}>
          <Text style={styles.closeIcon}>✕</Text>
        </Pressable>
        <ThemedText style={styles.title}>Filters</ThemedText>
        <Pressable onPress={clearAll}>
          <Text style={styles.clearAll}>Clear all</Text>
        </Pressable>
      </View>

      <ScrollView style={styles.scrollView} showsVerticalScrollIndicator={true}>
        <FilterSection
          title="Length"
          filters={length}
          onToggle={(value) => toggleFilter('length', value)}
          options={['1', '2', '3', '4', '5']}
        />

        <FilterSection
          title="Complexity"
          filters={complexity}
          onToggle={(value) => toggleFilter('complexity', value)}
          options={['1', '2', '3', '4']}
        />

        <FilterSection
          title="Intensity"
          filters={intensity}
          onToggle={(value) => toggleFilter('intensity', value)}
          options={['1', '2', '3', '4', '5']}
        />

        <FilterSection
          title="Feedback"
          filters={feedback}
          onToggle={(value) => toggleFilter('feedback', value)}
          options={['1', '2', '3', '4']}
        />
      </ScrollView>

      <View style={styles.footer}>
        <Button 
          label="Show results" 
          onPress={handleShowResults}
          style={styles.showResultsButton}
        />
      </View>
    </ThemedView>
  );
}

interface FilterSectionProps {
  title: string;
  filters: FilterState;
  onToggle: (value: string) => void;
  options: string[];
}

function FilterSection({ title, filters, onToggle, options }: FilterSectionProps) {
  return (
    <View style={styles.section}>
      <ThemedText style={styles.sectionTitle}>{title}</ThemedText>
      <View style={styles.optionsGrid}>
        {options.map((option) => (
          <CheckboxItem
            key={option}
            label={`${title} ${option}`}
            checked={filters[option]}
            onToggle={() => onToggle(option)}
          />
        ))}
      </View>
    </View>
  );
}

interface CheckboxItemProps {
  label: string;
  checked: boolean;
  onToggle: () => void;
}

function CheckboxItem({ label, checked, onToggle }: CheckboxItemProps) {
  return (
    <Pressable style={styles.checkboxContainer} onPress={onToggle}>
      <View style={[styles.checkbox, checked && styles.checkboxChecked]}>
        {checked && <Text style={styles.checkmark}>✓</Text>}
      </View>
      <Text style={styles.checkboxLabel}>{label}</Text>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FAFAFA',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 20,
    paddingTop: 20,
    paddingBottom: 20,
    backgroundColor: 'white',
  },
  closeButton: {
    width: 40,
    height: 40,
    alignItems: 'center',
    justifyContent: 'center',
  },
  closeIcon: {
    fontSize: 24,
    color: Colors.light.text,
    fontWeight: '300',
  },
  title: {
    fontSize: 20,
    fontWeight: '600',
    color: Colors.light.text,
  },
  clearAll: {
    fontSize: 16,
    color: '#007AFF',
    fontWeight: '400',
  },
  scrollView: {
    flex: 1,
    paddingHorizontal: 20,
  },
  section: {
    marginTop: 30,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '600',
    color: Colors.light.text,
    marginBottom: 16,
  },
  optionsGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 12,
  },
  checkboxContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    width: '48%',
    marginBottom: 12,
  },
  checkbox: {
    width: 24,
    height: 24,
    borderRadius: 6,
    borderWidth: 2,
    borderColor: Colors.light.borderColor,
    backgroundColor: 'white',
    alignItems: 'center',
    justifyContent: 'center',
    marginRight: 10,
  },
  checkboxChecked: {
    backgroundColor: Colors.light.borderColor,
    borderColor: Colors.light.borderColor,
  },
  checkmark: {
    color: 'white',
    fontSize: 16,
    fontWeight: 'bold',
  },
  checkboxLabel: {
    fontSize: 16,
    color: Colors.light.text,
  },
  footer: {
    padding: 20,
    paddingBottom: 40,
    backgroundColor: 'white',
  },
  showResultsButton: {
    width: '100%',
  },
});
