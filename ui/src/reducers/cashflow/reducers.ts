import { createSlice } from '@reduxjs/toolkit'
import { STATE } from '../types'
import { CASH_FLOW_SLICE, TCashFlowDto } from '@budgettracker-utils'
import { getCashFlowRecords } from './actions'

const INITIAL_ALL_STATE: STATE<TCashFlowDto[]> = {
  value: [],
  isLoading: false
}

export const cashFlowSlice = createSlice({
  name: CASH_FLOW_SLICE,
  initialState: INITIAL_ALL_STATE,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(getCashFlowRecords.pending, state => {
      state.isLoading = true
    }).addCase(getCashFlowRecords.fulfilled, (state, action) => {
      state.value = action.payload
      state.isLoading = false
    }).addCase(getCashFlowRecords.rejected, state => {
      state.value = []
      state.isLoading = false
    })
  }
})