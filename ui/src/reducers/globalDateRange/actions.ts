import { createAsyncThunk } from '@reduxjs/toolkit'
import { SET_GLOBAL_DATE_RANGE, RESET_GLOBAL_DATE_RANGE } from '@budgettracker-utils'
import moment from 'moment/moment'
import { DateRange } from '@mui/x-date-pickers-pro'

export const setGlobalDateRange = createAsyncThunk(SET_GLOBAL_DATE_RANGE, (globalDateRange: DateRange<moment.Moment>) => {
  return { dateRange : globalDateRange }
})

export const resetGlobalDateRange = createAsyncThunk(RESET_GLOBAL_DATE_RANGE, () => {})