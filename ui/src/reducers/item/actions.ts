import { createAsyncThunk } from '@reduxjs/toolkit'
import {
  GET_ITEM_BY_CATEGORY_RECORDS,
  GET_ITEM_RECORDS,
  ITEM_API_URL,
  ITEM_BY_CATEGORY_API_URL
} from '@budgettracker-utils'

export const getItemRecords = createAsyncThunk(GET_ITEM_RECORDS, async () => {
  const response = await fetch(ITEM_API_URL, {
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

export const getItemsByCategoryIdRecords = createAsyncThunk(GET_ITEM_BY_CATEGORY_RECORDS, async (categoryId: string) => {
  const response = await fetch(ITEM_BY_CATEGORY_API_URL + '/' + categoryId, {
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