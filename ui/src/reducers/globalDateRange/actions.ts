import { createAsyncThunk } from '@reduxjs/toolkit'
import {SET_GLOBAL_DATE_RANGE, RESET_GLOBAL_DATE_RANGE, TDateRangePicker } from '@budgettracker-utils'

export const setGlobalDateRange = createAsyncThunk(SET_GLOBAL_DATE_RANGE, (globalDateRange: TDateRangePicker) => {
  return globalDateRange
})

export const resetGlobalDateRange = createAsyncThunk(RESET_GLOBAL_DATE_RANGE, () => {})