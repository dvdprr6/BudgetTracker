import { STATE } from '@budgettracker-reducers'
import {ITEM_BY_CATEGORY_SLICE, TItemDto} from '@budgettracker-utils'
import { createSlice } from '@reduxjs/toolkit'
import { getItemsByCategoryIdRecords } from './actions'


const INITIAL_ALL_STATE: STATE<TItemDto[]> = {
  value: [],
  isLoading: false
}

export const itemsByCategorySlice = createSlice({
  name: ITEM_BY_CATEGORY_SLICE,
  initialState: INITIAL_ALL_STATE,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(getItemsByCategoryIdRecords.pending, state => {
      state.isLoading = true
    }).addCase(getItemsByCategoryIdRecords.fulfilled, (state, action) => {
      state.value = action.payload
      state.isLoading = false
    }).addCase(getItemsByCategoryIdRecords.rejected, state => {
      state.value = []
      state.isLoading = false
    })
  }
})