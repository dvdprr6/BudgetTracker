import { FC } from 'react'
import BudgetTrackerTable from './BudgetTrackerTable'
import BudgetTrackerPieChart  from './BudgetTrackerPieChart'
import BudgetTrackerLineChart from './BudgetTrackerLineChart'
import BudgetTrackerDenseTable from './BudgetTrackerDenseTable'
import { TColumn } from './types'
import IconButton from '@mui/material/IconButton'
import CloseIcon from '@mui/icons-material/Close'

export {
  BudgetTrackerTable,
  BudgetTrackerPieChart,
  BudgetTrackerLineChart,
  BudgetTrackerDenseTable
}

export type { TColumn }

export const CloseDialogIcon: FC<{
  handleOnClose: () => void
}> = (props) => {
  const { handleOnClose } = props

  return (
    <IconButton
      onClick={handleOnClose}
      sx={{
        position: 'absolute',
        right: 8,
        top: 8,
        color: (theme) => theme.palette.grey[500],
      }}
    >
      <CloseIcon/>
    </IconButton>
  )
}