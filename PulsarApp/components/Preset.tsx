import React from 'react';
import { Image, ScrollView, StyleSheet, Text, View, type ImageSourcePropType } from 'react-native';

import Card from './Card';
import { ThemedText } from './themed-text';
import { Colors, Fonts } from '@/constants/theme';
import Button from './Button';

const IMAGE_HEIGHT = 160;

export type PresetTag = {
	label: string;
};

export interface PresetProps {
	title: string;
	subtitle: string;
	tags: PresetTag[];
	image: ImageSourcePropType;
	onPress: () => void;
}

function Preset({ title, subtitle, tags = [], image, onPress }: PresetProps) {
	const imageMeta = Image.resolveAssetSource(image);
	const imageAspectRatio =
		imageMeta?.width && imageMeta?.height ? imageMeta.width / imageMeta.height : undefined;
	const imageWidth = imageAspectRatio ? IMAGE_HEIGHT * imageAspectRatio : undefined;

	return (
		<Card style={styles.card}>
			<View style={styles.container}>
				<View style={styles.tagsContainer}>
					{tags.map((tag, index) => (
						<View
							key={`${tag.label}-${index}`}
							style={styles.tag}
						>
							<Text style={styles.tagText}>{tag.label}</Text>
						</View>
					))}
				</View>

				<ThemedText type="subtitle" style={styles.title}>
					{title}
				</ThemedText>
				<ThemedText>{subtitle}</ThemedText>

				<View style={styles.border}>
					<ScrollView
						horizontal
						bounces={false}
						style={styles.imagesScroll}
						contentContainerStyle={styles.imagesContent}
					>
						<Image
							source={image}
							style={[
								styles.image,
								imageWidth ? { width: imageWidth } : undefined,
							]}
							resizeMode="contain"
						/>
					</ScrollView>
				</View>

				<Button label='Play' onClick={onPress} />

			</View>
		</Card>
	);
}

const styles = StyleSheet.create({
	card: {
		paddingVertical: 15,
	},
	container: {
		gap: 5,
	},
	tagsContainer: {
		flexDirection: 'row',
		flexWrap: 'wrap',
		gap: 8,
	},
	tag: {
		borderRadius: 999,
		backgroundColor: '#B5E1F1',
		paddingHorizontal: 10,
		paddingVertical: 5,
	},
	tagText: {
		fontSize: 14,
		color: '#001A72',
		fontWeight: '500',
	},
	title: {
		marginTop: 6,
	},
	border: {
		borderWidth: 1,
		borderColor: '#E1F3FA',
		marginTop: 8,
	},
	imagesScroll: {
		height: IMAGE_HEIGHT + 20,
	},
	imagesContent: {
		height: IMAGE_HEIGHT + 20,
		alignItems: 'center',
	},
	image: {
		height: IMAGE_HEIGHT,
		alignSelf: 'center',
		paddingTop: 5,
		paddingBottom: 5,
		paddingHorizontal: 5,
	},
	placeholderText: {
		fontFamily: Fonts.sans,
		fontSize: 12,
		color: Colors.light.text,
	},
});

export default Preset;
