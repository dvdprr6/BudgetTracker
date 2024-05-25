import { FC, useEffect } from 'react'
import { Grid } from '@mui/material'
import { TPropsFromRedux, connector, TAppDispatch, getCashFlowRecords } from '@budgettracker-reducers'
import { useDispatch } from 'react-redux'
import { BudgetTrackerLineChart, TColumn, BudgetTrackerTable } from '@budgettracker-components-common'
import { useCashFlow } from './hooks'
import moment from 'moment'

const columns: TColumn[] = [
  {
    id: 'createDate',
    label: 'Month',
    minWidth: 170,
    formatString: (value: string) => moment(value).format('MMMM')
  },
  {
    id: 'amount',
    label: 'Cash',
    minWidth: 170,
    format: (value: number) => new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD'}).format(value)
  },
  {
    id: 'delta',
    label: 'Profit & Loss',
    minWidth: 170,
    format: (value: number) => new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD'}).format(value)
  },
  {
    id: 'createDate',
    label: 'Create Date',
    minWidth: 170,
    formatString: (value: string) => moment(value).format('LLL')
  }
]

const CashFlow: FC<TPropsFromRedux> = (props) => {
  const { cashFlow: { value: cashFlowDto }} = props
  const { amounts, months } = useCashFlow(cashFlowDto)
  const dispatch: TAppDispatch = useDispatch()

  useEffect(() => {
    function getCashFlow(): Promise<void>{
      dispatch(getCashFlowRecords())

      return Promise.resolve()
    }

    getCashFlow().then()
  }, [])

  return(
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <BudgetTrackerLineChart
          data={amounts}
          xLabels={months}
          width={1000}
          height={400}
          title={'Cash Flow'}
        />
      </Grid>
      <Grid item xs={12}>
        <BudgetTrackerTable columns={columns} rows={cashFlowDto} />
      </Grid>
    </Grid>
  )
}

export default connector(CashFlow)