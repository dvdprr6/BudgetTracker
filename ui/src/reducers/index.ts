import { configureStore, combineReducers } from '@reduxjs/toolkit'
import { connect, ConnectedProps } from 'react-redux'
import { STATE } from './types'
import { getCashFlowRecords, cashFlowSlice } from './cashflow'
import { getCategoryRecords, getCategoryGroupByWithTotalsRecords, categoryGroupByWithTotalsSlice } from './category'
import { getItemRecords, getItemsByCategoryIdRecords, itemsByCategorySlice, itemsSlice} from './item'

const rootReducers = combineReducers({
  cashFlow: cashFlowSlice.reducer,
  categoryGroupByWithTotals: categoryGroupByWithTotalsSlice.reducer,
  items: itemsSlice.reducer,
  itemsByCategory: itemsByCategorySlice.reducer
})

export const store = configureStore({ reducer: rootReducers })

export type TRootState = ReturnType<typeof store.getState>
export type TAppDispatch = typeof store.dispatch

const mapStateToProps = (state: TRootState) => ({
  cashFlow: state.cashFlow,
  categoryGroupByWithTotals: state.categoryGroupByWithTotals,
  items: state.items,
  itemsByCategory: state.itemsByCategory
})

export const connector = connect(mapStateToProps)
export type TPropsFromRedux = ConnectedProps<typeof connector>

export type { STATE }

export {
  getCashFlowRecords,

  getCategoryRecords,
  getCategoryGroupByWithTotalsRecords,

  getItemRecords,
  getItemsByCategoryIdRecords
}