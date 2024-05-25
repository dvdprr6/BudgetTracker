import { createSlice } from '@reduxjs/toolkit'
import { STATE } from '../types'
import {CATEGORY_GROUP_BY_WITH_TOTALS_SLICE, TCategoryGroupByWithTotalsDto} from '@budgettracker-utils'
import { getCategoryGroupByWithTotalsRecords } from './actions.ts'

const INITIAL_ALL_STATE: STATE<TCategoryGroupByWithTotalsDto[]> = {
  value: [],
  isLoading: false
}

export const categoryGroupByWithTotalsSlice = createSlice({
  name: CATEGORY_GROUP_BY_WITH_TOTALS_SLICE,
  initialState: INITIAL_ALL_STATE,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(getCategoryGroupByWithTotalsRecords.pending, state => {
      state.isLoading = true
    }).addCase(getCategoryGroupByWithTotalsRecords.fulfilled, (state, action) =>{
      state.value = action.payload
      state.isLoading = false
    }).addCase(getCategoryGroupByWithTotalsRecords.rejected, state => {
      state.value = []
      state.isLoading = false
    })
  }
})

