import React from 'react';
import { Image, ScrollView, StyleSheet, Text, View, type ImageSourcePropType } from 'react-native';

import Card from './Card';
import { ThemedText } from './themed-text';
import { Colors, Fonts } from '@/constants/theme';

export type PresetTag = {
	label: string;
	backgroundColor?: string;
	textColor?: string;
};

export interface PresetProps {
	title: string;
	subtitle?: string;
	tags?: PresetTag[];
	images?: ImageSourcePropType[];
}

function Preset({ title, subtitle, tags = [], images = [] }: PresetProps) {
	return (
		<Card>
			<View style={styles.container}>
				{tags.length > 0 ? (
					<View style={styles.tagsContainer}>
						{tags.map((tag, index) => (
							<View
								key={`${tag.label}-${index}`}
								style={[
									styles.tag,
									tag.backgroundColor ? { backgroundColor: tag.backgroundColor } : undefined,
								]}
							>
								<Text
									style={[
										styles.tagText,
										tag.textColor ? { color: tag.textColor } : undefined,
									]}
								>
									{tag.label}
								</Text>
							</View>
						))}
					</View>
				) : null}

				<ThemedText type="subtitle" style={styles.title}>
					{title}
				</ThemedText>
				{subtitle ? (
					<ThemedText style={styles.subtitle}>{subtitle}</ThemedText>
				) : null}

				<ScrollView
					horizontal
					showsHorizontalScrollIndicator={false}
					contentContainerStyle={styles.imagesContainer}
					style={styles.imagesScroll}
				>
					{images.length > 0 ? (
						images.map((source, index) => (
							<Image key={`preset-image-${index}`} source={source} style={styles.image} />
						))
					) : (
						<View style={styles.imagePlaceholder}>
							<Text style={styles.placeholderText}>No preview</Text>
						</View>
					)}
				</ScrollView>
			</View>
		</Card>
	);
}

const styles = StyleSheet.create({
	container: {
		gap: 12,
	},
	tagsContainer: {
		flexDirection: 'row',
		flexWrap: 'wrap',
		gap: 8,
	},
	tag: {
		borderRadius: 999,
		borderWidth: 1,
		borderColor: Colors.light.borderColor,
		backgroundColor: '#E8F6FB',
		paddingHorizontal: 10,
		paddingVertical: 4,
	},
	tagText: {
		fontFamily: Fonts.sans,
		fontSize: 12,
		color: Colors.light.text,
	},
	title: {
		marginTop: 6,
	},
	subtitle: {
		color: '#2B85AB',
	},
	imagesScroll: {
		marginTop: 8,
	},
	imagesContainer: {
		gap: 12,
		paddingBottom: 2,
	},
	image: {
		width: 240,
		height: 120,
		borderRadius: 10,
		borderWidth: 1,
		borderColor: Colors.light.borderColor,
	},
	imagePlaceholder: {
		width: 240,
		height: 120,
		borderRadius: 10,
		borderWidth: 1,
		borderColor: Colors.light.borderColor,
		backgroundColor: '#F3FAFD',
		alignItems: 'center',
		justifyContent: 'center',
	},
	placeholderText: {
		fontFamily: Fonts.sans,
		fontSize: 12,
		color: Colors.light.text,
	},
});

export default Preset;
