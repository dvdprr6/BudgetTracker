import { createAsyncThunk } from '@reduxjs/toolkit'
import { GET_CASH_FLOW_RECORDS, CASH_FLOW_API_URL } from '@budgettracker-utils'
import 'cross-fetch/polyfill'

export const getCashFlowRecords = createAsyncThunk(GET_CASH_FLOW_RECORDS, async () => {
  const response = await fetch(CASH_FLOW_API_URL, {
    method: 'GET',
    credentials: 'same-origin',
    headers: { 'Content-Type':'application/json' }
  })

  if(response.status === 200){
    return await response.json()
  }else{
    return ''
  }
})