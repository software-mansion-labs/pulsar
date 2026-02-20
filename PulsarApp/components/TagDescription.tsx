import { StyleSheet, View } from 'react-native';
import { ThemedText } from './themed-text';
import { useThemeColor } from '@/hooks/use-theme-color';

interface Props {
  name: string;
  description: string;
  usage: string;
}

export function TagDescription({ name, description, usage }: Props) {
  const backgroundColor = useThemeColor({ light: '#E8F5FB', dark: '#1a2a3a' }, 'background');

  return (
    <View style={[styles.container, { backgroundColor }]}>
      <View style={styles.tag}>
        <ThemedText style={styles.tagText}>{name}</ThemedText>
      </View>
      <ThemedText style={styles.description}>{description}</ThemedText>
      <ThemedText style={styles.usage}>Usage: {usage}</ThemedText>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 20,
    borderRadius: 4,
    marginBottom: 16,
  },
  tag: {
    borderWidth: 2,
    borderRadius: 4,
    borderColor: '#38ACDD',
    boxShadow: '-2px 2px 0px #38ACDD',
    paddingHorizontal: 16,
    paddingVertical: 8,
    alignSelf: 'flex-start',
    marginBottom: 12,
  },
  tagText: {
    fontSize: 18,
    fontWeight: '500',
  },
  description: {
    fontSize: 16,
    lineHeight: 28,
    marginBottom: 12,
    fontWeight: '500',
  },
  usage: {
    lineHeight: 28,
  },
});
