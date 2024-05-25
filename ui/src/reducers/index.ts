import { configureStore, combineReducers } from '@reduxjs/toolkit'
import { connect, ConnectedProps } from 'react-redux'
import { STATE } from './types'
import { getCashFlowRecords, cashFlowSlice } from './cashflow'
import { getCategoryRecords, getCategoryGroupByWithTotalsRecords, categoryGroupByWithTotalsSlice } from './category'

const rootReducers = combineReducers({
  cashFlow: cashFlowSlice.reducer,
  categoryGroupByWithTotals: categoryGroupByWithTotalsSlice.reducer
})

export const store = configureStore({ reducer: rootReducers })

export type TRootState = ReturnType<typeof store.getState>
export type TAppDispatch = typeof store.dispatch

const mapStateToProps = (state: TRootState) => ({
  cashFlow: state.cashFlow,
  categoryGroupByWithTotals: state.categoryGroupByWithTotals
})

export const connector = connect(mapStateToProps)
export type TPropsFromRedux = ConnectedProps<typeof connector>

export type { STATE }

export {
  getCashFlowRecords,

  getCategoryRecords,
  getCategoryGroupByWithTotalsRecords
}