import { createAsyncThunk } from '@reduxjs/toolkit'
import {
  GET_CATEGORY_RECORDS,
  CATEGORY_API_URL,
  GET_CATEGORY_GROUP_BY_WITH_TOTALS_RECORDS,
  CATEGORY_GROUP_BY_WITH_TOTALS_API_URL
} from '@budgettracker-utils'

export const getCategoryRecords = createAsyncThunk(GET_CATEGORY_RECORDS, async () => {
  const response = await fetch(CATEGORY_API_URL, {
    method: 'GET',
    credentials: 'same-origin',
    headers: { 'Content-Type':'application/json' }
  })

  if(response.status === 200){
    const body = await response.json()
    return body
  }else{
    return ''
  }
})

export const getCategoryGroupByWithTotalsRecords = createAsyncThunk(GET_CATEGORY_GROUP_BY_WITH_TOTALS_RECORDS, async () => {
  const response = await fetch(CATEGORY_GROUP_BY_WITH_TOTALS_API_URL, {
    method: 'GET',
    credentials: 'same-origin',
    headers: { 'Content-Type':'application/json' }
  })

  if(response.status === 200){
    const body = await response.json()
    return body
  }else{
    return ''
  }
})