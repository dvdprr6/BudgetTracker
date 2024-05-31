import { createAsyncThunk } from '@reduxjs/toolkit'
import { GET_ITEM_BY_CATEGORY_RECORDS, ITEM_BY_CATEGORY_API_URL } from '@budgettracker-utils'

export const getItemsByCategoryIdRecords = createAsyncThunk(GET_ITEM_BY_CATEGORY_RECORDS, async (categoryId) => {
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