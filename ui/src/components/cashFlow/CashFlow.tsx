import { FC, useEffect } from 'react'
import { Grid } from '@mui/material'
import { TPropsFromRedux, connector, TAppDispatch } from '@budgettracker-reducers'
import { useDispatch } from 'react-redux'

const CashFlow: FC<TPropsFromRedux> = (props) => {
  const { cashFlow: { value: cashFlowDto, isLoading: loading }} = props

  return(
    <Grid container spacing={2}>
      <p>Cash Flow Page</p>
    </Grid>
  )
}

export default connector(CashFlow)