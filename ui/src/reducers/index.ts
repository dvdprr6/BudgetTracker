import { configureStore, combineReducers } from '@reduxjs/toolkit'
import { connect, ConnectedProps } from 'react-redux'
import { STATE } from './types'
import { getCashFlowRecords, cashFlowSlice } from './cashflow'
import { getCategoryRecords, categorySlice } from './category'

const rootReducers = combineReducers({
  cashFlow: cashFlowSlice.reducer,
  category: categorySlice.reducer
})

export const store = configureStore({ reducer: rootReducers })

export type TRootState = ReturnType<typeof store.getState>
export type TAppDispatch = typeof store.dispatch

const mapStateToProps = (state: TRootState) => ({
  cashFlow: state.cashFlow,
  category: state.category
})

export const connector = connect(mapStateToProps)
export type TPropsFromRedux = ConnectedProps<typeof connector>

export type { STATE }

export {
  getCashFlowRecords,
  getCategoryRecords
}