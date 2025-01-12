import { FC } from 'react'
import Dialog from '@mui/material/Dialog'
import DialogTitle from '@mui/material/DialogTitle'
import DialogContent from '@mui/material/DialogContent'
import Grid from '@mui/material/Grid'
import { TItemDto } from '@budgettracker-utils'
import { TColumn } from '@budgettracker-components-common'
import { BudgetTrackerDenseTable, BudgetTrackerPieChart } from '@budgettracker-components-common'
import { useCategoriesDialog } from './hooks'
import IconButton from '@mui/material/IconButton'
import CloseIcon from '@mui/icons-material/Close'

type TCategoriesDialog = {
  open: boolean
  itemDto: TItemDto[]
  handleOnClose: () => void
}

const columns: TColumn[] = [
  {
    id: 'itemName',
    label: 'Item Name',
    minWidth: 170
  },
  {
    id: 'amount',
    label: 'Amount',
    minWidth: 170,
    format: (value: number) => new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD'}).format(value)
  },
  {
    id: 'itemType',
    label: 'Item Type',
    minWidth: 170
  },
  {
    id: 'createDate',
    label: 'Create Date',
    minWidth: 170
  },
  {
    id: 'modifiedDate',
    label: 'Modified Date',
    minWidth: 170
  }
]

const CategoriesDialog: FC<TCategoriesDialog> = (props) => {
  const { open , itemDto, handleOnClose } = props
  const { pieChartData} = useCategoriesDialog(itemDto)

  return(
    <Dialog
      open={open}
      fullWidth={true}
      maxWidth={'xl'}
      scroll={'paper'}
    >
      <DialogTitle sx={{ m: 0, p: 2 }}>
        Category
      </DialogTitle>
      <IconButton
        onClick={handleOnClose}
        sx={{
          position: 'absolute',
          right: 8,
          top: 8,
          color: (theme) => theme.palette.grey[500],
        }}
      >
        <CloseIcon />
      </IconButton>
      <DialogContent dividers>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <BudgetTrackerPieChart
              data={pieChartData}
              width={800}
              height={400}
            />
          </Grid>
          <Grid item xs={12}>
            <BudgetTrackerDenseTable
              columns={columns}
              rows={itemDto}
            />
          </Grid>
        </Grid>
      </DialogContent>
    </Dialog>
  )
}

export default CategoriesDialog