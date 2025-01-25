import { FC, useEffect, useState, useCallback } from 'react'
import {
  Grid, Box, Stack,
  Chip,
  CardContent, Card, CardActions,
  Button
} from '@mui/material'
import {
  TPropsFromRedux,
  connector,
  TAppDispatch,
  getCategoryGroupByWithTotalsRecords,
  getItemsByCategoryIdRecords
} from '@budgettracker-reducers'
import { useDispatch } from 'react-redux'
import { BudgetTrackerTable, TColumn, BudgetTrackerPieChart } from '@budgettracker-components-common'
import { useCategories } from './hooks'
import moment from 'moment'
import CategoriesDialog from './CategoriesDialog'

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
  const {
    categoryGroupByWithTotals: { value: categoryGroupByWithTotalsDto },
    itemsByCategory: { value: itemByCategoryDto }
  } = props
  const { pieChartData } = useCategories(categoryGroupByWithTotalsDto)
  const [openDialog, setOpenDialog] = useState<boolean>(false)
  const dispatch: TAppDispatch = useDispatch()

  useEffect(() => {
    function getCategoryRecords(): Promise<void>{
      dispatch(getCategoryGroupByWithTotalsRecords())
      return Promise.resolve()
    }
    getCategoryRecords().then()
  }, [])

  const handleCloseDialog = useCallback(() => {
    setOpenDialog(false)
  }, [openDialog])

  const handleCategoryOnClick = (categoryId: string) => {
    function getItemRecords(): Promise<void>{
      dispatch(getItemsByCategoryIdRecords(categoryId))
      return Promise.resolve()
    }

    getItemRecords().then(() => setOpenDialog(true))
  }

  return (
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <BudgetTrackerPieChart data={pieChartData} width={800} height={400} />
      </Grid>
      <Grid item xs={12}>
        <BudgetTrackerTable
          columns={columns}
          rows={categoryGroupByWithTotalsDto}
          handleOnClick={handleCategoryOnClick}
        />
      </Grid>
      <Grid item xs={12}>
        <CategoriesDialog
          itemDto={itemByCategoryDto}
          open={openDialog}
          handleOnClose={handleCloseDialog}
        />
      </Grid>
    </Grid>
  )
}

export default connector(Categories)