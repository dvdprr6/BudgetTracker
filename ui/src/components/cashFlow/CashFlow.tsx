import { FC, useEffect } from 'react'
import { Grid } from '@mui/material'
import { TPropsFromRedux, connector, TAppDispatch } from '@budgettracker-reducers'
import { useDispatch } from 'react-redux'
import { BudgetTrackerLineChart } from '@budgettracker-components-common'

const data = [4000, 3000, 2000, 2780, 1890, 2390, 3490, 1000, 6000, 4500, 5500, 2000];
const xLabels = [
  'January',
  'February',
  'March',
  'April',
  'May',
  'June',
  'July',
  'August',
  'September',
  'October',
  'November',
  'December'
]

const CashFlow: FC<TPropsFromRedux> = (props) => {
  const { cashFlow: { value: cashFlowDto, isLoading: loading }} = props

  return(
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <BudgetTrackerLineChart
          data={data}
          xLabels={xLabels}
          width={1000}
          height={400}
          title={'Cash Flow'}
        />
      </Grid>
    </Grid>
  )
}

export default connector(CashFlow)