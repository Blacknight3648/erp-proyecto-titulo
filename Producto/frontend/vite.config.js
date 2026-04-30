/// <reference types="vitest" />
import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [tailwindcss(), react()],
  define: {
    'process.env': {},
    'global': 'globalThis',
  },
  resolve: {
    alias: {
      'xlsx-js-style': 'xlsx-js-style/dist/xlsx.min.js',
      'stream': fileURLToPath(new URL('./src/utils/stream-polyfill.js', import.meta.url)),
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
