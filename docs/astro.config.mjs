// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';
import react from '@astrojs/react';

export default defineConfig({
  vite: {
    css: {
      modules: {
        localsConvention: 'camelCase',
        generateScopedName: '[name]__[local]',
      },
    },
    ssr: {
      external: ['react', 'react-dom'],
    },
  },
  integrations: [starlight({
    title: 'Pulsar',
    customCss: [
      './src/pages/index.css',
      './src/content/docs/custom.css',
      '@fontsource/bebas-neue/400.css',
    ],
    pagination: false,
    social: [{ icon: 'github', label: 'GitHub', href: 'https://github.com/swmansion/pulsar' }],
    sidebar: [
      {
        label: 'Getting started',
        slug: 'getting-started'
      },
    ],
    logo: {
      light: './src/assets/logo_label.svg',
      dark: './src/assets/logo_label.svg',
      alt: 'Pulsar Logo',
      replacesTitle: true,
    },
    components: {
      ThemeSelect: './src/components/ThemeSelect.astro',
    },
  }),
  react()
],
});