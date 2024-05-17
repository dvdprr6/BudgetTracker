import { createAsyncThunk } from '@reduxjs/toolkit'
import { GET_CASH_FLOW_RECORDS, CASH_FLOW_API_URL } from '@budgettracker-utils'
import 'cross-fetch/polyfill'

export const getCashFlowRecords = createAsyncThunk(GET_CASH_FLOW_RECORDS, async () => {
  const response = await fetch(CASH_FLOW_API_URL, {
    method: 'GET',
    credentials: 'same-origin',
    headers: { 'Content-Type':'application/json' , 'Accept': '*/*' }
  })

  if(response.status === 200){
    const body = await response.json()
    return body
  }else{
    return ''
  }
})