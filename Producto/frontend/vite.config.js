/// <reference types="vitest" />
import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
  envDir: '../',
  plugins: [tailwindcss(), react()],
  define: {
    'process.env': {},
    'global': 'globalThis',
  },
  resolve: {
    alias: {
      '@':           fileURLToPath(new URL('./src', import.meta.url)),
      '@layouts':    fileURLToPath(new URL('./src/components/layout', import.meta.url)),
      '@pages':      fileURLToPath(new URL('./src/pages', import.meta.url)),
      '@components': fileURLToPath(new URL('./src/components', import.meta.url)),
      '@ui':         fileURLToPath(new URL('./src/ui', import.meta.url)),
      '@hooks':      fileURLToPath(new URL('./src/hooks', import.meta.url)),
      '@contexts':   fileURLToPath(new URL('./src/contexts', import.meta.url)),
      '@services':   fileURLToPath(new URL('./src/remote/service', import.meta.url)),
      '@remote':     fileURLToPath(new URL('./src/remote', import.meta.url)),
      '@utils':      fileURLToPath(new URL('./src/utils', import.meta.url)),
      '@types':      fileURLToPath(new URL('./src/types', import.meta.url)),
      '@data':       fileURLToPath(new URL('./src/data', import.meta.url)),
      'xlsx-js-style': 'xlsx-js-style/dist/xlsx.min.js',
      'stream':      fileURLToPath(new URL('./src/utils/stream-polyfill.js', import.meta.url)),
    }
  },
  server: {
    port: 5173,
    strictPort: true,
    open: true,
  },
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: './src/setupTests.js',
  },
})
