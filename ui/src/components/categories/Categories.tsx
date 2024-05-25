import { FC, useEffect } from 'react'
import { Grid } from '@mui/material'
import { TPropsFromRedux, connector, TAppDispatch, getCategoryGroupByWithTotalsRecords } from '@budgettracker-reducers'
import { useDispatch } from 'react-redux'
import { BudgetTrackerTable, TColumn, BudgetTrackerPieChart } from '@budgettracker-components-common'
import { useCategories } from './hooks'
import moment from 'moment'

const columns: TColumn[] = [
  {
    id: 'categoryName',
    label: 'Category Name',
    minWidth: 170
  },
  {
    id: 'total',
    label: 'Total',
    minWidth: 170,
    format: (value: number) => new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD'}).format(value)
  },
  {
    id: 'createDate',
    label: 'Create Date',
    minWidth: 170,
    formatString: (value: string) => moment(value).format('LLL')
  },
  {
    id: 'modifiedDate',
    label: 'Modified Date',
    minWidth: 170,
    formatString: (value: string) => moment(value).format('LLL')
  }
]

const Categories: FC<TPropsFromRedux> = (props) => {
  const { categoryGroupByWithTotals: { value: categoryGroupByWithTotalsDto }} = props
  const { pieChartData } = useCategories(categoryGroupByWithTotalsDto)
  const dispatch: TAppDispatch = useDispatch()

  useEffect(() => {
    function getCategoryRecords(): Promise<void>{
      dispatch(getCategoryGroupByWithTotalsRecords())

      return Promise.resolve()
    }

    getCategoryRecords().then()

  }, []);

  return (
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <BudgetTrackerPieChart data={pieChartData} width={800} height={400} />
      </Grid>
      <Grid item xs={12}>
        <BudgetTrackerTable columns={columns} rows={categoryGroupByWithTotalsDto} />
      </Grid>
    </Grid>
  )
}

export default connector(Categories)