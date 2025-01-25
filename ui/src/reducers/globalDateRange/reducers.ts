import { createSlice } from '@reduxjs/toolkit'
import { STATE } from '../types'
import { TDateRangePicker, GLOBAL_DATE_RANGE_SLICE } from '@budgettracker-utils'
import moment from 'moment'
import { setGlobalDateRange, resetGlobalDateRange } from './actions'

const INITIAL_STATE: STATE<TDateRangePicker> = {
  value: { dateRange: [moment().startOf('month'), moment().endOf('month')] },
  isLoading: false
}

export const globalDateRangeSlice = createSlice({
  name: GLOBAL_DATE_RANGE_SLICE,
  initialState: INITIAL_STATE,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(setGlobalDateRange.pending, state => {
      state.isLoading = true
    }).addCase(setGlobalDateRange.fulfilled, (state, action) => {
      state.value = action.payload
      state.isLoading = false
    }).addCase(setGlobalDateRange.rejected, state => {
      state.value = INITIAL_STATE.value
      state.isLoading = false
    }).addCase(resetGlobalDateRange.pending, state => {
      state.isLoading = true
    }).addCase(resetGlobalDateRange.fulfilled, state => {
      state.value = INITIAL_STATE.value
      state.isLoading = false
    }).addCase(resetGlobalDateRange.rejected, state => {
      state.value = INITIAL_STATE.value
      state.isLoading = false
    })
  }
})