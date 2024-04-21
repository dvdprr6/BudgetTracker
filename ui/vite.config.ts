import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'
//import tsconfigPaths from "vite-tsconfig-paths";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  base: '/BudgetTracker',
  resolve: {
    alias: {
      '@budgettracker-components-home': path.resolve(__dirname, './src/components/home'),
      '@budgettracker-components-navigation': path.resolve(__dirname, './src/components/navigation'),
      '@budgettracker-components-dashboard': path.resolve(__dirname, './src/components/dashboard'),
      '@budgettracker-utils': path.resolve(__dirname, './src/utils')
    }
  }
})