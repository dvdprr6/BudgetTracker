import { createSlice } from '@reduxjs/toolkit'
import { STATE } from '../types'
import { CATEGORY_SLICE, TCategoryDto } from '@budgettracker-utils'
import { getCategoryRecords } from './actions.ts'

const INITIAL_ALL_STATE: STATE<TCategoryDto[]> = {
  value: [],
  isLoading: false
}

export const categorySlice = createSlice({
  name: CATEGORY_SLICE,
  initialState: INITIAL_ALL_STATE,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(getCategoryRecords.pending, state => {
      state.isLoading = true
    }).addCase(getCategoryRecords.fulfilled, (state, action) => {
      state.value = action.payload
      state.isLoading = false
    }).addCase(getCategoryRecords.rejected, state => {
      state.value = []
      state.isLoading = false
    })
  }
})